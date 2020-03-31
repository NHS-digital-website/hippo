package uk.nhs.digital.intranet.provider;

import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;

import javax.servlet.http.Cookie;

public class CookieProvider {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "__MGACCESSTOKEN";
    private static final int TTL = 30 * 24 * 3600;
    private static final String COOKIE_PATH = "/site/intranet";

    private final AccessTokenEncoder encoder;

    public CookieProvider(final AccessTokenEncoder encoder) {
        this.encoder = encoder;
    }

    public Cookie getAccessTokenCookie(final AccessToken accessToken) {
        final String encodedAccessToken = encoder.encode(accessToken);
        final Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, encodedAccessToken);
        accessTokenCookie.setPath(COOKIE_PATH);
        accessTokenCookie.setMaxAge(TTL);
        return accessTokenCookie;
    }
}
