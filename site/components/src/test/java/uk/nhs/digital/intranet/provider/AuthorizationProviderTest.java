package uk.nhs.digital.intranet.provider;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
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

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationProviderTest {

    private static final String APP_ID = "app_id";
    private static final String CLIENT_SECRET = "secret";
    private static final String REDIRECT_URI = "http://test.digital.uk/response";
    private static final String AUTHORIZATION_CODE = "authCode";
    private static final String TOKEN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token";
    private static final TokenResponse TOKEN_RESPONSE = getTokenResponse();
    private static final String TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh";
    private static final int EXPIRES_IN = 100;

    private static TokenResponse getTokenResponse() {
        final TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(TOKEN);
        tokenResponse.setRefreshToken(REFRESH_TOKEN);
        tokenResponse.setExpiresIn(EXPIRES_IN);
        return tokenResponse;
    }

    @Mock
    private RestTemplate restTemplate;

    private AuthorizationProvider authorizationProvider;

    @Before
    public void setUp() {
        authorizationProvider = new AuthorizationProvider(restTemplate, APP_ID, REDIRECT_URI, CLIENT_SECRET);
    }

    @Test
    public void callsGraphApiWithCorrectHeadersAndReturnsAccessToken() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenReturn(ResponseEntity.ok().body(TOKEN_RESPONSE));
        final HttpEntity<MultiValueMap<String, String>> httpRequest = getHttpRequest();

        final AccessToken accessToken = authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE);

        verify(restTemplate).postForEntity(URI.create(TOKEN_URL), httpRequest, TokenResponse.class);
        assertNotNull(accessToken);
        assertEquals(TOKEN, accessToken.getToken());
        assertEquals(REFRESH_TOKEN, accessToken.getRefreshToken());
        assertEquals(LocalDateTime.now().plusSeconds(EXPIRES_IN).truncatedTo(ChronoUnit.SECONDS), accessToken.getExpirationDate().truncatedTo(ChronoUnit.SECONDS));
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfAccessTokenResponseIsNull() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenReturn(ResponseEntity.ok().body(null));

        authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE);
    }

    @Test(expected = AuthorizationException.class)
    public void throwsAuthorizationExceptionIfGraphApiThrowsHttpException() throws Exception {
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        authorizationProvider.processAuthorizationResponse(AUTHORIZATION_CODE);
    }

    private HttpEntity<MultiValueMap<String, String>> getHttpRequest() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", APP_ID);
        map.add("scope", "https://graph.microsoft.com/user.readbasic.all");
        map.add("redirect_uri", REDIRECT_URI);
        map.add("grant_type", "authorization_code");
        map.add("client_secret", CLIENT_SECRET);
        map.add("code", AUTHORIZATION_CODE);

        return new HttpEntity<>(map, headers);
    }
}
