package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.CyberAlertsHubComponentInfo;
import uk.nhs.digital.website.beans.CyberAlert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ParametersInfo(
    type = CyberAlertsHubComponentInfo.class
)
public class CyberAlertsHubComponent extends EssentialsListComponent {

    private static Logger log = LoggerFactory.getLogger(CyberAlertsHubComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        String[] selectedTypes = getPublicRequestParameters(request, "threattype");
        request.setAttribute("selectedThreattype", Arrays.asList(selectedTypes));
        String[] selectedSeverity = getPublicRequestParameters(request, "severity");
        request.setAttribute("selectedSeverity", Arrays.asList(selectedSeverity));
        request.setAttribute("selectedYear", findYearOrDefault(getSelectedYear(request) , Calendar.getInstance().get(Calendar.YEAR)));
        request.setAttribute("years", years());
    }

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) throws FilterException {
        super.contributeAndFilters(filters, request, query);

        String[] selectedTypes = getSelectedType(request);
        if (selectedTypes.length > 0) {
            final Filter filter = query.createFilter();
            for (String type : selectedTypes) {
                try {
                    filter.addEqualTo("@website:threattype", type);
                } catch (FilterException filterException) {
                    log.warn("Errors while adding cyber type filter {}", filterException);
                }
            }
            filters.add(filter);
        }

        String[] selectedSeverity = getSelectedSeverity(request);
        if (selectedSeverity.length > 0) {
            final Filter filter = query.createFilter();
            for (String severity : selectedSeverity) {
                try {
                    filter.addEqualTo("@website:severity", severity);
                } catch (FilterException filterException) {
                    log.warn("Errors while adding cyber severity filter {}", filterException);
                }
            }
            filters.add(filter);
        }

        Calendar calendar = Calendar.getInstance();
        int year = Integer.valueOf(findYearOrDefault(getSelectedYear(request) , calendar.get(Calendar.YEAR)));
        final Filter filter = query.createFilter();
        calendar.set(Calendar.YEAR, year);
        filter.addBetween("@publicationsystem:NominalDate", calendar, calendar, DateTools.Resolution.YEAR);
        filters.add(filter);

    }

    private String findYearOrDefault(String target, int fallback) {
        if (isYear(target)) {
            return target;
        }
        return String.valueOf(fallback);
    }

    private boolean isYear(String candidate) {
        return candidate != null && candidate.matches("20[1-9][0-9]");
    }

    private Map<String, Long> years() {
        try {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(yearsQuery(), Spliterator.ORDERED), false)
                .filter(ca -> ((CyberAlert) ca).getPublicallyAccessible())
                .map(ca -> String.valueOf(((CyberAlert) ca).getPublishedDate().get(Calendar.YEAR)))
                .collect(Collectors.groupingBy(
                    Function.identity(), Collectors.counting()
                ));
        } catch (QueryException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private HippoBeanIterator yearsQuery() throws QueryException {
        return HstQueryBuilder.create(RequestContextProvider.get().getSiteContentBaseBean())
            .ofTypes(CyberAlert.class)
            .build()
            .execute()
            .getHippoBeans();
    }

    protected String getSelectedYear(HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }

    protected String[] getSelectedType(HstRequest request) {
        return getPublicRequestParameters(request, "threattype");
    }

    protected String[] getSelectedSeverity(HstRequest request) {
        return getPublicRequestParameters(request, "severity");
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        //getting the componentPageSize parameter value if defined in the component
        String compononentPageSize = StringUtils.defaultIfEmpty(getComponentLocalParameter("defaultPageSize"), "");
        //if the componentPageSize hasn't been defined, then use the component param info
        return NumberUtils.isCreatable(compononentPageSize)
            ? Integer.parseInt(compononentPageSize) : super.getPageSize(request, paramInfo);
    }

}
