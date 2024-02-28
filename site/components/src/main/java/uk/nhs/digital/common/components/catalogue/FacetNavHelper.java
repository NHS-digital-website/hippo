package uk.nhs.digital.common.components.catalogue;

import java.util.List;
import java.util.Set;

public interface FacetNavHelper {
    Set<String> getAllTags();

    List<String> getAllTagsForLink(CatalogueLink link);
}
