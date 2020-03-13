package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;

public class CoronavirusBanner extends EssentialsContentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase("hide-coronavirus-banner")).findFirst();
        request.setAttribute("showBanner", !(cookie.isPresent() && cookie.get().getValue().equalsIgnoreCase("true")) ? true : false);
    }
}
