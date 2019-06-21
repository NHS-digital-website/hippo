package uk.nhs.digital.ps.components;

import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.request.*;
import org.slf4j.*;
import uk.nhs.digital.common.components.*;
import uk.nhs.digital.ps.beans.*;

public class LegacyPublicationComponent extends BaseGaContentComponent {

    private static final Logger log = LoggerFactory.getLogger(LegacyPublicationComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        LegacyPublication publication = getPublication(ctx);

        request.setAttribute("legacyPublication", publication);
    }

    private LegacyPublication getPublication(HstRequestContext ctx) throws HstComponentException {
        HippoBean content = ctx.getContentBean();

        if (content instanceof LegacyPublication) {
            return (LegacyPublication) content;
        }

        if (content instanceof HippoFolder) {
            return Publication.getPublicationInFolder((HippoFolder)content, LegacyPublication.class);
        }

        log.warn("Cannot find Publication document for: {}", content.getPath());
        throw new HstComponentException("Cannot find Publication document based on request content");
    }
}
