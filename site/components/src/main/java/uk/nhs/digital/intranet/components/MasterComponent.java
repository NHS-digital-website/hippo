package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.CommonComponent;

public class MasterComponent extends CommonComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final HstRequestContext context = request.getRequestContext();
        final HippoBean document = context.getContentBean();

        if (document != null) {
            request.setAttribute("document", document);
        }
    }
}
