package org.example;

public class Record extends FileReading{
    private String name;
    private double capacity;
    private Double la;
    private Double lo;

    public Record(Double la, Double lo) {
        this.la = Double.valueOf(la);
        this.lo = Double.valueOf(lo);
    }

    public String getName() {
        return name;
    }

    public double getCapacity() {
        return capacity;
    }

    public Double getLa() {
        return la;
    }

    public Double getLo() {
        return lo;
    }
    /*@Override
    public String toString() {
        return "Station{name='" + name + "', capacity=" + capacity + ", la='" + la + "', lo='" + lo + "'}";
    }*/
}
