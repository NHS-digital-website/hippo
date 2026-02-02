package uk.nhs.digital.common.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.forge.breadcrumb.components.BreadcrumbProvider;

public class BreadcrumbComponent extends BaseHstComponent {

    private final CustomBreadcrumbProvider customBreadcrumbProvider = new CustomBreadcrumbProvider(this);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute(BreadcrumbProvider.ATTRIBUTE_NAME, customBreadcrumbProvider.getBreadcrumb(request));
    }

}
