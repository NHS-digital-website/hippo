package uk.nhs.digital.ps.test.acceptance.models;

import java.time.Instant;

public class DatasetBuilder {

    private String name;
    private String title;
    private String summary;
    private Instant nominalDate;

    public static DatasetBuilder newDataset() {
        return new DatasetBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public DatasetBuilder withName(String name) {
        return cloneAndAmend(builder -> builder.name = name);
    }

    public DatasetBuilder withTitle(String title) {
        return cloneAndAmend(builder -> builder.title = title);
    }

    public DatasetBuilder withSummary(String summary) {
        return cloneAndAmend(builder -> builder.summary = summary);
    }

    public DatasetBuilder withNominalDate(Instant nominalDate) {
        return cloneAndAmend(builder -> builder.nominalDate = nominalDate);
    }
    //</editor-fold>

    public Dataset build() {
        return new Dataset(this);
    }

    //<editor-fold desc="GETTERS" defaultstate="collapsed">
    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Instant getNominalDate() {
        return nominalDate;
    }
    //</editor-fold>

    private DatasetBuilder() {
    }

    private DatasetBuilder(final DatasetBuilder original) {
        name = original.getName();
        title = original.getTitle();
        summary = original.getSummary();
        nominalDate = original.getNominalDate();
    }

    private DatasetBuilder cloneAndAmend(final DatasetBuilder.PropertySetter propertySetter) {
        final DatasetBuilder clone = new DatasetBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(DatasetBuilder builder);
    }
}
