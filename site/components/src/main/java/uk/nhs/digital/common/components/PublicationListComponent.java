package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.content.beans.query.filter.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.parameters.*;
import org.hippoecm.repository.util.DateTools.Resolution;
import org.onehippo.cms7.essentials.components.*;
import org.slf4j.*;
import uk.nhs.digital.common.components.info.PublicationListComponentInfo;
import uk.nhs.digital.ps.beans.Publication;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        query.addOrderByDescending("publicationsystem:NominalDate");
        query.addOrderByAscending("publicationsystem:publicationtier");

        try {
            filter.addLessOrEqualThan("publicationsystem:NominalDate",
                GregorianCalendar.from(
                    ZonedDateTime.now(ZoneId.of("UTC")).minusHours(Publication.HOUR_OF_PUBLIC_RELEASE)
                        .minusMinutes(Publication.MINUTE_OF_PUBLIC_RELEASE)), Resolution.DAY);
            filter.addEqualTo("hippostd:stateSummary", "live");
            if (!componentParametersInfo.getDisplayTier3()) {
                filter.addNotEqualTo("publicationsystem:publicationtier", "Tier 3");
            }
        } catch (FilterException ex) {
            log.error("Errors while adding PubliclyAccessible filter", ex);
        }
    }
}
