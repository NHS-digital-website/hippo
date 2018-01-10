package uk.nhs.digital.ps.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import uk.nhs.digital.ps.beans.Dataset;

import java.io.IOException;

public class DatasetComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        Dataset dataset = (Dataset) ctx.getContentBean();

        if (!dataset.isPubliclyAccessible()) {
            try {
                response.forward("/error/404");
            } catch (IOException ioException) {
                throw new HstComponentException("forward failed", ioException);
            }

            return;
        }

        request.setAttribute("dataset", dataset);
    }
}
