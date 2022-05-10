package uk.nhs.digital.intranet.provider;

import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;
import uk.nhs.digital.intranet.utils.Constants;

import javax.servlet.http.Cookie;

public class CookieProvider {

    public static final Cookie EMPTY_ACCESS_TOKEN_COOKIE = getEmptyCookie(Constants.ACCESS_TOKEN_COOKIE_NAME);
    public static final Cookie EMPTY_REFRESH_TOKEN_COOKIE = getEmptyCookie(Constants.REFRESH_TOKEN_COOKIE_NAME);

    private static Cookie getEmptyCookie(final String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private static final int TTL = 30 * 24 * 3600;
    private static final String COOKIE_PATH = "/";

    private final AccessTokenEncoder encoder;

    public CookieProvider(final AccessTokenEncoder encoder) {
        this.encoder = encoder;
    }

    public Cookie getAccessTokenCookie(final AccessToken accessToken) {
        final AccessToken lightAccessToken = new AccessToken(accessToken.getToken(), null, accessToken.getExpirationDate());
        final String encodedAccessToken = encoder.encode(lightAccessToken);
        final Cookie accessTokenCookie = new Cookie(Constants.ACCESS_TOKEN_COOKIE_NAME, encodedAccessToken);
        accessTokenCookie.setPath(COOKIE_PATH);
        accessTokenCookie.setMaxAge(TTL);
        accessTokenCookie.setHttpOnly(true);
        return accessTokenCookie;
    }

    public Cookie getRefreshTokenCookie(final AccessToken accessToken) {
        final Cookie refreshTokenCookie = new Cookie(Constants.REFRESH_TOKEN_COOKIE_NAME, accessToken.getRefreshToken());
        refreshTokenCookie.setPath(COOKIE_PATH);
        refreshTokenCookie.setMaxAge(TTL);
        refreshTokenCookie.setHttpOnly(true);
        return refreshTokenCookie;
    }
}
