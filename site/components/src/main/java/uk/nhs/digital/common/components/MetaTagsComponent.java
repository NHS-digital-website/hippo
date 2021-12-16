package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.util.DocumentUtils;

public class MetaTagsComponent extends CommonComponent {
    private static final Logger log = LoggerFactory.getLogger(MetaTagsComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        log.debug("Value of request path info is " + request.getPathInfo());
        DocumentUtils.setMetaTags(request, this);
    }
}
