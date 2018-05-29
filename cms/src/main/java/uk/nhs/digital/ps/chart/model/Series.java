package uk.nhs.digital.ps.chart.model;

import uk.nhs.digital.ps.chart.ChartType;

import java.util.ArrayList;
import java.util.List;

public class Series {
    private final String name;
    private final List<Point> data;
    private Tooltip tooltip;
    private String type;

    public Series(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    public Series(String name, ChartType type, Tooltip tooltip) {
        this(name);
        this.type = type.getHighChartsType();
        this.tooltip = tooltip;
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

    public String getType() {
        return type;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }
}
