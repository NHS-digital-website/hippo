package uk.nhs.digital.intranet.provider;

import static junit.framework.TestCase.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.nhs.digital.intranet.json.TokenResponse;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.model.exception.AuthorizationException;
import uk.nhs.digital.intranet.provider.impl.MicrosoftGraphAuthorizationProvider;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RunWith(MockitoJUnitRunner.class)
public class MicrosoftGraphAuthorizationProviderTest extends MockitoSessionTestBase {

    private static final String APP_ID = "app_id";
    private static final String CLIENT_SECRET = "secret";
    private static final String REDIRECT_URI = "http://test.digital.uk/response";
    private static final String AUTHORIZATION_CODE = "authCode";
    private static final String TOKEN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
    private static final String BASE_URI = "https://login.microsoftonline.com/common/oauth2/v2.0/";
    private static final TokenResponse TOKEN_RESPONSE = getTokenResponse();
    private static final String TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh";
    private static final int EXPIRES_IN = 100;
    private static final String SCOPE = "user.read.all";

    private static TokenResponse getTokenResponse() {
        final TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(TOKEN);
        tokenResponse.setRefreshToken(REFRESH_TOKEN);
        tokenResponse.setExpiresIn(EXPIRES_IN);
        return tokenResponse;
    }

    @Mock
    private RestTemplate restTemplate;

    private MicrosoftGraphAuthorizationProvider authorizationProvider;

    @Before
    public void setUp() {
        authorizationProvider = new MicrosoftGraphAuthorizationProvider(restTemplate, APP_ID, REDIRECT_URI, CLIENT_SECRET, BASE_URI);
    }

    @Test
    public void callsGraphApiWithCorrectHeadersAndReturnsAccessToken() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenReturn(ResponseEntity.ok().body(TOKEN_RESPONSE));
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", APP_ID);
        map.add("scope", SCOPE);
        map.add("redirect_uri", REDIRECT_URI);
        map.add("grant_type", "authorization_code");
        map.add("client_secret", CLIENT_SECRET);
        map.add("code", AUTHORIZATION_CODE);
        final HttpEntity<MultiValueMap<String, String>> httpRequest = getHttpRequest(map);

        final AccessToken accessToken = authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE);

        verify(restTemplate).postForEntity(URI.create(TOKEN_URL), httpRequest, TokenResponse.class);
        assertNotNull(accessToken);
        assertEquals(TOKEN, accessToken.getToken());
        assertEquals(REFRESH_TOKEN, accessToken.getRefreshToken());

        assertTrue("Token expiration should be within 5 seconds of calculated time", timeWithin5Seconds(accessToken));
    }

    @Test
    public void callsGraphApiWithCorrectHeadersAndRefreshesAccessToken() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenReturn(ResponseEntity.ok().body(TOKEN_RESPONSE));
        final AccessToken oldAccessToken = new AccessToken(TOKEN, REFRESH_TOKEN, 1);
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", APP_ID);
        map.add("scope", SCOPE);
        map.add("redirect_uri", REDIRECT_URI);
        map.add("grant_type", "refresh_token");
        map.add("client_secret", CLIENT_SECRET);
        map.add("refresh_token", REFRESH_TOKEN);
        final HttpEntity<MultiValueMap<String, String>> httpRequest = getHttpRequest(map);

        final AccessToken accessToken = authorizationProvider.refreshAccessToken(oldAccessToken);

        verify(restTemplate).postForEntity(URI.create(TOKEN_URL), httpRequest, TokenResponse.class);
        assertNotNull(accessToken);
        assertEquals(TOKEN, accessToken.getToken());
        assertEquals(REFRESH_TOKEN, accessToken.getRefreshToken());

        assertTrue("Token expiration should be within 5 seconds of calculated time", timeWithin5Seconds(accessToken));
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfAccessTokenResponseIsNullOnRequestAccessToken() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenReturn(ResponseEntity.ok().body(null));

        authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfAccessTokenResponseIsNullOnRefreshAccessToken() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenReturn(ResponseEntity.ok().body(null));
        final AccessToken oldAccessToken = new AccessToken(TOKEN, REFRESH_TOKEN, 1);

        authorizationProvider.refreshAccessToken(oldAccessToken);
    }

    @Test(expected = AuthorizationException.class)
    public void throwsAuthorizationExceptionIfGraphApiThrowsHttpExceptionOnRequestAccessToken() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE);
    }

    @Test(expected = AuthorizationException.class)
    public void throwsAuthorizationExceptionIfGraphApiThrowsHttpExceptionOnRefreshAccessToken() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        final AccessToken oldAccessToken = new AccessToken(TOKEN, REFRESH_TOKEN, 1);

        authorizationProvider.refreshAccessToken(oldAccessToken);
    }

    private HttpEntity<MultiValueMap<String, String>> getHttpRequest(MultiValueMap<String, String> map) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(map, headers);
    }

    private boolean timeWithin5Seconds(AccessToken accessToken) {
        long timeDiffInSeconds = Math.abs(ChronoUnit.SECONDS.between(LocalDateTime.now().plusSeconds(EXPIRES_IN).truncatedTo(ChronoUnit.SECONDS),
            accessToken.getExpirationDate().truncatedTo(ChronoUnit.SECONDS)));

        return timeDiffInSeconds < 5;
    }
}
