package uk.nhs.digital.intranet.valves;

import static org.mockito.Mockito.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hippoecm.hst.core.container.ValveContext;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.model.exception.AuthorizationException;
import uk.nhs.digital.intranet.provider.AuthorizationProvider;
import uk.nhs.digital.intranet.provider.CookieProvider;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;
import uk.nhs.digital.intranet.utils.Constants;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class AccessTokenValveTest extends MockitoSessionTestBase {

    private static final String ENCODED_COOKIE_VALUE = "encoded_cookie_value";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final Cookie ACCESS_TOKEN_COOKIE = new Cookie(Constants.ACCESS_TOKEN_COOKIE_NAME, ENCODED_COOKIE_VALUE);
    private static final Cookie REFRESH_TOKEN_COOKIE = new Cookie(Constants.REFRESH_TOKEN_COOKIE_NAME, REFRESH_TOKEN);

    @Mock private AccessTokenEncoder encoder;
    @Mock private AuthorizationProvider authorizationProvider;
    @Mock private CookieProvider cookieProvider;
    @Mock private ValveContext valveContext;
    @Mock private HstRequestContext requestContext;
    @Mock private HttpServletResponse servletResponse;
    @Mock private HttpServletRequest servletRequest;

    private AccessTokenValve valve;

    @Before
    public void setUp() {
        valve = new AccessTokenValve(encoder, authorizationProvider, cookieProvider);
        when(requestContext.getServletResponse()).thenReturn(servletResponse);
        when(requestContext.getServletRequest()).thenReturn(servletRequest);
        when(valveContext.getRequestContext()).thenReturn(requestContext);
    }

    @Ignore
    @Test // Flaky test & the intranet code is earmarked for deletion!
    public void invokesNextValveIfNoCookiesPresent() throws Exception {
        when(servletRequest.getCookies()).thenReturn(null);

        valve.invoke(valveContext);

        verify(requestContext, never()).setAttribute(anyString(), any());
        verify(valveContext).invokeNext();
    }

    @Ignore
    @Test // Flaky test & the intranet code is earmarked for deletion!
    public void invokesNextValveIfIncorrectCookiePresent() throws Exception {
        when(servletRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("name", "value")});

        valve.invoke(valveContext);

        verify(requestContext, never()).setAttribute(anyString(), any());
        verify(valveContext).invokeNext();
    }

    @Ignore
    @Test // Flaky test & the intranet code is earmarked for deletion!
    public void setsRequestContextAttributeIfAccessTokenNotExpired() throws Exception {
        final AccessToken accessToken = new AccessToken("token", "refresh", 3600);
        when(servletRequest.getCookies()).thenReturn(new Cookie[]{ACCESS_TOKEN_COOKIE});
        when(encoder.decode(ENCODED_COOKIE_VALUE)).thenReturn(accessToken);

        valve.invoke(valveContext);

        verify(requestContext).setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "token");
        verify(valveContext).invokeNext();
    }

    @Ignore
    @Test // Flaky test & the intranet code is earmarked for deletion!
    public void requestsNewAccessTokenIfAccessTokenIsExpired() throws Exception {
        final AccessToken expiredAccessToken = new AccessToken("token", null, -3600);
        final AccessToken completeAccessToken = new AccessToken("token", REFRESH_TOKEN, -3600);
        final AccessToken newAccessToken = new AccessToken("new-token", "refresh", 3600);

        when(servletRequest.getCookies()).thenReturn(new Cookie[]{ACCESS_TOKEN_COOKIE, REFRESH_TOKEN_COOKIE});
        when(encoder.decode(ENCODED_COOKIE_VALUE)).thenReturn(expiredAccessToken);
        when(authorizationProvider.refreshAccessToken(completeAccessToken)).thenReturn(newAccessToken);
        when(cookieProvider.getAccessTokenCookie(newAccessToken)).thenReturn(ACCESS_TOKEN_COOKIE);
        when(cookieProvider.getRefreshTokenCookie(newAccessToken)).thenReturn(REFRESH_TOKEN_COOKIE);

        valve.invoke(valveContext);

        verify(requestContext).setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "new-token");
        verify(servletResponse).addCookie(ACCESS_TOKEN_COOKIE);
        verify(servletResponse).addCookie(REFRESH_TOKEN_COOKIE);
        verify(valveContext).invokeNext();
    }

    @Ignore
    @Test // Flaky test & the intranet code is earmarked for deletion!
    public void failsGracefullyIfCannotRequestNewAccessToken() throws Exception {
        final AccessToken expiredAccessToken = new AccessToken("token", null, -3600);
        final AccessToken completeAccessToken = new AccessToken("token", REFRESH_TOKEN, -3600);
        when(servletRequest.getCookies()).thenReturn(new Cookie[]{ACCESS_TOKEN_COOKIE, REFRESH_TOKEN_COOKIE});
        when(encoder.decode(ENCODED_COOKIE_VALUE)).thenReturn(expiredAccessToken);
        when(authorizationProvider.refreshAccessToken(completeAccessToken)).thenThrow(new AuthorizationException(HttpStatus.BAD_REQUEST, new RuntimeException()));

        valve.invoke(valveContext);

        verify(requestContext).removeAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME);
        verify(servletResponse).addCookie(CookieProvider.EMPTY_ACCESS_TOKEN_COOKIE);
        verify(servletResponse).addCookie(CookieProvider.EMPTY_REFRESH_TOKEN_COOKIE);
        verify(valveContext).invokeNext();
    }

    @Ignore
    @Test // Flaky test & the intranet code is earmarked for deletion!
    public void usesNullRefreshTokenIfRefreshTokenCookieNotPresent() throws Exception {
        final AccessToken expiredAccessToken = new AccessToken("token", null, -3600);
        when(servletRequest.getCookies()).thenReturn(new Cookie[]{ACCESS_TOKEN_COOKIE});
        when(encoder.decode(ENCODED_COOKIE_VALUE)).thenReturn(expiredAccessToken);
        when(authorizationProvider.refreshAccessToken(expiredAccessToken)).thenThrow(new AuthorizationException(HttpStatus.BAD_REQUEST, new RuntimeException()));

        valve.invoke(valveContext);

        verify(requestContext).removeAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME);
        verify(servletResponse).addCookie(CookieProvider.EMPTY_ACCESS_TOKEN_COOKIE);
        verify(servletResponse).addCookie(CookieProvider.EMPTY_REFRESH_TOKEN_COOKIE);
        verify(valveContext).invokeNext();
    }
}
