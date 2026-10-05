package uk.nhs.digital.apispecs.services.auth;

import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Map;

public class ApigeeOAuth2TokenProvider {

    private static final Logger log = LoggerFactory.getLogger(ApigeeOAuth2TokenProvider.class);
    private static final int MAX_RESPONSE_BODY_LOG_LENGTH = 500;

    private final RestTemplate restTemplate;
    private final IGoogleAuthenticator otpGenerator;
    private final String otpKey;
    private final String basicAuthToken;
    private final Clock clock;

    public ApigeeOAuth2TokenProvider(IGoogleAuthenticator otpGenerator, String otpKey,
                                     String basicAuthToken, Clock clock, RestTemplate restTemplate) {
        this.otpGenerator = otpGenerator;
        this.otpKey = otpKey;
        this.basicAuthToken = basicAuthToken;
        this.clock = clock;
        this.restTemplate = restTemplate;

        ensureRequiredArgProvided("OTP key", otpKey);
        ensureRequiredArgProvided("basic auth token", basicAuthToken);
    }

    public String getAccessToken(String tokenUri, String username, String password) {
        log.debug("Requesting Apigee OAuth access token from {}.", safeUri(tokenUri));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        body.add("mfa_token", oneTimePassword());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                tokenUri, HttpMethod.POST, entity, Map.class
            );

            log.debug(
                "Apigee OAuth token response received from {}; status: {}; body present: {}.",
                safeUri(tokenUri),
                response.getStatusCode(),
                response.getBody() != null
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                final String accessToken = (String) response.getBody().get("access_token");

                if (accessToken != null) {
                    log.debug("Apigee OAuth token response included access_token.");
                    return accessToken;
                }

                throw new RuntimeException("Failed to get access token: response did not include access_token.");
            }

            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());

        } catch (final RestClientResponseException e) {
            log.error(
                "Apigee OAuth token request failed; endpoint: {}; status: {}; response body: {}.",
                safeUri(tokenUri),
                e.getRawStatusCode(),
                sanitizedResponseBody(e.getResponseBodyAsString()),
                e
            );
            throw e;
        } catch (final Exception e) {
            log.error("Apigee OAuth token request failed; endpoint: {}.", safeUri(tokenUri), e);
            throw e;
        }
    }

    private String oneTimePassword() {
        return String.valueOf(otpGenerator.getTotpPassword(otpKey, clock.millis()));
    }

    private void ensureRequiredArgProvided(final String argName, final String argValue) {
        Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }

    private String safeUri(final String url) {
        try {
            final URI uri = URI.create(url);
            return uri.getScheme() + "://" + uri.getHost() + safePort(uri) + uri.getPath();
        } catch (final Exception e) {
            return "<invalid-url>";
        }
    }

    private String safePort(final URI uri) {
        return uri.getPort() == -1 ? "" : ":" + uri.getPort();
    }

    private String sanitizedResponseBody(final String responseBody) {
        if (responseBody == null) {
            return "<empty>";
        }

        final String sanitizedBody = responseBody
            .replaceAll("(?i)\"access_token\"\\s*:\\s*\"[^\"]+\"", "\"access_token\":\"<redacted>\"")
            .replaceAll("(?i)\"token\"\\s*:\\s*\"[^\"]+\"", "\"token\":\"<redacted>\"")
            .replaceAll("(?i)\"password\"\\s*:\\s*\"[^\"]+\"", "\"password\":\"<redacted>\"");

        return sanitizedBody.length() <= MAX_RESPONSE_BODY_LOG_LENGTH
            ? sanitizedBody
            : sanitizedBody.substring(0, MAX_RESPONSE_BODY_LOG_LENGTH) + "...";
    }
}

