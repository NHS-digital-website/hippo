package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.onehippo.cms7.essentials.components.*;
import org.slf4j.*;
import uk.nhs.digital.common.components.info.*;

import java.util.*;

@ParametersInfo(
    type = LatestNewsComponentInfo.class
)
public class LatestNewsComponent extends EssentialsNewsComponent {

    private static Logger log = LoggerFactory.getLogger(LatestNewsComponent.class);

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) {
        super.contributeAndFilters(filters, request, query);
        //filtering on 'Display on news hub and homepage' property, if it has been selected
        try {
            Filter filter = query.createFilter();
            filter.addEqualTo("website:display", true);
            filters.add(filter);
        } catch (FilterException var7) {
            log.error("An exception occurred while trying to create a query filter showing document with display field on : {}", var7);
        }
    }
}
