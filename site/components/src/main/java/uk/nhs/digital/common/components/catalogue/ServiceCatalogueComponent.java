package uk.nhs.digital.common.components.catalogue;

import static java.util.stream.Collectors.toList;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.util.*;

public class ServiceCatalogueComponent extends CatalogueComponent {

    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogueComponent.class);
    private static final String TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH = "/content/documents/administration/website/service-catalogue/taxonomy-filters-mapping";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final List<CatalogueLink> allCatalogueLinks = catalogueLinksFrom(request);

        final Set<String> userSelectedFilterKeys = userSelectedFilterKeysFrom(request);

        final List<CatalogueLink> catalogueLinksFiltered = applyUserSelectedFilters(
            allCatalogueLinks,
            userSelectedFilterKeys
        );

        final Filters filtersModel = filtersModel(
            allCatalogueLinks,
            userSelectedFilterKeys,
            sessionFrom(request),
            TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH,
            log
        );

        request.setAttribute(
            Param.catalogueLinks.name(),
            catalogueLinksFiltered.stream().map(CatalogueLink::raw).collect(toList())
        );
        request.setAttribute(Param.filtersModel.name(), filtersModel);
    }

    enum Param {
        catalogueLinks,
        filtersModel,
        filter
    }
}
