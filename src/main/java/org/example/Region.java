package org.example;

public class Region {
    private double latStart;
    private double lonStart;
    private double latEnd;
    private double lonEnd;

    public Region(double latStart, double lonStart, double latEnd, double lonEnd) {
        this.latStart = latStart;
        this.lonStart = lonStart;
        this.latEnd = latEnd;
        this.lonEnd = lonEnd;
    }

    public double getLatStart() {
        return latStart;
    }

    public double getLonStart() {
        return lonStart;
    }

    public double getLatEnd() {
        return latEnd;
    }

    public double getLonEnd() {
        return lonEnd;
    }
}
