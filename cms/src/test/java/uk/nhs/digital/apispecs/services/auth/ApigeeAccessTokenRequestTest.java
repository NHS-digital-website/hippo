package uk.nhs.digital.apispecs.services.auth;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;

import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

public class ApigeeAccessTokenRequestTest {

    @Mock private IGoogleAuthenticator googleAuthenticator;
    @Mock private RestTemplate restTemplate;
    @Mock private ResponseEntity<Map> response;

    private Clock clock;

    private String testOtpKey;
    private String testBasicAuthToken;

    private ApigeeOAuth2TokenProvider provider;
    private int expectedOtpCode;

    @Before
    public void setUp() {
        openMocks(this);

        testOtpKey = randomString();
        testBasicAuthToken = randomString();
        expectedOtpCode = randomInt();

        clock = Clock.fixed(Instant.parse("2020-06-10T11:17:00.017Z"), ZoneId.of("UTC"));

        provider = new ApigeeOAuth2TokenProvider(
            googleAuthenticator,
            testOtpKey,
            testBasicAuthToken,
            clock,
            restTemplate
        );
    }

    @Test
    public void testRequestWithOneTimePassword() {

        // given
        given(googleAuthenticator.getTotpPassword(any(), anyLong())).willReturn(expectedOtpCode);
        given(restTemplate.exchange(eq("https://get.a.token.com"), eq(HttpMethod.POST), any(), (Class<Map>) any())).willReturn(response);
        given(response.getStatusCode()).willReturn(HttpStatusCode.valueOf(200));
        given(response.getBody()).willReturn(Map.of("access_token", "12345"));

        // when
        provider.getAccessToken("https://get.a.token.com", "user", "pass");

        // then
        then(googleAuthenticator).should().getTotpPassword(testOtpKey, clock.millis());
        then(restTemplate).should().exchange("https://get.a.token.com", HttpMethod.POST, expectedHttpEntity(), Map.class);
    }

    private HttpEntity<MultiValueMap<String, String>> expectedHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + testBasicAuthToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", "user");
        body.add("password", "pass");
        body.add("mfa_token", String.valueOf(expectedOtpCode));

        return new HttpEntity<>(body, headers);
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    private int randomInt() {
        return RandomUtils.nextInt();
    }
}