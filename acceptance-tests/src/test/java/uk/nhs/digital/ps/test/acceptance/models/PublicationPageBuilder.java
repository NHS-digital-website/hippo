package uk.nhs.digital.ps.test.acceptance.models;

import static java.util.Collections.emptyList;

import uk.nhs.digital.ps.test.acceptance.models.section.BodySection;

import java.util.List;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class PublicationPageBuilder {

    private String name;
    private String title;
    private List<BodySection> bodySections = emptyList();

    public static PublicationPageBuilder newPublicationPage() {
        return new PublicationPageBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public PublicationPageBuilder withName(final String name) {
        return cloneAndAmend(builder -> builder.name = name);
    }

    public PublicationPageBuilder withTitle(final String title) {
        return cloneAndAmend(builder -> builder.title = title);
    }

    public PublicationPageBuilder withBodySections(final List<BodySection> bodySections) {
        return cloneAndAmend(builder -> builder.bodySections = bodySections);
    }
    //</editor-fold>

    public Page build() {
        return new Page(this);
    }

    //<editor-fold desc="GETTERS" defaultstate="collapsed">
    String getName() {
        return name;
    }

    String getTitle() {
        return title;
    }

    public List<BodySection> getBodySections() {
        return bodySections;
    }
    //</editor-fold>

    private PublicationPageBuilder(final PublicationPageBuilder original) {
        name = original.getName();
        title = original.getTitle();
        bodySections = original.getBodySections();
    }

    private PublicationPageBuilder() {
        // no-op; made private to promote the use of factory methods
    }

    private PublicationPageBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final PublicationPageBuilder clone = new PublicationPageBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(PublicationPageBuilder builder);
    }
}
