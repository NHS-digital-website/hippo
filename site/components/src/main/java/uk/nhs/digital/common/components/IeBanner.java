package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.resourcebundle.ResourceBundleUtils;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;

import java.util.ResourceBundle;

public class IeBanner extends EssentialsContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        request.setAttribute("showBanner", shouldShowBanner(request));
    }

    private boolean shouldShowBanner(HstRequest request) {
        return bannerIsSwitchedOn(request);
    }

    private boolean bannerIsSwitchedOn(HstRequest request) {
        ResourceBundle bundle = ResourceBundleUtils.getBundle("ie.banner", request.getLocale());
        if (bundle.containsKey("show-banner")) {
            String shouldShow = bundle.getString("show-banner");
            return Boolean.valueOf(shouldShow);
        }
        return false;
    }
}
