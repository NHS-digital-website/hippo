package uk.nhs.digital.ps.chart.model;

import uk.nhs.digital.ps.chart.ChartType;

import java.util.ArrayList;
import java.util.List;

public class Series<T> {
    private final String name;
    private final List<T> data;
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

    public void add(T value) {
        data.add(value);
    }

    public String getName() {
        return name;
    }

    public List<T> getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }
}
