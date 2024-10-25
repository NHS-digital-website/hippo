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

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {

        long startTime = System.currentTimeMillis();
        log.debug("Start Time:" + startTime);

        super.doBeforeRender(request, response);

        CatalogueResultsComponentInfo parameterInfo = this.getComponentParametersInfo(request);
        CatalogueFilterManager catalogueFilterManager = new CatalogueFilterManager(parameterInfo.getTaxonomyFilterMappingDocumentPath());

        List<Section> sections = catalogueFilterManager.getRawFilters(request).getSections();

        Map<String, Section> sectionEntries = sections.stream()
            .flatMap(section -> section.getEntriesAndChildEntries().stream())
            .collect(Collectors.toMap(
                Section::getTaxonomyKey,
                entry -> entry,
                (existing, replacement) -> existing  // Keep the existing entry in case of duplicate keys, but
            ));

        request.setAttribute("sectionEntries", sectionEntries);

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
