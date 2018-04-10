package uk.nhs.digital.ps.test.acceptance.models;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class PublicationSeriesBuilder {

    private String title;
    private List<PublicationBuilder> publicationBuilders = new ArrayList<>();
    private String name;
    private String summary;

    public static PublicationSeriesBuilder newPublicationSeries() {
        return new PublicationSeriesBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public PublicationSeriesBuilder withName(final String name) {
        return cloneAndAmend(builder -> builder.name = name);
    }

    public PublicationSeriesBuilder withTitle(final String title) {
        return cloneAndAmend(builder -> builder.title = title);
    }

    public PublicationSeriesBuilder withPublications(final PublicationBuilder... publicationBuilders) {
        return cloneAndAmend(builder -> builder.publicationBuilders = asList(publicationBuilders));
    }

    public PublicationSeriesBuilder withSummary(final String summary) {
        return cloneAndAmend(builder -> builder.summary = summary);
    }
    //</editor-fold>


    public PublicationSeries build() {
        return new PublicationSeries(this);
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

    String getSummary() {
        return summary;
    }
    //</editor-fold>

    private PublicationSeriesBuilder() {
    }

    private PublicationSeriesBuilder(final PublicationSeriesBuilder original) {
        name = original.getName();
        title = original.getTitle();
        summary = original.getSummary();
        publicationBuilders = original.getPublicationBuilders();
    }

    private PublicationSeriesBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final PublicationSeriesBuilder clone = new PublicationSeriesBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(PublicationSeriesBuilder builder);
    }

}
