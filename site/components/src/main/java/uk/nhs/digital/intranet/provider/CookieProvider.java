package uk.nhs.digital.intranet.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;

import java.io.IOException;

import javax.servlet.http.Cookie;

public class CookieProvider {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "MGAT";
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieProvider.class);
    private static final int TTL = 30 * 24 * 3600;

    public static Cookie getAccessTokenCookie(final AccessToken accessToken) {
        final String encodedAccessToken;
        try {
            encodedAccessToken = AccessTokenEncoder.encode(accessToken);
            final Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, encodedAccessToken);
            accessTokenCookie.setPath("/intranet");
            accessTokenCookie.setMaxAge(TTL);
            return accessTokenCookie;
        } catch (final IOException e) {
            LOGGER.error("Could not encode access token.", e);
            return null;
        }
    }
}
