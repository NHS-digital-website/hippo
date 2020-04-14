package uk.nhs.digital.apispecs.apigee.auth;

import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.RequestEnhancer;
import org.springframework.util.MultiValueMap;

import java.time.Clock;

public class ApigeeAccessTokenRequestOneTimePasswordEnhancer implements RequestEnhancer {

    private static final String TOKEN_REQUEST_FIELD_NAME_MFA_TOKEN = "mfa_token";
    private static final String HTTP_HEADER_NAME_AUTHORIZATION = "Authorization";


    private final IGoogleAuthenticator otpGenerator;

    private final String otpKey;
    private final String basicAuthToken;
    private final Clock clock;

    public ApigeeAccessTokenRequestOneTimePasswordEnhancer(final IGoogleAuthenticator otpGenerator,
                                                           final String otpKey,
                                                           final String basicAuthToken,
                                                           final Clock clock
    ) {
        this.otpKey = otpKey;
        this.otpGenerator = otpGenerator;
        this.basicAuthToken = basicAuthToken;
        this.clock = clock;

        ensureRequiredArgProvided("OTP key", otpKey);
        ensureRequiredArgProvided("basic auth token", basicAuthToken);
    }

    @Override
    public void enhance(
        final AccessTokenRequest request,
        final OAuth2ProtectedResourceDetails resource,
        final MultiValueMap<String, String> form,
        final HttpHeaders headers
    ) {
        final String authorisationHeaderValue = "Basic " + basicAuthToken;

        headers.add(HTTP_HEADER_NAME_AUTHORIZATION, authorisationHeaderValue);

        form.add(
            TOKEN_REQUEST_FIELD_NAME_MFA_TOKEN,
            oneTimePassword()
        );
    }

    private String oneTimePassword() {
        return String.valueOf(otpGenerator.getTotpPassword(otpKey, clock.millis()));
    }

    private void ensureRequiredArgProvided(final String argName, final String argValue) {
        Validate.notBlank(argValue, "Required configuration argument is missing: %s", argName);
    }
}
