package uk.nhs.digital.apispecs.services.auth;

import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.apache.commons.lang3.Validate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Map;

public class ApigeeOAuth2TokenProvider {

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basicAuthToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        body.add("mfa_token", oneTimePassword());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            tokenUri, HttpMethod.POST, entity, Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }

        throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
    }

    private String oneTimePassword() {
        return String.valueOf(otpGenerator.getTotpPassword(otpKey, clock.millis()));
    }

    private void ensureRequiredArgProvided(final String argName, final String argValue) {
        Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }
}


