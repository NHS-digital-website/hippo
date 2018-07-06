package uk.nhs.digital.common.components;

import org.apache.commons.lang3.*;
import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.hippoecm.repository.util.*;
import org.slf4j.*;
import uk.nhs.digital.common.components.info.*;

import java.util.*;

@ParametersInfo(
    type = EventsComponentInfo.class
)
public class FilterableEventsComponent extends EventsComponent {

    private static Logger log = LoggerFactory.getLogger(FilterableEventsComponent.class);

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        super.contributeAndFilters(filters, request, query);

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
     * Fetch the value of month parameter from the URL query string
     *
     * @param request containing the month parameter
     * @return the value of the first month parameter if exists, otherwise empty
     */
    protected String getSelectedMonth(HstRequest request) {
        return getPublicRequestParameter(request, "month");
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

    @Override
    protected void addIntervalConstraint(final List filters, final HstQuery hstQuery, final String dateField, final HstRequest request) throws FilterException {
        //in case of filterable latest event, add a constraint based on the selected month
        String selectedMonth = getSelectedMonth(request);
        //check if the month has been specified in the query
        if (StringUtils.isNotEmpty(selectedMonth)) {
            final Filter filter = hstQuery.createFilter();
            Calendar month = Calendar.getInstance();
            month.set(Calendar.DAY_OF_MONTH, 1);
            try {
                //calculating the current month according to the request parameter
                int monthNumber = Integer.parseInt(selectedMonth) - 1;
                month.set(Calendar.MONTH, monthNumber);
                //resolution is by MONTH, no needs to calculate the end date
                filter.addBetween(dateField, month, month, DateTools.Resolution.MONTH);
            } catch (FilterException filterException) {
                log.warn("Errors while adding month interval filter {}", filterException);
            } catch (NumberFormatException formatException) {
                log.warn("Errors while parsing month parameter {}", formatException);
            }
            filters.add(filter);
        } else {
            super.addIntervalConstraint(filters, hstQuery, dateField, request);
        }
    }
}
