package uk.nhs.digital.common.components;

import jakarta.servlet.ServletContext;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.onehippo.forge.breadcrumb.components.BreadcrumbProvider;

public class BreadcrumbComponent extends BaseHstComponent {

    private CustomBreadcrumbProvider customBreadcrumbProvider;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute(BreadcrumbProvider.ATTRIBUTE_NAME, customBreadcrumbProvider.getBreadcrumb(request));
    }

    @Override
    public void init(final ServletContext servletContext, final ComponentConfiguration componentConfig) throws HstComponentException {
        super.init(servletContext, componentConfig);
        customBreadcrumbProvider = new CustomBreadcrumbProvider(this);
    }

}
