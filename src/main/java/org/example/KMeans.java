package org.example;

import java.util.ArrayList;
import java.util.List;

public class KMeans {
    private List<Cluster> clusters;
    private List<Record> records;
    private int k; //k=number of clusters

    public KMeans(List<Record> records, int k) {
        this.records = records;
        this.k = k;
        this.clusters = new ArrayList<>();
    }

    //initialize clusters randomly by picking k records as initial cluster centers
    public void initializeClusters() {
        for (int i = 0; i < k; i++) {
            Record record = records.get(i);
            double lat = record.getLa();
            double lon = record.getLo();
            clusters.add(new Cluster(lat, lon));
        }
    }

    public void performClustering(EuclideanDistance distance) {
        boolean isChanged = true;

        while (isChanged){
            isChanged = false;
            //clear previous record assignments for each cluster
            for (Cluster cluster : clusters) {
                cluster.clearRecords();  //reset the cluster's records
            }

            //assign each record to the closest cluster based on the current cluster centers
            for (Record record : records) {
                double minDistance = Double.MAX_VALUE;
                Cluster closestCluster = null;
                //find the closest cluster by calculating the Euclidean distance

                for (Cluster cluster : clusters) {
                    double dist = distance.calculate(record, cluster);
                    if (dist < minDistance) {
                        minDistance = dist;
                        closestCluster = cluster;
                    }
                }

                //add the record to the closest cluster
                closestCluster.addRecord(record);
            }

            //update the cluster centers after all records are assigned
            for (Cluster cluster : clusters) {
                //save the current center for comparison
                double oldCenterLat = cluster.getCenterLat();
                double oldCenterLon = cluster.getCenterLon();

                //update the center
                Coordinate newCenter = cluster.updateCenter();

                //check if the center has changed
                if (newCenter != null &&
                        (oldCenterLat != newCenter.getLat() || oldCenterLon != newCenter.getLon())) {
                    isChanged = true; //centers changed, continue clustering
                }
            }
        }
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

}



