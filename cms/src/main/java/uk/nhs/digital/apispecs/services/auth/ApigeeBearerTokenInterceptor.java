package uk.nhs.digital.apispecs.services.auth;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Collections;

public class ApigeeBearerTokenInterceptor implements ClientHttpRequestInterceptor {

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
        String accessToken = tokenProvider.getAccessToken(tokenUri, username, password);
        request.getHeaders().setBearerAuth(accessToken);
        request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return execution.execute(request, body);
    }
}
