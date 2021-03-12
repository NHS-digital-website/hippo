package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.onehippo.cms7.essentials.components.*;
import org.slf4j.*;
import uk.nhs.digital.common.components.info.PublicationListComponentInfo;

import java.util.*;

@ParametersInfo(
    type = PublicationListComponentInfo.class
)
public class PublicationListComponent extends EssentialsListComponent {

    private static Logger log = LoggerFactory.getLogger(PublicationListComponent.class);

    @Override
    protected void contributeAndFilters(final List<BaseFilter> filters, final HstRequest request, final HstQuery query) throws FilterException {
        super.contributeAndFilters(filters, request, query);

        final PublicationListComponentInfo componentParametersInfo = getComponentParametersInfo(request);

        request.setAttribute("size", componentParametersInfo.getPageSize());
        request.setAttribute("viewAllUrl", componentParametersInfo.getViewAllUrl());
        request.setAttribute("viewUpcomingUrl", componentParametersInfo.getViewUpcomingUrl());

        Filter filter = query.createFilter();
        query.setFilter(filter);
        try {
            filter.addEqualTo("publicationsystem:PubliclyAccessible", true);
        } catch (FilterException ex) {
            log.error("Errors while adding PubliclyAccessible filter", ex);
        }
    }
}
