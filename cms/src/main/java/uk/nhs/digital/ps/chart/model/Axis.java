package uk.nhs.digital.ps.chart.model;

import java.util.List;

public class Axis {
    private Title title;
    private List<String> categories = null;

    public Axis(String title, List<String> categories) {
        this.title = new Title(title);
        this.categories = categories;
    }

    public Title getTitle() {
        return title;
    }

    public List<String> getCategories() {
        return categories;
    }
}
