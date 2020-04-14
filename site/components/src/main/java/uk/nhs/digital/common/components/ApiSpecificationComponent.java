package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;

public class ApiSpecificationComponent extends BaseGaContentComponent {

    @Override public void doBeforeRender(final HstRequest request,
                                         final HstResponse response) {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();

        final HippoBean document = ctx.getContentBean();

        if (document != null) {
            request.setAttribute("document", document);
        }
    }
}
