package org.example;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.input.CenterMapListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Map {

    public static JFrame createMapFrame(List<Cluster> clusters) {
        JFrame frame = new JFrame("Clustering Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JXMapViewer mapViewer = new JXMapViewer();

        //set the tile factory (OpenStreetMap)
        OSMTileFactoryInfo tileFactoryInfo = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(tileFactoryInfo);
        mapViewer.setTileFactory(tileFactory);

        //initial focus location
        GeoPosition startPosition = new GeoPosition(48.7758, 9.1829); //Stuttgart, Germany
        mapViewer.setZoom(11);
        mapViewer.setAddressLocation(startPosition);

        //generate waypoints for clusters and records
        //separate the waypoints into two: one for cluster centers and one for records
        Set<ColoredWaypoint> clusterCenterWaypoints = new HashSet<>();
        Set<ColoredWaypoint> recordWaypoints = new HashSet<>();
        Random random = new Random();

        for (Cluster cluster : clusters) {
            //random color for the cluster
            Color clusterColor = new Color(random.nextInt(200) + 55, random.nextInt(200) + 55, random.nextInt(200) + 55);

            //waypoints for records in this cluster
            for (Record record : cluster.getRecords()) {
                GeoPosition recordPosition = new GeoPosition(record.getLa(), record.getLo());
                recordWaypoints.add(new ColoredWaypoint(recordPosition, clusterColor));
            }

            //waypoint for the cluster center
            GeoPosition centerPosition = new GeoPosition(cluster.getCenterLat(), cluster.getCenterLon());
            clusterCenterWaypoints.add(new ColoredWaypoint(centerPosition, Color.BLACK, true)); //cluster black
        }

        //2 separate waypoint painters
        WaypointPainter<ColoredWaypoint> recordPainter = new WaypointPainter<>();
        recordPainter.setWaypoints(recordWaypoints);
        recordPainter.setRenderer((g, map, wp) -> {
            Point2D point = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());
            if (point == null) return;

            Graphics2D g2d = g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(wp.getColor());

            int size = 7; //waypoint size
            g2d.fillOval((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
        });

        WaypointPainter<ColoredWaypoint> centerPainter = new WaypointPainter<>();
        centerPainter.setWaypoints(clusterCenterWaypoints);
        centerPainter.setRenderer((g, map, wp) -> {
            Point2D point = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());
            if (point == null) return;

            Graphics2D g2d = g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 15; //cluster size
            g2d.setColor(Color.WHITE);
            //int outlineSize = size + 4; // Add an outline for better visibility
            //g2d.fillOval((int) point.getX() - outlineSize / 2, (int) point.getY() - outlineSize / 2, outlineSize, outlineSize); - for outline of the cluster

            //draw the cluster center
            g2d.setColor(wp.getColor());
            g2d.fillOval((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
        });

        //combine painters
        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>();
        compoundPainter.setPainters(recordPainter, centerPainter);

        //sets compound painter on the map
        mapViewer.setOverlayPainter(compoundPainter);

        //listeners for panning and zooming
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        frame.add(mapViewer, BorderLayout.CENTER);

        return frame;
    }
}
