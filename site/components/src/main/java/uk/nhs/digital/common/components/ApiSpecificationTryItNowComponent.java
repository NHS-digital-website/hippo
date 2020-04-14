package uk.nhs.digital.common.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import java.util.Optional;

public class ApiSpecificationTryItNowComponent extends BaseHstComponent {

    @Override public void doBeforeRender(final HstRequest request,
                                         final HstResponse response) {
        super.doBeforeRender(request, response);

        // Set by ApiSpecificationComponent
        Optional.ofNullable(request.getSession().getAttribute("document"))
            .ifPresent(document -> request.setAttribute("document", document));
    }
}
