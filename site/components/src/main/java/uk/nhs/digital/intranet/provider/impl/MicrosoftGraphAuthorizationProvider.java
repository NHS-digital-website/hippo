package uk.nhs.digital.intranet.provider.impl;

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
import uk.nhs.digital.intranet.provider.AuthorizationProvider;

import java.net.URI;

public class MicrosoftGraphAuthorizationProvider implements AuthorizationProvider {

    private static final String USER_READ_ALL_PERMISSION = "user.read.all";

    private final RestTemplate restTemplate;
    private final String applicationId;
    private final String redirectUri;
    private final String clientSecret;
    private final String baseUri;

    public MicrosoftGraphAuthorizationProvider(final RestTemplate restTemplate, final String applicationId,
                                               final String redirectUri, final String clientSecret, final String baseUri) {
        this.restTemplate = restTemplate;
        this.applicationId = applicationId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.baseUri = baseUri;
    }

    @Override
    public AccessToken processAuthorizationResponse(final String authorizationCode) throws AuthorizationException {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", applicationId);
        map.add("scope", USER_READ_ALL_PERMISSION);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");
        map.add("client_secret", clientSecret);
        map.add("code", authorizationCode);

        return getAccessToken(map);
    }

    @Override
    public AccessToken refreshAccessToken(final AccessToken accessToken) throws AuthorizationException {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", applicationId);
        map.add("scope", USER_READ_ALL_PERMISSION);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "refresh_token");
        map.add("client_secret", clientSecret);
        map.add("refresh_token", accessToken.getRefreshToken());

        return getAccessToken(map);
    }

    private AccessToken getAccessToken(MultiValueMap<String, String> map) throws AuthorizationException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(map, headers);

        try {
            final ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(URI.create(baseUri + "token"), httpRequest, TokenResponse.class);
            final TokenResponse tokenResponse = responseEntity.getBody();
            Assert.notNull(tokenResponse, "Received null response from Microsoft Graph API.");
            return new AccessToken(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), tokenResponse.getExpiresIn());
        } catch (final HttpStatusCodeException e) {
            throw new AuthorizationException(e.getStatusCode(), e);
        }
    }
}
