package uk.nhs.digital.intranet.valves;

import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import org.hippoecm.hst.core.request.HstRequestContext;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;
import uk.nhs.digital.intranet.utils.Constants;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class AccessTokenValve extends AbstractOrderableValve {

    private final AccessTokenEncoder encoder;

    public AccessTokenValve(final AccessTokenEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void invoke(ValveContext context) throws ContainerException {
        final HstRequestContext requestContext = context.getRequestContext();
        if (requestContext.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME) == null) {
            final Optional<Cookie> cookieOptional = getAccessTokenCookie(requestContext.getServletRequest());
            cookieOptional.ifPresent(cookie -> {
                final AccessToken accessToken = encoder.decode(cookie.getValue());
                if (accessToken != null) {
                    requestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, accessToken.getToken());
                }
            });
        }
        context.invokeNext();
    }

    private Optional<Cookie> getAccessTokenCookie(final HttpServletRequest request) {
        return getCookies(request)
            .stream()
            .filter(Objects::nonNull)
            .filter(cookie -> Constants.ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findAny();
    }

    private List<Cookie> getCookies(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        return cookies != null ? Arrays.asList(cookies) : Collections.emptyList();
    }
}
