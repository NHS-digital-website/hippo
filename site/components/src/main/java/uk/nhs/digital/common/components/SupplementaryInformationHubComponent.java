package uk.nhs.digital.common.components;

import static org.apache.commons.collections.IteratorUtils.toList;

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

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

        List<SupplementaryInformation> si = getListOfSupplementaryInformation();

        request.setAttribute("years", years(si));

        String selectedYear = DocumentUtils.findYearOrDefault(getSelectedYear(request), Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("selectedYear", selectedYear);

        final Month selectedMonth = findMonthOrNull(getSelectedMonth(request));
        if (Objects.nonNull(selectedMonth)) {
            request.setAttribute("selectedMonth", selectedMonth);
        }

        request.setAttribute("months", months(Integer.parseInt(selectedYear), si));
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

        // Add year filter
        final String selectedYear = DocumentUtils.findYearOrDefault(getSelectedYear(request), Calendar.getInstance().get(Calendar.YEAR));
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(selectedYear));
        Filter yearFilter = query.createFilter();
        try {
            yearFilter.addEqualTo("publicationsystem:NominalDate", calendar, DateTools.Resolution.YEAR);
            filters.add(yearFilter);
        } catch (final FilterException exception) {
            log.error("Error trying to add year filter", exception);
        }

        // Add month filter
        final Month selectedMonth = findMonthOrNull(getSelectedMonth(request));
        if (Objects.nonNull(selectedMonth)) {
            calendar.set(Calendar.MONTH, calenderMonthOrdinate(selectedMonth));
            Filter monthFilter = query.createFilter();
            try {
                monthFilter.addEqualTo("publicationsystem:NominalDate", calendar, DateTools.Resolution.MONTH);
                filters.add(monthFilter);
            } catch (final FilterException exception) {
                log.error("Error trying to add year filter", exception);
            }
        }
    }

    private int calenderMonthOrdinate(Month selectedMonth) {
        return selectedMonth.getValue() - 1;
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        String componentPageSize = StringUtils.defaultIfEmpty(getComponentParameter("defaultPageSize"), "");
        return NumberUtils.isCreatable(componentPageSize)
            ? Integer.parseInt(componentPageSize) : super.getPageSize(request, paramInfo);
    }

    private List<SupplementaryInformation> getListOfSupplementaryInformation() {
        try {
            return toList(DocumentUtils.documentsQuery(SupplementaryInformation.class));
        } catch (final QueryException e) {
            log.error("Exception while querying SIF publication documents", e);
        }
        return Collections.emptyList();
    }

    private List<String> years(List<SupplementaryInformation> si) {
        return si.stream()
            .map(this::getPublicationYear)
            .filter(Objects::nonNull)
            .distinct()
            .map(String::valueOf)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
    }

    private List<Integer> months(Integer inYear, List<SupplementaryInformation> si) {
        return si.stream()
            .filter(document -> inYear.equals(getPublicationYear(document)))
            .map(this::getPublicationMonth)
            .filter(Objects::nonNull)
            .distinct()
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.toList());
    }

    private Integer getPublicationYear(final SupplementaryInformation document) {
        return document.getPublishedDate() != null ? document.getPublishedDate().get(Calendar.YEAR)
            : null;
    }

    private Integer getPublicationMonth(final SupplementaryInformation document) {
        return document.getPublishedDate() != null ? document.getPublishedDate().get(Calendar.MONTH)
            : null;
    }

    protected String getSelectedYear(final HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }

    protected String getSelectedMonth(final HstRequest request) {
        return getPublicRequestParameter(request, "month");
    }

    private static Month findMonthOrNull(final String target) {
        return isMonth(target) ? Month.valueOf(target.toUpperCase()) : null;
    }

    private static boolean isMonth(final String candidate) {
        try {
            if (Objects.nonNull(candidate)) {
                Month.valueOf(candidate.toUpperCase());
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
