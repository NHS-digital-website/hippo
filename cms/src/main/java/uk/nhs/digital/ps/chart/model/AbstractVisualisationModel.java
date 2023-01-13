package uk.nhs.digital.ps.chart.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVisualisationModel {
    private final Title title;
    private final List<Series> series;

    public AbstractVisualisationModel(String title, List<Series> series) {
        this.title = new Title(title);
        this.series = series;
    }

    public AbstractVisualisationModel(String title) {
        this.title = new Title(title);
        this.series = new ArrayList<>();
    }

    public Title getTitle() {
        return title;
    }

    public List<Series> getSeries() {
        return series;
    }
}
