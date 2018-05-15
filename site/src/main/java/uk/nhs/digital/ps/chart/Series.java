package uk.nhs.digital.ps.chart;

import java.util.ArrayList;
import java.util.List;

public class Series {
    private final String name;
    private final List data;

    public Series(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    public void add(Object value) {
        data.add(value);
    }

    public String getName() {
        return name;
    }

    public List getData() {
        return data;
    }
}
