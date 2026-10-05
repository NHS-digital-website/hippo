package uk.nhs.digital.apispecs.services.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

public class ApigeeBearerTokenInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ApigeeBearerTokenInterceptor.class);

    private final ApigeeOAuth2TokenProvider tokenProvider;
    private final String tokenUri;
    private final String username;
    private final String password;

    public ApigeeBearerTokenInterceptor(ApigeeOAuth2TokenProvider tokenProvider, String tokenUri,
                                        String username, String password) {
        this.tokenProvider = tokenProvider;
        this.tokenUri = tokenUri;
        this.username = username;
        this.password = password;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        log.debug(
            "Preparing authenticated Apigee request; target: {}; token endpoint: {}.",
            safeUri(request.getURI()),
            safeUri(tokenUri)
        );

        try {
            String accessToken = tokenProvider.getAccessToken(tokenUri, username, password);
            log.debug("Apigee access token acquired; target: {}.", safeUri(request.getURI()));

            request.getHeaders().setBearerAuth(accessToken);
            request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            final ClientHttpResponse response = execution.execute(request, body);

            log.debug(
                "Authenticated Apigee request completed; target: {}; status: {}.",
                safeUri(request.getURI()),
                response.getStatusCode()
            );

            return response;
        } catch (final IOException e) {
            log.error("Authenticated Apigee request failed; target: {}.", safeUri(request.getURI()), e);
            throw e;
        } catch (final RuntimeException e) {
            log.error("Authenticated Apigee request failed; target: {}.", safeUri(request.getURI()), e);
            throw e;
        }
    }

    private String safeUri(final String url) {
        try {
            return safeUri(URI.create(url));
        } catch (final Exception e) {
            return "<invalid-url>";
        }
    }

    private String safeUri(final URI uri) {
        try {
            return uri.getScheme() + "://" + uri.getHost() + safePort(uri) + uri.getPath();
        } catch (final Exception e) {
            return "<invalid-url>";
        }
    }

    private String safePort(final URI uri) {
        return uri.getPort() == -1 ? "" : ":" + uri.getPort();
    }
}
