package uk.nhs.digital.common.components;

import jakarta.servlet.http.HttpServletRequest;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import uk.nhs.digital.common.earlyaccesskey.EarlyAccessKeyProcessor;
import uk.nhs.digital.website.beans.General;

public class GeneralArticleComponent extends DocumentChildComponent {

    EarlyAccessKeyProcessor earlyAccessKeyProcessor = new EarlyAccessKeyProcessor();

    @Override
    public void doBeforeRender(final HstRequest hstRequest, final HstResponse hstResponse) {
        super.doBeforeRender(hstRequest, hstResponse);
        final HstRequestContext context = RequestContextProvider.get();
        HttpServletRequest request = context.getServletRequest();
        Object bean = hstRequest.getAttribute(REQUEST_ATTR_DOCUMENT);
        if (bean != null && bean instanceof HippoBean) {
            General generalDocument = (General) bean;
            earlyAccessKeyProcessor.checkInvalidEarlyAccessKey(generalDocument, hstRequest, hstResponse, request);
        }
    }

}