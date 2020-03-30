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

    public static final String base_url = "https://login.microsoftonline.com/common/oauth2/v2.0/";

    //todo extract into properties files
    public static final String applicationId = "a1a6e366-394a-43a6-9de8-4f2096814edf";
    public static final String redirectUri = "http://localhost:8080/site/restapi/intranet/auth/response";
    private static final String clientSecret = "h9Fs87G-vi-6UT_l]Zw?8M=L?@zFjYeh";

    private final RestTemplate restTemplate;

    public AuthorizationProvider(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            final ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(URI.create(base_url + "token"), httpRequest, TokenResponse.class);
            final TokenResponse tokenResponse = responseEntity.getBody();
            Assert.notNull(tokenResponse, "Received null response from Microsoft Graph API.");
            return new AccessToken(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), tokenResponse.getExpiresIn());
        } catch (final HttpStatusCodeException e) {
            throw new AuthorizationException(e.getStatusCode(), e);
        }
    }
}
