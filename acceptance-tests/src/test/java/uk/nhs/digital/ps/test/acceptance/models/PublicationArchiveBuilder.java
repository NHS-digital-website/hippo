package uk.nhs.digital.ps.test.acceptance.models;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class PublicationArchiveBuilder {

    private String title;
    private List<PublicationBuilder> publicationBuilders = new ArrayList<>();
    private String name;
    private String summary;

    public static PublicationArchiveBuilder newPublicationArchive() {
        return new PublicationArchiveBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public PublicationArchiveBuilder withName(final String name) {
        return cloneAndAmend(builder -> builder.name = name);
    }

    public PublicationArchiveBuilder withTitle(final String title) {
        return cloneAndAmend(builder -> builder.title = title);
    }

    public PublicationArchiveBuilder withPublications(final PublicationBuilder... publicationBuilders) {
        return cloneAndAmend(builder -> builder.publicationBuilders = asList(publicationBuilders));
    }

    public PublicationArchiveBuilder withSummary(final String summary) {
        return cloneAndAmend(builder -> builder.summary = summary);
    }
    //</editor-fold>


    public PublicationArchive build() {
        return new PublicationArchive(this);
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

    private PublicationArchiveBuilder() {
    }

    private PublicationArchiveBuilder(final PublicationArchiveBuilder original) {
        name = original.getName();
        title = original.getTitle();
        summary = original.getSummary();
        publicationBuilders = original.getPublicationBuilders();
    }

    private PublicationArchiveBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final PublicationArchiveBuilder clone = new PublicationArchiveBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(PublicationArchiveBuilder builder);
    }

}
