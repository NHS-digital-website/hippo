package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.Financial;
import uk.nhs.digital.website.beans.Publishedwork;

public class FinancialLinkedBeansComponent extends ContentRewriterComponent {

    private static Logger log =
        LoggerFactory.getLogger(FinancialLinkedBeansComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        //linkPath property contains the relative path of the links property in the published workflow document
        String linkPath = getComponentParameter("linkPath");

        final HstRequestContext context = request.getRequestContext();
        // we assume a Financial as content bean, thus expect a PublishedWork as "parent" document
        Financial financial = context.getContentBean(Financial.class);
        try {
            HstQuery linkedBeanQuery = ContentBeanUtils.createIncomingBeansQuery(
                financial, context.getSiteContentBaseBean(),
                linkPath,
                Publishedwork.class, false);
            //chapter cannot be re-used across different publishedworkflow
            linkedBeanQuery.setLimit(1);
            //linked documents will contain the publishedworkflow document containing all the chapters
            HstQueryResult linkedDocuments = linkedBeanQuery.execute();
            request.setAttribute("linkeddocuments", linkedDocuments);
        } catch (QueryException queryException) {
            log.warn("QueryException ", queryException);
        }
    }
}
