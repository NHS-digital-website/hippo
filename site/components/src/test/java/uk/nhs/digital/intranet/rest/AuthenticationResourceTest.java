package uk.nhs.digital.intranet.rest;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.model.exception.AuthorizationException;
import uk.nhs.digital.intranet.provider.AuthorizationProvider;
import uk.nhs.digital.intranet.provider.CookieProvider;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationResourceTest extends MockitoSessionTestBase {

    private static final String AUTHORIZATION_CODE = "auth_code";
    private static final String REDIRECT_URI = "http://redirect.uri/home";

    @Mock private AuthorizationProvider authorizationProvider;
    @Mock private CookieProvider cookieProvider;
    @Mock private HttpServletResponse response;

    private AuthenticationResource authenticationResource;

    @Before
    public void setUp() {
        authenticationResource = new AuthenticationResource(authorizationProvider, cookieProvider, REDIRECT_URI);
    }

    @Test
    public void requestsAccessTokenAndSavesCookieAndRedirectsToUri() throws Exception {
        final AccessToken accessToken = new AccessToken("token", "refresh", 1);
        final Cookie accessTokenCookie = new Cookie("accessToken", "access_token_value");
        final Cookie refreshTokenCookie = new Cookie("refreshToken", "access_token_value");
        when(authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE)).thenReturn(accessToken);
        when(cookieProvider.getAccessTokenCookie(accessToken)).thenReturn(accessTokenCookie);
        when(cookieProvider.getRefreshTokenCookie(accessToken)).thenReturn(refreshTokenCookie);

        authenticationResource.processResponse(response, AUTHORIZATION_CODE);

        verify(response).addCookie(accessTokenCookie);
        verify(response).addCookie(refreshTokenCookie);
        verify(response).sendRedirect(REDIRECT_URI);
    }

    @Test
    public void redirectsToUriIfCannotRequestAccessToken() throws Exception {
        when(authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE)).thenThrow(new AuthorizationException(HttpStatus.BAD_REQUEST, null));

        authenticationResource.processResponse(response, AUTHORIZATION_CODE);

        verify(response, never()).addCookie(any(Cookie.class));
        verify(response).sendRedirect(REDIRECT_URI);
    }
}
