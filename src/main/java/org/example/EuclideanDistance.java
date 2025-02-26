package org.example;

public class EuclideanDistance implements Distance<Record, Cluster> {

    //calculates the distance between a Record and a Cluster
    @Override
    public double calculate(Record record, Cluster cluster) {
        //parse latitude and longitude as doubles from the strings in the Record
        double lat1 = record.getLa();
        double lon1 = record.getLo();

        //get the center coordinates of the Cluster
        double lat2 = cluster.getCenterLat();
        double lon2 = cluster.getCenterLon();

        //calculate the Euclidean distance between the two points (latitude, longitude)
        double deltaLat = lat1 - lat2;
        double deltaLon = lon1 - lon2;

        //apply the Euclidean formula for distance with ignoring Earth's curvature
        return Math.sqrt(deltaLat * deltaLat + deltaLon * deltaLon);
    }

}
