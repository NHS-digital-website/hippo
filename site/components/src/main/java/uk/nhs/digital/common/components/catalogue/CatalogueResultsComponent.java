package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoResultSetBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.DefaultPagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.info.CatalogueResultsComponentInfo;

import java.util.*;
import java.util.stream.Collectors;

@ParametersInfo(
    type = CatalogueResultsComponentInfo.class
)
public class CatalogueResultsComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory.getLogger(CatalogueResultsComponent.class);
    // public static final String TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH = "/content/documents/administration/website/developer-hub/taxonomy-filters-mapping";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("Start Time:" + startTime);

        super.doBeforeRender(request, response);

        CatalogueResultsComponentInfo parameterInfo = this.getComponentParametersInfo(request);
        CatalogueFilterManager catalogueFilterManager = new CatalogueFilterManager(parameterInfo.getTaxonomyFilterMappingDocumentPath());

        List<Section> sections = catalogueFilterManager.getRawFilters(request).getSections();

        String statusGroupName = parameterInfo.getTaxonomyStatusGroupName();

        Optional<Section> status = sections.stream()
            .filter(section -> statusGroupName.equals(section.getDisplayName()))
            .findFirst();

        status.ifPresent(section -> {
            Map<String, Section> sectionMap = section.getEntries().stream()
                .collect(Collectors.toMap(
                    Section::getTaxonomyKey,
                    entry -> entry
                ));
            request.setAttribute("statusEntries", sectionMap);
        });

        List<Section> nonStatusSections = sections.stream()
            .filter(section -> !statusGroupName.equals(section.getDisplayName()))
            .collect(Collectors.toList());

        request.setAttribute("nonStatusSections", nonStatusSections);

        request.setAttribute("requestContext", request.getRequestContext());
        request.setAttribute("currentQuery", Optional.ofNullable(getAnyParameter(request, "query")).orElse(""));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("End of method: doBeforeRender in ApiCatalogueHubComponent  at " + endTime + " ms. Duration: " + duration + " ms");
    }

    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> doFacetedSearch(HstRequest request, T paramInfo, HippoBean scope) {

        Pageable<HippoBean> pageable = DefaultPagination.emptyCollection();
        String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        HippoFacetNavigationBean facetBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));
        if (facetBean != null) {
            HippoResultSetBean resultSet = facetBean.getResultSet();
            if (resultSet != null) {
                HippoDocumentIterator<HippoBean> iterator = resultSet.getDocumentIterator(HippoBean.class);
                pageable = this.getPageableFactory().createPageable(iterator, facetBean.getResultSet().getDocumentSize(), paramInfo.getPageSize(), this.getCurrentPage(request));
                request.setAttribute("totalAvailable", facetBean.getResultSet().getDocumentSize());
            }
        }
        return pageable;
    }

}
