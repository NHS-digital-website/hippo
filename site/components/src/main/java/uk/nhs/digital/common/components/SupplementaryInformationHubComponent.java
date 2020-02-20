package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.SupplementaryInformationHubComponentInfo;
import uk.nhs.digital.common.util.DocumentUtils;
import uk.nhs.digital.website.beans.SupplementaryInformation;
import uk.nhs.digital.website.beans.SupplementaryInformationHub;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ParametersInfo(
    type = SupplementaryInformationHubComponentInfo.class
)
public class SupplementaryInformationHubComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory.getLogger(SupplementaryInformationHubComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        final HstRequestContext context = request.getRequestContext();
        final SupplementaryInformationHub document = (SupplementaryInformationHub) context.getContentBean();

        if (document != null) {
            request.setAttribute("document", document);
        }

        request.setAttribute("years", years());
        request.setAttribute("selectedYear", DocumentUtils.findYearOrDefault(getSelectedYear(request), Calendar.getInstance().get(Calendar.YEAR)));
    }

    @Override
    protected Filter createQueryFilter(final HstRequest request, final HstQuery query) throws FilterException {

        Filter queryFilter = null;
        final String queryParam = getSearchQuery(request);
        if (queryParam != null) {
            final String querystring = ComponentUtils.parseAndApplyWildcards(queryParam);
            queryFilter = query.createFilter();
            queryFilter.addContains(".", querystring);
        }
        return queryFilter;
    }

    @Override
    protected void contributeAndFilters(List<BaseFilter> filters, HstRequest request, HstQuery query) throws FilterException {
        super.contributeAndFilters(filters, request, query);
        final String selectedYear = DocumentUtils.findYearOrDefault(getSelectedYear(request), Calendar.getInstance().get(Calendar.YEAR));
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(selectedYear));
        Filter filter = query.createFilter();
        try {
            filter.addEqualTo("publicationsystem:NominalDate", calendar, DateTools.Resolution.YEAR);
            filters.add(filter);
        } catch (final FilterException exception) {
            log.error("Error trying to add year filter", exception);
        }
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        String componentPageSize = StringUtils.defaultIfEmpty(getComponentParameter("defaultPageSize"), "");
        return NumberUtils.isCreatable(componentPageSize)
            ? Integer.parseInt(componentPageSize) : super.getPageSize(request, paramInfo);
    }

    private List<String> years() {
        try {
            return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(DocumentUtils.documentsQuery(SupplementaryInformation.class), Spliterator.ORDERED), true)
                .map(document -> getPublicationYear((SupplementaryInformation) document))
                .filter(Objects::nonNull)
                .distinct()
                .map(String::valueOf)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        } catch (final QueryException e) {
            log.error("Exception while querying SIF publication years", e);
        }
        return Collections.emptyList();
    }

    private Integer getPublicationYear(final SupplementaryInformation document) {
        return document.getPublishedDate() != null ? document.getPublishedDate().get(Calendar.YEAR)
            : null;
    }

    protected String getSelectedYear(final HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }
}
