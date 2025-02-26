package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Map extends JPanel {
    private static final int TILE_SIZE = 256;
    private static final int INITIAL_ZOOM = 3;
    private double latitude = 41.9981; // Initial latitude (Bitola, Macedonia)
    private double longitude = 21.4254; // Initial longitude
    private int zoom = INITIAL_ZOOM;

    public Map() {
        // Mouse wheel listener for zooming
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                zoom = Math.min(zoom + 1, 19); // Max zoom level
            } else {
                zoom = Math.max(zoom - 1, 0); // Min zoom level
            }
            repaint();
        });

        // Mouse listener for panning
        addMouseMotionListener(new MouseAdapter() {
            private Point lastPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastPoint != null) {
                    int deltaX = e.getX() - lastPoint.x;
                    int deltaY = e.getY() - lastPoint.y;
                    longitude -= deltaX / (double) TILE_SIZE * 360 / Math.pow(2, zoom);
                    latitude += deltaY / (double) TILE_SIZE * 180 / Math.pow(2, zoom);
                    longitude = (longitude + 180) % 360 - 180; // Keep longitude in [-180, 180]
                    latitude = Math.max(-85.0511, Math.min(85.0511, latitude)); // Clamp latitude
                    lastPoint = e.getPoint();
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        int tileX = lonToTileX(longitude, zoom);
        int tileY = latToTileY(latitude, zoom);

        int tilesX = width / TILE_SIZE + 2;
        int tilesY = height / TILE_SIZE + 2;

        for (int dx = -tilesX / 2; dx <= tilesX / 2; dx++) {
            for (int dy = -tilesY / 2; dy <= tilesY / 2; dy++) {
                try {
                    int x = tileX + dx;
                    int y = tileY + dy;
                    BufferedImage tile = fetchTile(zoom, x, y);
                    if (tile != null) {
                        int screenX = width / 2 + dx * TILE_SIZE;
                        int screenY = height / 2 + dy * TILE_SIZE;
                        g.drawImage(tile, screenX, screenY, TILE_SIZE, TILE_SIZE, null);
                    }
                } catch (Exception e) {
                    // Handle missing tiles or network issues
                    e.printStackTrace();
                }
            }
        }
    }

    private BufferedImage fetchTile(int zoom, int x, int y) throws IOException {
        // Ensure x and y are within valid bounds
        int maxTileIndex = (1 << zoom) - 1; // 2^zoom - 1
        if (x < 0 || x > maxTileIndex || y < 0 || y > maxTileIndex) {
            return null; // Return null for invalid tile coordinates
        }

        // Construct the URL for the tile
        String tileUrl = String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoom, x, y);
        URL url = new URL(tileUrl);

        // Add User-Agent header
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "MyMapViewer/1.0 (your-email@example.com)");

        // Check response code
        if (connection.getResponseCode() != 200) {
            return null; // Return null if the tile is not available
        }

        // Return the tile as a BufferedImage
        return ImageIO.read(connection.getInputStream());
    }


    public static int lonToTileX(double lon, int zoom) {
        return (int) Math.floor((lon + 180) / 360 * Math.pow(2, zoom));
    }

    public static int latToTileY(double lat, int zoom) {
        double radLat = Math.toRadians(lat);
        return (int) Math.floor((1 - Math.log(Math.tan(radLat) + 1 / Math.cos(radLat)) / Math.PI) / 2 * Math.pow(2, zoom));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Map Viewer");
        Map mapViewer = new Map();
        frame.add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
