package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.utils.ComponentsUtils;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.FeedListComponentInfo;
import uk.nhs.digital.common.util.DocumentUtils;

import java.util.*;

// tell cpd to start ignoring code - CPD-OFF
@ParametersInfo(
    type = FeedListComponentInfo.class
)
public class FeedListComponent extends EssentialsListComponent {

    private static final Logger log = LoggerFactory.getLogger(FeedListComponent.class);
    private ComponentHelper componentHelper = new ComponentHelper();

    /**
     * Fetch the values of type parameters from the URL query string
     *
     * @param request containing the type parameters
     * @return array of type parameters if at least one exists, otherwise empty
     */
    protected String[] getSelectedTypes(HstRequest request) {
        return getPublicRequestParameters(request, "type");
    }

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        FeedListComponentInfo info = getComponentParametersInfo(request);
        // Filter logic from individual List components

        // Filter for News
        if (info.getDocumentTypes().equals("website:news")) {
            if (info.getHideFutureItems()) {
                final String documentDateField = info.getDocumentDateField();
                if (!Strings.isNullOrEmpty(documentDateField)) {
                    try {
                        Filter filter = query.createFilter();
                        filter.addLessOrEqualThan(documentDateField, Calendar.getInstance(), DateTools.Resolution.DAY);
                        filters.add(filter);
                    } catch (FilterException e) {
                        log.error("An exception occurred while trying to create a query filter for hiding future items: {}", e, e);
                    }
                }
            }

            query.addOrderByDescending("website:publisheddatetime");
            //filtering on 'Display on news hub and homepage' property, if it has been selected
            try {
                Filter filter = query.createFilter();
                filter.addEqualTo("website:display", true);
                filters.add(filter);
            } catch (FilterException var7) {
                // Exception is entered twice - once as a parameter for the string, and once as the actual exception thrown
                // see: http://www.slf4j.org/faq.html#paramException
                log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7, var7);
            }
        }

        // Filter for Blogs
        if (info.getDocumentTypes().equals("website:blog")) {
            query.addOrderByDescending("website:dateofpublication");
        }

        // Filter for Events
        if (info.getDocumentTypes().equals("website:event")) {
            // filter's documents such that the website:display is set to true
            try {
                Filter filter = query.createFilter();
                filter.addEqualTo("website:display", true);
                filters.add(filter);
            } catch (FilterException var7) {
                log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7, var7);
            }

            //fetching the selected types from the request
            String[] selectedTypes = getSelectedTypes(request);
            if (selectedTypes.length > 0) {
                final Filter filter = query.createFilter();
                for (String type : selectedTypes) {
                    try {
                        filter.addEqualTo("@website:type", type);
                    } catch (FilterException filterException) {
                        log.warn("Errors while adding event type filter {}", filterException, filterException);
                    }
                }
                filters.add(filter);
            }
        }
    }

    /**
     * Fetch the value of year parameter from the URL query string
     * <p>
     * Copied from uk.nhs.digital.common.components.EventsComponent
     *
     * @param request containing the year parameter
     * @return the value of the first year parameter if exists, otherwise empty
     */
    protected String getSelectedYear(HstRequest request) {
        return getPublicRequestParameter(request, "year");
    }


    /**
     * Copied from uk.nhs.digital.common.components.EventsComponent
     * Added a check for the "website:event" doctype.
     * Runs super if not,
     * Super from org.onehippo.cms7.essentials.components.EssentialsListComponent;
     */
    @Override
    protected <T extends EssentialsListComponentInfo> Pageable<HippoBean> executeQuery(HstRequest request, T paramInfo, HstQuery query) throws QueryException {
        final FeedListComponentInfo info = getComponentParametersInfo(request);
        final String documentTypes = info.getDocumentTypes();

        int pageSize = this.getPageSize(request, paramInfo);
        int page = this.getCurrentPage(request);
        query.setLimit(pageSize);
        query.setOffset((page - 1) * pageSize);
        this.applyExcludeScopes(request, query, paramInfo);
        this.buildAndApplyFilters(request, query);

        if (documentTypes.equals("website:event")) {
            return componentHelper.executeQuery(request, paramInfo, query, page, pageSize,this);
        } else {
            return super.executeQuery(request, paramInfo, query);
        }
    }

    @Override
    public void doBeforeRender(HstRequest request,
                               HstResponse response) throws HstComponentException {

        FeedListComponentInfo info = getComponentParametersInfo(request);
        final String documentTypes = info.getDocumentTypes();
        ComponentsUtils.addCurrentDateStrings(request);
        if (Strings.isNullOrEmpty(documentTypes)) {
            setEditMode(request);
            return;
        }

        super.doBeforeRender(request, response);

        if (info.getDocumentTypes().equals("website:event")) {
            //sending the eventstype values
            final ValueList eventsTypeValueList =
                SelectionUtil.getValueListByIdentifier("eventstype", RequestContextProvider.get());
            if (eventsTypeValueList != null) {
                request.setAttribute("eventstype", SelectionUtil.valueListAsMap(eventsTypeValueList));
            }

            String[] selectedTypes = getSelectedTypes(request);
            request.setAttribute("selectedTypes", Arrays.asList(selectedTypes));
            request.setAttribute("selectedYear", DocumentUtils.findYearOrDefault(getSelectedYear(request), Calendar.getInstance().get(Calendar.YEAR)));
            request.setAttribute("years", componentHelper.years());
        }

        String titleText = info.getTitleText();
        String buttonText = info.getButtonText();
        String buttonDestination = info.getButtonDestination();
        String secondaryButtonText = info.getSecondaryButtonText();
        String secondaryButtonDestination = info.getSecondaryButtonDestination();

        request.setAttribute("titleText", titleText);
        request.setAttribute("buttonText", buttonText);
        request.setAttribute("buttonDestination", buttonDestination);
        request.setAttribute("secondaryButtonText", secondaryButtonText);
        request.setAttribute("secondaryButtonDestination", secondaryButtonDestination);
    }

    @Override
    public FeedListComponentInfo getComponentParametersInfo(HstRequest request) {
        FeedListComponentInfo ret = super.getComponentParametersInfo(request);
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
// resume CPD analysis - CPD-ON
