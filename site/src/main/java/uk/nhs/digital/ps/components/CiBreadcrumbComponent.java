package uk.nhs.digital.ps.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.ps.beans.navigation.CiBreadcrumb;
import uk.nhs.digital.ps.beans.navigation.CiBreadcrumbProvider;

public class CiBreadcrumbComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        CiBreadcrumbProvider ciBreadcrumbProvider = new CiBreadcrumbProvider(request);
        CiBreadcrumb ciBreadcrumb = ciBreadcrumbProvider.getBreadcrumb();

        request.setAttribute("ciBreadcrumb", ciBreadcrumb);
    }
}
