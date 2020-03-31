package uk.nhs.digital.intranet.provider;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import uk.nhs.digital.intranet.json.TokenResponse;
import uk.nhs.digital.intranet.model.AccessToken;
import uk.nhs.digital.intranet.model.exception.AuthorizationException;

import java.net.URI;

public class AuthorizationProvider {

    public static final String BASE_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/";

    private final RestTemplate restTemplate;
    private final String applicationId;
    private final String redirectUri;
    private final String clientSecret;

    public AuthorizationProvider(final RestTemplate restTemplate, final String applicationId, final String redirectUri, final String clientSecret) {
        this.restTemplate = restTemplate;
        this.applicationId = applicationId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    public AccessToken processAuthorizationResponse(final String authorizationCode) throws AuthorizationException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", applicationId);
        map.add("scope", "https://graph.microsoft.com/user.readbasic.all");
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");
        map.add("client_secret", clientSecret);
        map.add("code", authorizationCode);

        final HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(map, headers);

        try {
            final ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(URI.create(BASE_URL + "token"), httpRequest, TokenResponse.class);
            final TokenResponse tokenResponse = responseEntity.getBody();
            Assert.notNull(tokenResponse, "Received null response from Microsoft Graph API.");
            return new AccessToken(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), tokenResponse.getExpiresIn());
        } catch (final HttpStatusCodeException e) {
            throw new AuthorizationException(e.getStatusCode(), e);
        }
    }
}
