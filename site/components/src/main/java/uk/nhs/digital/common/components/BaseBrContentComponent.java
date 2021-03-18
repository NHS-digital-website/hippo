package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.*;
import org.onehippo.cms7.essentials.components.*;
import uk.nhs.digital.common.contentrewriters.*;

public class BaseBrContentComponent extends DocumentChildComponent {

    public static final BrandRefreshContentRewriter brContentRewriter =
        new BrandRefreshContentRewriter();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        request.setAttribute("brContentRewriter", brContentRewriter);
    }
}
