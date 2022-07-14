package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsEventsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.info.EssentialsPageable;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.EventsComponentInfo;
import uk.nhs.digital.common.util.DocumentUtils;

import java.util.*;

@ParametersInfo(
    type = EventsComponentInfo.class
)
public class EventsComponent extends EssentialsEventsComponent {

    private static Logger log = LoggerFactory.getLogger(EventsComponent.class);
    private ComponentHelper componentHelper = new ComponentHelper();

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        super.doBeforeRender(request, response);

        //sending the eventstype values
        final ValueList eventsTypeValueList =
            SelectionUtil.getValueListByIdentifier("eventstype", RequestContextProvider.get());
        if (eventsTypeValueList != null) {
            request.setAttribute("eventstype", SelectionUtil.valueListAsMap(eventsTypeValueList));
        }

        String[] selectedTypes = getPublicRequestParameters(request, "type");
        request.setAttribute("selectedTypes", Arrays.asList(selectedTypes));
        request.setAttribute("selectedYear", DocumentUtils.findYearOrDefault(getSelectedYear(request), Calendar.getInstance().get(Calendar.YEAR)));
        request.setAttribute("years", componentHelper.years());
    }

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {

        // filter's documents such that the website:display is set to true
        try {
            Filter filter = query.createFilter();
            filter.addEqualTo("website:display", true);
            filters.add(filter);
        } catch (FilterException var7) {
            log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7);
        }

        //fetching the selected types from the request
        String[] selectedTypes = getSelectedTypes(request);
        if (selectedTypes.length > 0) {
            final Filter filter = query.createFilter();
            for (String type : selectedTypes) {
                try {
                    filter.addEqualTo("@website:type", type);
                } catch (FilterException filterException) {
                    log.warn("Errors while adding event type filter {}", filterException);
                }
            }
            filters.add(filter);
        }
    }

    /**
     * Fetch the values of type parameters from the URL query string
     *
     * @param request containing the type parameters
     * @return array of type parameters if at least one exists, otherwise empty
     */
    protected String[] getSelectedTypes(HstRequest request) {
        return getPublicRequestParameters(request, "type");
    }



    /**
     * Fetch the value of year parameter from the URL query string
     *
     * @param request containing the year parameter
     * @return the value of the first year parameter if exists, otherwise empty
     */
    protected String getSelectedYear(HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }

    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> executeQuery(HstRequest request, T paramInfo, HstQuery query) throws QueryException {
        int pageSize = this.getPageSize(request, paramInfo);
        int page = this.getCurrentPage(request);
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);
        this.applyExcludeScopes(request, query, paramInfo);
        this.buildAndApplyFilters(request, query);

        return ComponentHelper.executeQuery(request, paramInfo, query, page, pageSize, this);
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        //getting the componentPageSize parameter value if defined in the component
        String compononentPageSize = StringUtils.defaultIfEmpty(getComponentLocalParameter("defaultPageSize"), "");
        //if the componentPageSize hasn't been defined, then use the component param info
        return NumberUtils.isCreatable(compononentPageSize)
            ? Integer.parseInt(compononentPageSize) : super.getPageSize(request, paramInfo);
    }

    @Override
    public EventsComponentInfo getComponentParametersInfo(HstRequest request) {
        EventsComponentInfo ret = super.getComponentParametersInfo(request);
        return ret;
    }

    @Override
    public Filter createQueryFilter(HstRequest request, HstQuery hstQuery) throws FilterException {
        return super.createQueryFilter(request, hstQuery);
    }

    @Override
    public void applyAndFilters(HstQuery hstQuery, List<BaseFilter> filters) throws FilterException {
        super.applyAndFilters(hstQuery, filters);
    }
}
