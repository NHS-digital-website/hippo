package uk.nhs.digital.intranet.provider;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.utils.AccessTokenEncoder;
import uk.nhs.digital.intranet.utils.Constants;

import javax.servlet.http.Cookie;

public class CookieProviderTest {

    @Test
    public void returnsEncodedAccessTokenCookie() {
        final AccessTokenEncoder encoder = Mockito.mock(AccessTokenEncoder.class);
        final CookieProvider cookieProvider = new CookieProvider(encoder);
        final AccessToken accessToken = new AccessToken("token", "refresh-token", 10);
        when(encoder.encode(accessToken)).thenReturn("expected_returned_value");

        final Cookie cookie = cookieProvider.getAccessTokenCookie(accessToken);

        assertEquals(Constants.ACCESS_TOKEN_COOKIE_NAME, cookie.getName());
        assertEquals(2592000, cookie.getMaxAge());
        assertEquals("/site/intranet", cookie.getPath());
        assertEquals("expected_returned_value", cookie.getValue());
    }
}
