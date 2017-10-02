package uk.nhs.digital.ps.components;

import uk.nhs.digital.ps.beans.Publication;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicationComponent extends BaseHstComponent {

    public static final Logger log = LoggerFactory.getLogger(PublicationComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();

        Publication publication = (Publication) ctx.getContentBean();

        if (publication != null) {
            request.setAttribute("document", publication);
        }
    }
}


