package uk.nhs.digital.ps.chart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public abstract class AbstractHighchartsModel {
    @JsonIgnore
    private final Title title;
    private final List<Series> series;

    public AbstractHighchartsModel(String title, List<Series> series) {
        this.title = new Title(title);
        this.series = series;
    }

    public Title getTitle() {
        return title;
    }

    public List<Series> getSeries() {
        return series;
    }
}
