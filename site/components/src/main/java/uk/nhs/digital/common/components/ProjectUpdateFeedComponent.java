package uk.nhs.digital.common.components;

import static java.util.function.Function.*;
import static org.apache.commons.collections.IteratorUtils.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.ProjectUpdateFeedComponentInfo;
import uk.nhs.digital.common.util.DocumentUtils;
import uk.nhs.digital.website.beans.CommonFieldsBean;
import uk.nhs.digital.website.beans.Organisation;
import uk.nhs.digital.website.beans.ProjectUpdate;

import java.util.*;
import java.util.stream.Collectors;

@ParametersInfo(
    type = ProjectUpdateFeedComponentInfo.class
)
public class ProjectUpdateFeedComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory
        .getLogger(ProjectUpdateFeedComponent.class);
    private static final String QUERY_PARAM_YEAR = "year";
    private static final String QUERY_PARAM_UPDATE_TYPE = "updateType";
    private static final String QUERY_PARAM_ORGANISATION = "organisation";

    @Override
    public void doBeforeRender(final HstRequest request,
        final HstResponse response) {
        super.doBeforeRender(request, response);
        final HippoBean document = request.getRequestContext().getContentBean();

        if (document != null) {
            request.setAttribute("document", document);

            try {
                HippoBean parentBean = document.getParentBean();
                if (parentBean != null) {
                    List<ProjectUpdate> unfilteredResults = toList(
                        HstQueryBuilder.create(parentBean)
                            .ofTypes(ProjectUpdate.class).build().execute()
                            .getHippoBeans());
                    request.setAttribute("organisations", organisations(unfilteredResults));
                    request.setAttribute("updateTypes", types(unfilteredResults));
                    request.setAttribute("years", years(unfilteredResults));
                }
            } catch (QueryException e) {
                log.error("Query for filter generation failed: {}", e.getMessage());
            }
        }
        request.setAttribute("selectedOrganisation", getSelectedOrganisationTitle(request));
        request.setAttribute("selectedUpdateType", getSelectedUpdateType(request));
        request.setAttribute("selectedYear", getYearToFilter(request));
    }

    @Override
    protected Filter createQueryFilter(final HstRequest request,
        final HstQuery query) throws FilterException {
        final String queryParam = getSearchQuery(request);
        if (queryParam == null) {
            return null;
        }

        Filter queryFilter = query.createFilter();
        queryFilter
            .addContains(".", ComponentUtils.parseAndApplyWildcards(queryParam));
        return queryFilter;
    }

    @Override
    protected void contributeAndFilters(List<BaseFilter> filters,
        HstRequest request, HstQuery query) throws FilterException {
        super.contributeAndFilters(filters, request, query);
        addYearFilter(filters, getYearToFilter(request), query);
        addUpdateTypeFilter(filters, getSelectedUpdateType(request), query);
        addOrganisationFilter(filters, request, query);
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

    private Map<String, Long> years(List<ProjectUpdate> results) {
        return results.stream()
            .filter(ProjectUpdate::isPubliclyAccessible)
            .map(this::getYear)
            .filter(Objects::nonNull)
            .map(String::valueOf)
            .collect(Collectors.groupingBy(identity(), Collectors.counting()));
    }

    private Map<String, Long> organisations(List<ProjectUpdate> results) {
        return results.stream()
            .filter(ProjectUpdate::isPubliclyAccessible)
            .map(ProjectUpdate::getOrganisation)
            .filter(Objects::nonNull)
            .map(CommonFieldsBean::getTitle)
            .collect(Collectors.groupingBy(identity(), Collectors.counting()));
    }

    private Map<String, Long> types(List<ProjectUpdate> results) {
        return results.stream()
            .filter(ProjectUpdate::isPubliclyAccessible)
            .map(ProjectUpdate::getTypeOfUpdate)
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(identity(), Collectors.counting()));
    }

    private Integer getYear(final ProjectUpdate document) {
        return document.getUpdateTimestamp() != null ? document.getUpdateTimestamp()
            .get(Calendar.YEAR) : null;
    }

    private String getSelectedYear(final HstRequest request) {
        return getPublicRequestParameter(request, QUERY_PARAM_YEAR);
    }

    private String getSelectedUpdateType(final HstRequest request) {
        return getPublicRequestParameter(request, QUERY_PARAM_UPDATE_TYPE);
    }

    private String getSelectedOrganisationTitle(final HstRequest request) {
        return getPublicRequestParameter(request, QUERY_PARAM_ORGANISATION);
    }

    private String getYearToFilter(final HstRequest request) {
        return DocumentUtils.findYearOrDefault(getSelectedYear(request),
            Calendar.getInstance().get(Calendar.YEAR));
    }

    private void addYearFilter(List<BaseFilter> filters, String year,
        HstQuery query) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        Filter filter = query.createFilter();
        try {
            filter.addEqualTo("website:updatetimestamp", calendar,
                DateTools.Resolution.YEAR);
            filters.add(filter);
        } catch (final FilterException exception) {
            log.error("Error trying to add year filter", exception);
        }
    }

    private void addUpdateTypeFilter(List<BaseFilter> filters, String updateType,
        HstQuery query) {
        if (StringUtils.isNotBlank(updateType)) {
            Filter filter = query.createFilter();
            try {
                filter.addEqualTo("website:typeofupdate", updateType);
                filters.add(filter);
            } catch (final FilterException exception) {
                log.error("Error trying to add type of update filter", exception);
            }
        }
    }

    private void addOrganisationFilter(List<BaseFilter> filters, HstRequest request,
        HstQuery query) {
        String organisationTitle = getSelectedOrganisationTitle(request);
        if (StringUtils.isNotBlank(organisationTitle)) {
            HippoBeanIterator organisationBeans;
            try {
                final HstRequestContext requestContext = request
                    .getRequestContext();

                HstQuery orgQuery = requestContext.getQueryManager().createQuery(
                    requestContext.getSiteContentBaseBean(),
                    Organisation.class);
                Filter orgNameFilter = orgQuery.createFilter();
                orgNameFilter.addEqualToCaseInsensitive("website:title", organisationTitle);
                orgQuery.setFilter(orgNameFilter);
                organisationBeans = orgQuery.execute().getHippoBeans();
            } catch (QueryException e) {
                log.debug("Error finding organisation with title {}: {}",
                    organisationTitle, e);
                return;
            }

            if (organisationBeans.getSize() == 0) {
                return;
            }
            String jcrQuery;

            if (organisationBeans.getSize() > 1) {
                StringBuilder jcrQueryBuilder = new StringBuilder(String
                    .format("((website:organisation/@hippo:docbase = '%s')",
                        ((HippoDocument)organisationBeans.nextHippoBean()).getCanonicalHandleUUID()));
                while (organisationBeans.hasNext()) {
                    jcrQueryBuilder.append(String
                        .format(" or (website:organisation/@hippo:docbase = '%s')",
                            ((HippoDocument)organisationBeans.nextHippoBean()).getCanonicalHandleUUID()));
                }
                jcrQueryBuilder.append(")");
                jcrQuery = jcrQueryBuilder.toString();
            } else {
                jcrQuery = String
                    .format("(website:organisation/@hippo:docbase = '%s')",
                        ((HippoDocument)organisationBeans.nextHippoBean()).getCanonicalHandleUUID());
            }
            Filter filter = query.createFilter();
            filter.addJCRExpression(jcrQuery);
            filters.add(filter);
        }
    }
}
