package uk.nhs.digital.freemarker.statistics;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class PlainTextStatisticHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        if (execution.execute(request, body).getHeaders().getContentType().getType().equalsIgnoreCase("text")) {
            return new TextResponseWrapper(response);
        }
        return response;
    }
}
