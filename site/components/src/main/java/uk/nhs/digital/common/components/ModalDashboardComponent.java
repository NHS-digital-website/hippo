package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.website.beans.ModalDashboard;

public class ModalDashboardComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        final ModalDashboard document = request.getRequestContext().getContentBean(ModalDashboard.class);
        request.setAttribute("document", document);
    }
}
