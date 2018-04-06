package uk.nhs.digital.ps.test.acceptance.models;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class LegacyPublicationBuilder {

    private String name;
    private String title;
    private Instant nominalDate;
    private boolean publiclyAccessible;
    private List<ResourceBuilder> resourceBuilders = new ArrayList<>();

    private PublicationState state;

    public static LegacyPublicationBuilder newLegacyPublication() {
        return new LegacyPublicationBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public LegacyPublicationBuilder withName(final String name) {
        return cloneAndAmend(builder -> builder.name = name);
    }

    public LegacyPublicationBuilder withTitle(final String title) {
        return cloneAndAmend(builder -> builder.title = title);
    }

    public LegacyPublicationBuilder withNominalDate(final Instant nominalDate) {
        return cloneAndAmend(builder -> builder.nominalDate = nominalDate);
    }

    public LegacyPublicationBuilder withAttachments(final List<ResourceBuilder> resourceBuilders) {
        return cloneAndAmend(builder -> builder.resourceBuilders = resourceBuilders);
    }

    public LegacyPublicationBuilder withAttachments(final ResourceBuilder... resourceBuilders) {
        return cloneAndAmend(builder -> builder.resourceBuilders = asList(resourceBuilders));
    }

    public LegacyPublicationBuilder inState(final PublicationState state) {
        return cloneAndAmend(builder -> builder.state = state);
    }

    public LegacyPublicationBuilder withPubliclyAccessible(final boolean publiclyAccessible) {
        return cloneAndAmend(builder -> builder.publiclyAccessible = publiclyAccessible);
    }
    //</editor-fold>

    public LegacyPublication build() {
        return new LegacyPublication(this);
    }

    //<editor-fold desc="GETTERS" defaultstate="collapsed">
    String getName() {
        return name;
    }

    String getTitle() {
        return title;
    }

    Instant getNominalDate() {
        return nominalDate;
    }

    List<Resource> getAttachments() {
        return getAttachmentBuilders().stream().map(ResourceBuilder::build).collect(toList());
    }

    List<ResourceBuilder> getAttachmentBuilders() {
        return new ArrayList<>(resourceBuilders);
    }

    PublicationState getState() {
        return state;
    }

    public boolean isPubliclyAccessible() {
        return publiclyAccessible;
    }
    //</editor-fold>

    private LegacyPublicationBuilder(final LegacyPublicationBuilder original) {
        name = original.getName();
        title = original.getTitle();
        nominalDate = original.getNominalDate();
        publiclyAccessible = original.isPubliclyAccessible();
        resourceBuilders = original.getAttachmentBuilders();

        state = original.getState();
    }

    private LegacyPublicationBuilder() {
        // no-op; made private to promote the use of factory methods
    }

    private LegacyPublicationBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final LegacyPublicationBuilder clone = new LegacyPublicationBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(LegacyPublicationBuilder builder);
    }

    public static Collection collectionOf(final LegacyPublicationBuilder... publicationBuilders) {
        return new Collection(publicationBuilders);
    }

    public static class Collection {

        private final List<LegacyPublicationBuilder> legacyPublicationBuilders;

        Collection(final LegacyPublicationBuilder... legacyPublicationBuilders) {
            this.legacyPublicationBuilders = asList(legacyPublicationBuilders);
        }

        public List<LegacyPublication> build() {
            return legacyPublicationBuilders.stream().map(LegacyPublicationBuilder::build).collect(toList());
        }
    }
}
