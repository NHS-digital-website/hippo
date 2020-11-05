package uk.nhs.digital.common.components;

import org.apache.commons.lang.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.onehippo.cms7.essentials.components.*;
import org.onehippo.cms7.essentials.components.info.*;
import org.slf4j.*;
import uk.nhs.digital.common.components.info.*;

import java.util.*;

@ParametersInfo(
    type = LatestNewsComponentInfo.class
)
public class LatestNewsComponent extends EssentialsNewsComponent {

    private static final Logger log = LoggerFactory.getLogger(LatestNewsComponent.class);

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        super.contributeAndFilters(filters, request, query);
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

    @Override
    protected Filter createQueryFilter(final HstRequest request, final HstQuery query) throws FilterException {

        Filter queryFilter = null;

        final String queryParam = getSearchQuery(request);
        if (queryParam != null) {
            String querystring = ComponentUtils.parseAndApplyWildcards(queryParam);

            queryFilter = query.createFilter();
            queryFilter.addContains(".", querystring);
        }
        return queryFilter;
    }

    @Override
    protected int getPageSize(final HstRequest request, final EssentialsPageable paramInfo) {
        //getting the componentPageSize parameter value if defined in the component
        String compononentPageSize = StringUtils.defaultIfEmpty(getComponentParameter("defaultPageSize"), "");
        //if the componentPageSize hasn't been defined, then use the component param info
        return NumberUtils.isCreatable(compononentPageSize)
            ? Integer.parseInt(compononentPageSize) : super.getPageSize(request, paramInfo);
    }
}
