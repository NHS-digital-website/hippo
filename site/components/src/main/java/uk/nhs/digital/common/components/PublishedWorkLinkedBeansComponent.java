package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.query.*;
import org.hippoecm.hst.content.beans.query.exceptions.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.request.*;
import org.hippoecm.hst.util.*;
import org.slf4j.*;
import uk.nhs.digital.website.beans.*;

public class PublishedWorkLinkedBeansComponent extends BaseGaContentComponent {

    private static Logger log =
        LoggerFactory.getLogger(PublishedWorkLinkedBeansComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        //linkPath property contains the relative path of the links property in the published workflow document
        String linkPath = getComponentParameter("linkPath");

        final HstRequestContext context = request.getRequestContext();
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
            request.setAttribute("linkeddocuments", linkedDocuments);
        } catch (QueryException queryException) {
            log.warn("QueryException ", queryException);
        }
    }
}
