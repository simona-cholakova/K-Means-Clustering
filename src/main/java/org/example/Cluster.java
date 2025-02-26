package org.example;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private double centerLat;
    private double centerLon;
    private List<Record> records;

    public Cluster(double centerLat, double centerLon) {
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.records = new ArrayList<>();
    }

    public double getCenterLat() {
        return centerLat;
    }

    public double getCenterLon() {
        return centerLon;
    }

    //update the cluster center by recalculating the average position
    public Coordinate updateCenter() {
        if (records.isEmpty()) {
            return null; //skip if no records are assigned to the cluster
        }

        double totalLat = 0;
        double totalLon = 0;
        for (Record record : records) {
            totalLat += record.getLa();
            totalLon += record.getLo();
        }

        centerLat = totalLat / records.size();  //average latitude
        centerLon = totalLon / records.size();  //average longitude
        return new Coordinate(centerLat, centerLon);
    }

    public void clearRecords() {
        records.clear();
    }

    public void addRecord(Record record) {
        records.add(record);
    }
    public List<Record> getRecords() {
        return records;
    }
}
