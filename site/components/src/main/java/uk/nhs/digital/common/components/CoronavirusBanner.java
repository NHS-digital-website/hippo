package uk.nhs.digital.common.components;

import jakarta.servlet.http.Cookie;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.resourcebundle.ResourceBundleUtils;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;

import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class CoronavirusBanner extends EssentialsContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        request.setAttribute("showBanner", shouldShowBanner(request));
    }

    private boolean shouldShowBanner(HstRequest request) {
        return bannerIsSwitchedOn(request)
            && !userHasHiddenBanner(request)
            && !isCoronavirusDirectory(request);
    }

    private boolean userHasHiddenBanner(HstRequest request) {
        Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase("hide-coronavirus-banner")).findFirst();
        return cookie.isPresent() && cookie.get().getValue().equalsIgnoreCase("true") ? true : false;
    }

    private boolean isCoronavirusDirectory(HstRequest request) {
        return request.getRequestContext().getBaseURL().getPathInfo().toLowerCase().startsWith("/coronavirus");
    }

    private boolean bannerIsSwitchedOn(HstRequest request) {
        ResourceBundle bundle = ResourceBundleUtils.getBundle("coronavirus.banner", request.getLocale());
        if (bundle.containsKey("show-banner")) {
            String shouldShow = bundle.getString("show-banner");
            return Boolean.valueOf(shouldShow);
        }
        return false;
    }
}
