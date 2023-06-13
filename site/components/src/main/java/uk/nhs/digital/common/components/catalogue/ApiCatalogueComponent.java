package uk.nhs.digital.common.components.catalogue;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableSet;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.util.*;

public class ApiCatalogueComponent extends CatalogueComponent {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueComponent.class);

    private static final Set<String> RETIRED_API_FILTER_KEYS = ImmutableSet.of("retired-api");
    private static final String TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final List<CatalogueLink> allCatalogueLinks = catalogueLinksFrom(request);

        final boolean showRetired = shouldShowRetired(request);

        final List<CatalogueLink> catalogueLinksExcludingRetiredIfNeeded =
            eliminateRetiredIfNeeded(allCatalogueLinks, showRetired);

        final Set<String> userSelectedFilterKeys = userSelectedFilterKeysFrom(request);

        final List<CatalogueLink> catalogueLinksFiltered = applyUserSelectedFilters(
            catalogueLinksExcludingRetiredIfNeeded,
            userSelectedFilterKeys
        );

        final Filters filtersModel = filtersModel(
            catalogueLinksExcludingRetiredIfNeeded,
            userSelectedFilterKeys,
            sessionFrom(request),
            TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH,
            log
        );

        request.setAttribute(Param.showRetired.name(), showRetired);
        request.setAttribute(Param.hasRetired.name(), true);
        request.setAttribute(Param.catalogueLinks.name(), catalogueLinksFiltered.stream().map(CatalogueLink::raw).collect(toList()));
        request.setAttribute(Param.filtersModel.name(), filtersModel);
    }

    private boolean queryStringContainsParameter(final HstRequest request, final Param queryStringParameter) {
        return Optional.ofNullable(request.getQueryString())
            .filter(queryString -> queryString.contains(queryStringParameter.name()))
            .isPresent();
    }

    private boolean shouldShowRetired(final HstRequest request) {
        return queryStringContainsParameter(request, Param.showDeprecatedAndRetired)
            || queryStringContainsParameter(request, Param.showRetired);
    }

    private List<CatalogueLink> eliminateRetiredIfNeeded(
        final List<CatalogueLink> catalogueLinks,
        final boolean showRetired
    ) {
        return catalogueLinks.stream()
            .filter(link -> showRetired || link.notFilterable() || link.notTaggedWithAnyOf(RETIRED_API_FILTER_KEYS))
            .collect(toList());
    }

    enum Param {
        showRetired,
        hasRetired,
        catalogueLinks,
        filtersModel,
        filter,

        // Older parameter, deprecated in favour of showRetired,
        // retained in case it's been included in existing bookmarks.
        showDeprecatedAndRetired
    }
}