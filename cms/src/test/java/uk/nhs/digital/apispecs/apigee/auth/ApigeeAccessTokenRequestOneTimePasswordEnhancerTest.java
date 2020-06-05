package uk.nhs.digital.apispecs.apigee.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.util.MultiValueMap;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

public class ApigeeAccessTokenRequestOneTimePasswordEnhancerTest {

    @Mock private IGoogleAuthenticator googleAuthenticator;
    @Mock private AccessTokenRequest request;
    @Mock private OAuth2ProtectedResourceDetails resource;
    @Mock private MultiValueMap<String, String> form;
    @Mock private HttpHeaders headers;

    private Clock clock;

    private String testOtpKey;
    private String testBasicAuthToken;

    private ApigeeAccessTokenRequestOneTimePasswordEnhancer enhancer;
    private int expectedOtpCode;

    @Before
    public void setUp() {
        initMocks(this);

        testOtpKey = randomString();
        testBasicAuthToken = randomString();
        expectedOtpCode = randomInt();

        clock = Clock.fixed(Instant.parse("2020-06-10T11:17:00.017Z"), ZoneId.of("UTC"));

        enhancer = new ApigeeAccessTokenRequestOneTimePasswordEnhancer(
            googleAuthenticator,
            testOtpKey,
            testBasicAuthToken,
            clock
        );
    }

    @Test
    public void enhancesRequestFormWithAuthorizationHeader() {

        // given
        // setUp()

        // when
        enhancer.enhance(request, resource, form, headers);

        // then
        then(headers).should().add("Authorization", "Basic " + testBasicAuthToken);
    }

    @Test
    public void enhancesRequestFormWithOneTimePassword() {

        // given
        given(googleAuthenticator.getTotpPassword(any(), anyLong())).willReturn(expectedOtpCode);

        // when
        enhancer.enhance(request, resource, form, headers);

        // then
        then(googleAuthenticator).should().getTotpPassword(testOtpKey, clock.millis());
        then(form).should().add("mfa_token", String.valueOf(expectedOtpCode));
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    private int randomInt() {
        return RandomUtils.nextInt();
    }
}