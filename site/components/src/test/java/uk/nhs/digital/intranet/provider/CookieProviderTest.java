package uk.nhs.digital.intranet.provider;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;
import uk.nhs.digital.intranet.utils.Constants;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import javax.servlet.http.Cookie;

@RunWith(MockitoJUnitRunner.class)
public class CookieProviderTest extends MockitoSessionTestBase {

    @Mock private AccessTokenEncoder encoder;

    private CookieProvider cookieProvider;

    @Before
    public void setUp() {
        when(encoder.encode(any(AccessToken.class))).thenReturn("expected_returned_value");
        cookieProvider = new CookieProvider(encoder);
    }

    @Test
    public void returnsEncodedAccessTokenCookie() {
        final AccessToken accessToken = new AccessToken("token", "refresh-token", 10);

        final Cookie cookie = cookieProvider.getAccessTokenCookie(accessToken);

        assertEquals(Constants.ACCESS_TOKEN_COOKIE_NAME, cookie.getName());
        assertEquals(2592000, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
        assertEquals("expected_returned_value", cookie.getValue());
    }

    @Test
    public void returnsRefreshTokenCookie() {
        final AccessToken accessToken = new AccessToken("token", "refresh-token", 10);

        final Cookie cookie = cookieProvider.getRefreshTokenCookie(accessToken);

        assertEquals(Constants.REFRESH_TOKEN_COOKIE_NAME, cookie.getName());
        assertEquals(2592000, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
        assertEquals("refresh-token", cookie.getValue());
    }
}
