package uk.nhs.digital.common.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import uk.nhs.digital.website.beans.HubNewsAndEvents;

public class LatestNewsAndEventsComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final HstRequestContext ctx = request.getRequestContext();
        final HubNewsAndEvents document = (HubNewsAndEvents) ctx.getContentBean();
        if (document != null) {
            request.setAttribute("document", document);
        }
    }

}
