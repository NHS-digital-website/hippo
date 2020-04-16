package uk.nhs.digital.intranet.utils;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;
import uk.nhs.digital.intranet.model.AccessToken;

public class AccessTokenEncoderTest {


    @Test
    public void encodesAndDecodesAccessToken() {
        final AccessToken initialAccessToken = new AccessToken("token", "refresh", 1000);
        final AccessTokenEncoder encoder = new AccessTokenEncoder();

        final String base64String = encoder.encode(initialAccessToken);
        final AccessToken resultAccessToken = encoder.decode(base64String);

        assertEquals(initialAccessToken.getToken(), resultAccessToken.getToken());
        assertEquals(initialAccessToken.getRefreshToken(), resultAccessToken.getRefreshToken());
        assertEquals(initialAccessToken.getExpirationDate(), resultAccessToken.getExpirationDate());
    }
}
