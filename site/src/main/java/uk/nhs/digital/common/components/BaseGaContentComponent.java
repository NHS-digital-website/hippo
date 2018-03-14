package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.*;
import org.onehippo.cms7.essentials.components.*;
import uk.nhs.digital.common.contentrewriters.*;

public class BaseGaContentComponent extends EssentialsContentComponent {

    public static final GoogleAnalyticsContentRewriter gaContentRewriter =
        new GoogleAnalyticsContentRewriter();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        request.setAttribute("gaContentRewriter", gaContentRewriter);
    }
}
