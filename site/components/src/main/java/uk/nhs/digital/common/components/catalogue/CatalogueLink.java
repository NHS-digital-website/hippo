package uk.nhs.digital.common.components.catalogue;

import static java.util.stream.Collectors.toList;

import uk.nhs.digital.website.beans.Externallink;
import uk.nhs.digital.website.beans.Internallink;

import java.util.*;

/**
 * Links in documents of type 'Links list' - the same doc type used to back API catalogue - come in three flavours:
 * internal, external and asset. Of these API catalogue only supports the first two. Bean classes that represent these two
 * are {@linkplain uk.nhs.digital.website.beans.Internallink} and {@linkplain uk.nhs.digital.website.beans.Externallink}.
 * The former can be tagged with taxonomy terms and the latter cannot. They do not have a useful, common base class or interface
 * , which makes it awkward to make decisions based on type. This class is provided as a convenience wrapper to make it easier
 * to deal with this awkwardness in a more object-oriented way and promote better type safety.
 */
public class CatalogueLink {

    private final Object rawLink;

    private CatalogueLink(final Object rawLink) {
        this.rawLink = rawLink;
    }

    public static CatalogueLink from(final Object rawLink) {
        return new CatalogueLink(rawLink);
    }

    static List<CatalogueLink> linksFrom(final List<?> rawLinkCandidates) {
        return rawLinkCandidates.stream()
            .map(CatalogueLink::from)
            .collect(toList());
    }

    Object raw() {
        return rawLink;
    }

    boolean notTaggedWithAnyOf(final Set<String> filterKeys) {
        return allTaxonomyKeysOfReferencedDoc().stream()
            .noneMatch(filterKeys::contains);
    }

    public boolean taggedWith(final List<String> filterKeys) {
        final Set<String> taxonomyKeysOfLinkedDoc = allTaxonomyKeysOfReferencedDoc();
        return filterKeys.isEmpty() || !Collections.disjoint(taxonomyKeysOfLinkedDoc, filterKeys);
    }

    public boolean taggedWith(final String filterKey) {
        final Set<String> taxonomyKeysOfLinkedDoc = allTaxonomyKeysOfReferencedDoc();
        return taxonomyKeysOfLinkedDoc.contains(filterKey);
    }

    /**
     * @return Keys of all taxonomy terms the referenced doc is tagged with or empty collection
     * (never {@code null}) when the document is not tagged with any terms or is {@linkplain #notFilterable}.
     */
    public Set<String> allTaxonomyKeysOfReferencedDoc() {
        return notFilterable()
            ? Collections.emptySet()
            : new HashSet<>(Arrays.asList((String[]) ((Internallink) raw())
            .getLink()
            .getProperties()
            .getOrDefault("hippotaxonomy:keys", new String[0])));
    }

    public String getIdOfLink() {
        if (isFilterable()) {
            return ((Internallink) raw()).getId();
        } else {
            return ((Externallink) raw()).getId();
        }
    }

    /**
     * @return {@code true} if document referenced by this link can not be tagged with taxonomy terms (their keys);
     * {@code false} otherwise.
     */
    boolean notFilterable() {
        return !isFilterable();
    }

    /**
     * @return {@code true} if document referenced by this link can be tagged with taxonomy terms (their keys);
     * {@code false} otherwise.
     */
    private boolean isFilterable() {
        return rawLink instanceof Internallink;
    }

    boolean contentIsPublished() {
        if (isFilterable()) {
            return ((Internallink) raw()).getIsPublished();
        }
        return true;
    }
}
