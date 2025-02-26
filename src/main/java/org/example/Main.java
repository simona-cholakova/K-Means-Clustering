package org.example;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SwingUtilities.invokeLater(() -> {
            //ask the user for their preference for width and height of the frame
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Do you want the map to be the default size or set it manually?",
                    "Map Size Preference",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Default Size", "Set Manually"},
                    "Default Size"
            );

            //default map frame dimensions
            int width = 800;
            int height = 600;

            //if the user chooses to set the size manually
            if (choice == JOptionPane.NO_OPTION) {
                try {
                    String widthInput = JOptionPane.showInputDialog(null, "Enter the width:");
                    String heightInput = JOptionPane.showInputDialog(null, "Enter the height:");

                    //parse user input and validate dimensions
                    width = Integer.parseInt(widthInput);
                    height = Integer.parseInt(heightInput);

                    if (width <= 0 || height <= 0) {
                        JOptionPane.showMessageDialog(null, "Map will be displayed in default size.");
                        width = 800;
                        height = 600;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Map will be displayed in default size.");
                    width = 800;
                    height = 600;
                }
            }

            //ask the user for the number of clusters (k)
            int k = 0;
            while (k <= 0) {
                try {
                    String kInput = JOptionPane.showInputDialog(null, "Enter the number of clusters (k):");
                    k = Integer.parseInt(kInput);
                    if (k <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a positive integer for clusters.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
                }
            }

            //ask the user for the number of accumulation sites
            int accumulationSites = 0;
            while (accumulationSites <= 0) {
                try {
                    String sitesInput = JOptionPane.showInputDialog(null, "Enter the number of accumulation sites:");
                    accumulationSites = Integer.parseInt(sitesInput);
                    if (accumulationSites <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a positive integer for accumulation sites.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
                }
            }

            //display the selected values
            JOptionPane.showMessageDialog(null, "Map Size: " + width + "x" + height +
                    "\nNumber of Clusters (k): " + k +
                    "\nNumber of Accumulation Sites: " + accumulationSites);

            //load records from the file
            List<Record> records = FileReading.loadRecords("src/main/java/germany/germany.json");

            //validate that records were loaded
            if (records.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No records found. Exiting.");
                return;
            }

            //if number of accumulation sites is bigger than the number of sites in the file
            if (accumulationSites > records.size()) {
                JOptionPane.showMessageDialog(null, "Generating additional random locations...");

                int additionalSites = accumulationSites - records.size();

                //generate additional random locations
                Random random = new Random();
                List<Region> landRegions = RegionData.getLandRegions();

                for (int i = 0; i < additionalSites; i++) {
                    int regionIndex = random.nextInt(landRegions.size());
                    Region region = landRegions.get(regionIndex);

                    double randomLatitude = region.getLatStart() + (region.getLatEnd() - region.getLatStart()) * random.nextDouble();
                    double randomLongitude = region.getLonStart() + (region.getLonEnd() - region.getLonStart()) * random.nextDouble();

                    Record randomRecord = new Record(randomLatitude, randomLongitude);
                    records.add(randomRecord);
                }
            }

            //select the desired number of accumulation sites
            List<Record> filteredRecords = records.subList(0, accumulationSites);

            KMeans kMeans = new KMeans(filteredRecords, k);

            //initializing clusters randomly
            kMeans.initializeClusters();

            EuclideanDistance distance = new EuclideanDistance();

            //actual clustering
            kMeans.performClustering(distance);

            //displaying clusters' centers and sizes
            List<Cluster> clusters = kMeans.getClusters();
            StringBuilder clusterDetails = new StringBuilder();
            for (int i = 0; i < clusters.size(); i++) {
                Cluster cluster = clusters.get(i);
                clusterDetails.append("Cluster ").append(i + 1).append(" Center: (")
                        .append(cluster.getCenterLat()).append(", ")
                        .append(cluster.getCenterLon()).append(") with ")
                        .append(cluster.getRecords().size()).append(" records.\n");
            }

            //clusters info in a dialog box
            JOptionPane.showMessageDialog(null, clusterDetails.toString());

            //the map
            JFrame mapFrame = Map.createMapFrame(clusters);
            mapFrame.setSize(width, height);
            mapFrame.setVisible(true);
            long end = System.currentTimeMillis() - start;
            System.out.println("The program has been running for: " + end + "ms");
        });
    }
}
