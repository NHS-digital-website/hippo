package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsPageable;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.ProjectUpdateFeedComponentInfo;
import uk.nhs.digital.common.util.DocumentUtils;
import uk.nhs.digital.website.beans.ProjectUpdate;

import java.util.*;
import java.util.stream.Collectors;

@ParametersInfo(
    type = ProjectUpdateFeedComponentInfo.class
)
public class ProjectUpdateFeedComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory
        .getLogger(ProjectUpdateFeedComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        final HippoBean document = request.getRequestContext().getContentBean();

        if (document != null) {
            request.setAttribute("document", document);
        }

        request.setAttribute("years", years(request));

        request.setAttribute("selectedYear", getYearToFilter(request));
    }

    @Override
    protected Filter createQueryFilter(final HstRequest request, final HstQuery query) throws FilterException {
        final String queryParam = getSearchQuery(request);
        if (queryParam == null) {
            return null;
        }

        Filter queryFilter = query.createFilter();
        queryFilter.addContains(".", ComponentUtils.parseAndApplyWildcards(queryParam));
        return queryFilter;
    }

    @Override
    protected void contributeAndFilters(List<BaseFilter> filters,
        HstRequest request, HstQuery query) throws FilterException {
        super.contributeAndFilters(filters, request, query);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(getYearToFilter(request)));
        Filter filter = query.createFilter();
        try {
            filter.addEqualTo("website:updatetimestamp", calendar,
                DateTools.Resolution.YEAR);
            filters.add(filter);
        } catch (final FilterException exception) {
            log.error("Error trying to add year filter", exception);
        }
    }

    @Override
    protected int getPageSize(final HstRequest request,
        final EssentialsPageable paramInfo) {
        String componentPageSize = StringUtils
            .defaultIfEmpty(getComponentParameter("defaultPageSize"), "");
        return NumberUtils.isCreatable(componentPageSize) ? Integer
            .parseInt(componentPageSize) : super.getPageSize(request, paramInfo);
    }

    @Override
    protected HippoBean getSearchScope(HstRequest request, String path) {
        return RequestContextProvider.get().getContentBean().getParentBean();
    }

    private List<String> years(HstRequest request) {
        Pageable<HippoBean> pageable = request.getModel(REQUEST_ATTR_PAGEABLE);
        if (pageable == null) {
            return Collections.emptyList();
        }

        return pageable.getItems().parallelStream()
            .map(document -> getYear((ProjectUpdate) document))
            .filter(Objects::nonNull)
            .distinct()
            .map(String::valueOf)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
    }

    private Integer getYear(final ProjectUpdate document) {
        return document.getUpdateTimestamp() != null ? document.getUpdateTimestamp()
            .get(Calendar.YEAR) : null;
    }

    private String getSelectedYear(final HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }

    private String getYearToFilter(final HstRequest request) {
        return DocumentUtils.findYearOrDefault(getSelectedYear(request),
            Calendar.getInstance().get(Calendar.YEAR));
    }
}
