package uk.nhs.digital.ps.chart.model;

import java.util.List;

public class Categories {
    private final List<String> categories;

    public Categories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategories() {
        return categories;
    }
}
