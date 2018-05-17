package uk.nhs.digital.ps.chart.model;

public class Point {
    private final String name;
    private final Double x;
    private final Double y;

    public Point(String name, Double y) {
        this(name, null, y);
    }

    public Point(String name, Double x, Double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
