package uk.nhs.digital.ps.chart.model;

import java.util.ArrayList;
import java.util.List;

public class Series {
    private final String name;
    private final List<Point> data;

    public Series(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    public void add(Point value) {
        data.add(value);
    }

    public String getName() {
        return name;
    }

    public List<Point> getData() {
        return data;
    }
}
