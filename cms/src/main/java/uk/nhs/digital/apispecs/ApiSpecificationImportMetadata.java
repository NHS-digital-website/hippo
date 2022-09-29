package uk.nhs.digital.apispecs;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toList;

import org.apache.commons.lang3.builder.*;
import uk.nhs.digital.apispecs.util.CustomToStringStyle;

import java.time.Instant;
import java.util.*;

/**
 * Meta data of API Specifications processed during import.
 */
public class ApiSpecificationImportMetadata {

    final Map<String, Item> apiSpecsMetadata = new HashMap<>();

    private ApiSpecificationImportMetadata(final List<Item> apiSpecMetadataItems) {

        apiSpecMetadataItems.forEach(metadataItem ->
            this.apiSpecsMetadata.put(metadataItem.apiSpecJcrId(), metadataItem));
    }

    public static ApiSpecificationImportMetadata metadataWith(final List<Item> apiSpecsMetadata) {
        return new ApiSpecificationImportMetadata(apiSpecsMetadata);
    }

    public Item getBySpecJcrId(final String specJcrId) {
        if (!apiSpecsMetadata.containsKey(specJcrId)) {
            apiSpecsMetadata.put(specJcrId, Item.metadataItem(specJcrId, Instant.EPOCH));
        }

        return apiSpecsMetadata.get(specJcrId);
    }

    public Collection<Item> items() {
        return unmodifiableCollection(apiSpecsMetadata.values());
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApiSpecificationImportMetadata that = (ApiSpecificationImportMetadata) o;
        return apiSpecsMetadata.equals(that.apiSpecsMetadata);
    }

    @Override public int hashCode() {
        return Objects.hash(apiSpecsMetadata);
    }

    @Override public String toString() {
        return new ReflectionToStringBuilder(this, CustomToStringStyle.INSTANCE).build();
    }

    public List<Item> ofExistingApiSpecifications() {
        return items().stream().filter(Item::specExists).collect(toList());
    }

    /**
     * Metadata of a single API Specification processed during import.
     */
    public static class Item {

        private Instant lastChangeCheckInstant;
        private final String apiSpecJcrId;
        private boolean specExists;

        private Item(final String apiSpecJcrId, final Instant lastChangeCheckInstant) {
            this.lastChangeCheckInstant = lastChangeCheckInstant;
            this.apiSpecJcrId = apiSpecJcrId;
        }

        public static Item metadataItem(final String apiSpecJcrId, final Instant lastChangeCheckInstant) {
            return new Item(apiSpecJcrId, lastChangeCheckInstant);
        }

        public Instant lastChangeCheckInstant() {
            return lastChangeCheckInstant;
        }

        void setLastChangeCheckInstant(final Instant lastChangeCheckInstant) {
            this.lastChangeCheckInstant = lastChangeCheckInstant;
        }

        public String apiSpecJcrId() {
            return apiSpecJcrId;
        }

        @Override public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Item that = (Item) o;

            return new EqualsBuilder()
                .append(lastChangeCheckInstant, that.lastChangeCheckInstant)
                .append(apiSpecJcrId, that.apiSpecJcrId)
                .append(specExists, that.specExists)
                .isEquals();
        }

        @Override public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(lastChangeCheckInstant)
                .append(apiSpecJcrId)
                .append(specExists)
                .toHashCode();
        }

        @Override public String toString() {
            return new ToStringBuilder(this, CustomToStringStyle.INSTANCE)
                .append("lastChangeCheckInstant", lastChangeCheckInstant.toString())
                .append("apiSpecJcrId", apiSpecJcrId)
                .append("specExists", specExists)
                .toString();
        }

        public boolean specExists() {
            return specExists;
        }

        public void setSpecExists() {
            this.specExists = true;
        }
    }
}
