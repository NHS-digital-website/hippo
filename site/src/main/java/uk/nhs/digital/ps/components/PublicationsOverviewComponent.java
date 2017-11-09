package uk.nhs.digital.ps.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder.Order;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Publication;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

public class PublicationsOverviewComponent extends BaseHstComponent {

    private static final Logger log = LoggerFactory.getLogger(PublicationsOverviewComponent.class);

    private static final int ITEMS_PER_PAGE_COUNT = 10;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext requestContext = request.getRequestContext();

        request.setAttribute("publishedPublications", getPublicationBeans(requestContext, true, Order.DESC));
        request.setAttribute("upcomingPublications", getPublicationBeans(requestContext, false, Order.ASC));
    }

    private HippoBeanIterator getPublicationBeans(HstRequestContext requestContext, boolean isLive, final Order order) {
        try {
            HippoBean baseFolder = requestContext.getSiteContentBaseBean();

            final HstQuery hstQuery = HstQueryBuilder.create(baseFolder)
                .ofTypes(Publication.class)
                .orderBy(order, "publicationsystem:NominalDate")
                .where(constraint("publicationsystem:PubliclyAccessible").equalTo(isLive))
                .limit(ITEMS_PER_PAGE_COUNT)
                .build();

            HstQueryResult hstQueryResult = hstQuery.execute();

            return hstQueryResult.getHippoBeans();
        } catch (QueryException e) {
            log.error("Error getting list of published publications", e);
            return null;
        }
    }
}


