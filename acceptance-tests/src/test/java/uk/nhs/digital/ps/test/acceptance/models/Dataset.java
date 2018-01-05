package uk.nhs.digital.ps.test.acceptance.models;

import java.time.Instant;

public class Dataset {

    private String name;
    private String title;
    private String summary;
    private Instant nominalDate;

    public Dataset(DatasetBuilder builder) {
        this.name = builder.getName();
        this.title = builder.getTitle();
        this.summary = builder.getSummary();
        this.nominalDate = builder.getNominalDate();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrlName() {
        return name.toLowerCase().replace(' ', '-');
    }

    public Instant getNominalDate() {
        return nominalDate;
    }
}
