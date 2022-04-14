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
import uk.nhs.digital.common.earlyaccesskey.EarlyAccessKeyProcessor;
import uk.nhs.digital.website.beans.Publishedwork;
import uk.nhs.digital.website.beans.Publishedworkchapter;

import javax.servlet.http.HttpServletRequest;

public class PublishedWorkLinkedBeansComponent extends ContentRewriterComponent {

    private static Logger log =
        LoggerFactory.getLogger(PublishedWorkLinkedBeansComponent.class);

    @Override
    public void doBeforeRender(final HstRequest hstRequest, final HstResponse hstResponse) {
        super.doBeforeRender(hstRequest, hstResponse);
        //linkPath property contains the relative path of the links property in the published workflow document
        String linkPath = getComponentParameter("linkPath");

        final HstRequestContext context = hstRequest.getRequestContext();
        HttpServletRequest httpServletRequest = context.getServletRequest();

        // we assume a PublishedWorkChapter as content bean, thus expect a PublishedWork as "parent" document
        Publishedworkchapter publishedWorkChapter = context.getContentBean(Publishedworkchapter.class);
        try {
            HstQuery linkedBeanQuery = ContentBeanUtils.createIncomingBeansQuery(
                publishedWorkChapter, context.getSiteContentBaseBean(),
                linkPath,
                Publishedwork.class, false);
            //chapter cannot be re-used across different publishedworkflow
            linkedBeanQuery.setLimit(1);
            //linked documents will contain the publishedworkflow document containing all the chapters
            HstQueryResult linkedDocuments = linkedBeanQuery.execute();
            hstRequest.setAttribute("linkeddocuments", linkedDocuments);
            Publishedwork publishedworkDocument = (Publishedwork) linkedDocuments.getHippoBeans().nextHippoBean();
            checkInvalidEarlyAccessKey(hstRequest, hstResponse, publishedworkDocument, httpServletRequest);
        } catch (QueryException queryException) {
            log.warn("QueryException ", queryException);
        }
    }

    /**
     * Checks if a pre release access key is set and if so calls a method to check if it is valid.
     *
     * @param hstRequest The hst request object
     * @param hstResponse The hst reqsponse object
     * @param publishedworkDocument The PublishedWorkDocument bean object
     * @param httpServletRequest The http request object
     */
    private void checkInvalidEarlyAccessKey(HstRequest hstRequest, HstResponse hstResponse, Publishedwork publishedworkDocument, HttpServletRequest httpServletRequest) {
        EarlyAccessKeyProcessor earlyAccessKeyProcessor = new EarlyAccessKeyProcessor();
        earlyAccessKeyProcessor.checkInvalidEarlyAccessKey(publishedworkDocument, hstRequest, hstResponse, httpServletRequest);
    }

}
