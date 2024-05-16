package uk.nhs.digital.intranet.valves;

import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.model.exception.AuthorizationException;
import uk.nhs.digital.intranet.provider.AuthorizationProvider;
import uk.nhs.digital.intranet.provider.CookieProvider;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;
import uk.nhs.digital.intranet.utils.Constants;

import java.util.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class AccessTokenValve extends AbstractOrderableValve {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenValve.class);

    private final AccessTokenEncoder encoder;
    private final AuthorizationProvider authorizationProvider;
    private final CookieProvider cookieProvider;

    public AccessTokenValve(final AccessTokenEncoder encoder, final AuthorizationProvider authorizationProvider, final CookieProvider cookieProvider) {
        this.encoder = encoder;
        this.authorizationProvider = authorizationProvider;
        this.cookieProvider = cookieProvider;
    }

    @Override
    public void invoke(final ValveContext context) throws ContainerException {
        try {
            final HstRequestContext requestContext = context.getRequestContext();
            final Optional<Cookie> cookieOptional = getCookie(requestContext.getServletRequest(), Constants.ACCESS_TOKEN_COOKIE_NAME);
            if (cookieOptional.isPresent()) {
                final Cookie cookie = cookieOptional.get();
                AccessToken accessToken = encoder.decode(cookie.getValue());
                if (accessToken.isExpired()) {
                    try {
                        accessToken = getNewAccessToken(requestContext, accessToken);
                    } catch (final AuthorizationException e) {
                        LOGGER.warn("Refresh token expired or not existent. User will need to login again.");
                        removeAccessToken(requestContext);
                        return;
                    }
                }
                requestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, accessToken.getToken());
            }
        } finally {
            context.invokeNext();
        }
    }

    private void removeAccessToken(final HstRequestContext requestContext) {
        // deletes the property from the context and add an empty cookie which removes the existing one
        // this way the user is prompted to authorize again
        requestContext.removeAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME);
        requestContext.getServletResponse().addCookie(CookieProvider.EMPTY_ACCESS_TOKEN_COOKIE);
        requestContext.getServletResponse().addCookie(CookieProvider.EMPTY_REFRESH_TOKEN_COOKIE);
    }

    private AccessToken getNewAccessToken(final HstRequestContext requestContext, final AccessToken accessToken) throws AuthorizationException {
        try {
            final String refreshToken = getCookie(requestContext.getServletRequest(), Constants.REFRESH_TOKEN_COOKIE_NAME).map(Cookie::getValue).orElse(null);
            final AccessToken completeAccessToken = new AccessToken(accessToken.getToken(), refreshToken, accessToken.getExpirationDate());
            final AccessToken newAccessToken = authorizationProvider.refreshAccessToken(completeAccessToken);
            final Cookie accessTokenCookie = cookieProvider.getAccessTokenCookie(newAccessToken);
            final Cookie refreshTokenCookie = cookieProvider.getRefreshTokenCookie(newAccessToken);

            requestContext.getServletResponse().addCookie(accessTokenCookie);
            requestContext.getServletResponse().addCookie(refreshTokenCookie);
            return newAccessToken;
        } catch (final AuthorizationException e) {
            LOGGER.error("Received exception with status code {} from Microsoft Graph API when trying to refresh access token.", e.getStatusCode().value(), e.getCause());
            throw e;
        }
    }

    private Optional<Cookie> getCookie(final HttpServletRequest request, final String cookieName) {
        return getCookies(request)
            .stream()
            .filter(Objects::nonNull)
            .filter(cookie -> cookieName.equals(cookie.getName()))
            .findAny();
    }

    private List<Cookie> getCookies(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        return cookies != null ? Arrays.asList(cookies) : Collections.emptyList();
    }
}
