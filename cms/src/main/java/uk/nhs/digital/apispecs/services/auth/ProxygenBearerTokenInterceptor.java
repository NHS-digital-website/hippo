package uk.nhs.digital.apispecs.services.auth;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ProxygenBearerTokenInterceptor implements ClientHttpRequestInterceptor {

    private final ProxygenJwtTokenProvider tokenProvider;
    private final String tokenUri;

    public ProxygenBearerTokenInterceptor(ProxygenJwtTokenProvider tokenProvider, String tokenUri) {
        this.tokenProvider = tokenProvider;
        this.tokenUri = tokenUri;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        String accessToken = tokenProvider.getAccessToken(tokenUri);
        request.getHeaders().setBearerAuth(accessToken);
        return execution.execute(request, body);
    }

}

