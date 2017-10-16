package uk.nhs.digital.ps.test.acceptance.models;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class TimeSeriesBuilder {

    private String title;
    private List<PublicationBuilder> publicationBuilders = new ArrayList<>();
    private String name;

    public static TimeSeriesBuilder newTimeSeries() {
        return new TimeSeriesBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public TimeSeriesBuilder withName(final String name) {
        return cloneAndAmend(builder -> builder.name = name);
    }

    public TimeSeriesBuilder withTitle(final String title) {
        return cloneAndAmend(builder -> builder.title = title);
    }

    public TimeSeriesBuilder withPublications(final List<PublicationBuilder> publicationBuilders) {
        return cloneAndAmend(builder -> builder.publicationBuilders = publicationBuilders);
    }
    public TimeSeriesBuilder withPublications(final PublicationBuilder... publicationBuilders) {
        return cloneAndAmend(builder -> builder.publicationBuilders = asList(publicationBuilders));
    }
    //</editor-fold>


    public TimeSeries build() {
        return new TimeSeries(this);
    }

    //<editor-fold desc="GETTERS" defaultstate="collapsed">
    String getTitle() {
        return title;
    }

    List<PublicationBuilder> getPublicationBuilders() {
        return new ArrayList<>(publicationBuilders);
    }

    List<Publication> getPublications() {
        return publicationBuilders.stream().map(PublicationBuilder::build).collect(toList());
    }

    String getName() {
        return name;
    }
    //</editor-fold>

    private TimeSeriesBuilder() {
    }

    private TimeSeriesBuilder(final TimeSeriesBuilder original) {
        name = original.getName();
        title = original.getTitle();
        publicationBuilders = original.getPublicationBuilders();
    }

    private TimeSeriesBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final TimeSeriesBuilder clone = new TimeSeriesBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(TimeSeriesBuilder builder);
    }

}
