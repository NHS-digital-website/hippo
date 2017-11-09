package uk.nhs.digital.ps.test.acceptance.models;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class PublicationSeriesBuilder {

    private String title;
    private List<PublicationBuilder> publicationBuilders = new ArrayList<>();
    private String name;

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

    public PublicationSeriesBuilder withPublications(final List<PublicationBuilder> publicationBuilders) {
        return cloneAndAmend(builder -> builder.publicationBuilders = publicationBuilders);
    }
    public PublicationSeriesBuilder withPublications(final PublicationBuilder... publicationBuilders) {
        return cloneAndAmend(builder -> builder.publicationBuilders = asList(publicationBuilders));
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
    //</editor-fold>

    private PublicationSeriesBuilder() {
    }

    private PublicationSeriesBuilder(final PublicationSeriesBuilder original) {
        name = original.getName();
        title = original.getTitle();
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
