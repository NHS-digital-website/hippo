package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import java.util.Optional;

public class ApiSpecificationComponent extends ContentRewriterComponent {

    @Override public void doBeforeRender(final HstRequest request,
                                         final HstResponse response) {
        super.doBeforeRender(request, response);

        Optional.ofNullable(request.getRequestContext().getContentBean()).ifPresent(document -> {
            request.setAttribute("document", document); // for the main template
            request.setAttribute("path", request.getPathInfo()); // for the main template
        });
    }
}
