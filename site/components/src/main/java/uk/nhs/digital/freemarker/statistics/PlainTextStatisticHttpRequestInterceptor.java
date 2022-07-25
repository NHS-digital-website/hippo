package uk.nhs.digital.freemarker.statistics;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import uk.nhs.digital.crisp.TextResponseWrapper;

import java.io.IOException;

public class PlainTextStatisticHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public @NotNull ClientHttpResponse intercept(@NotNull HttpRequest request, @NotNull byte [] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        MediaType contentType = execution.execute(request, body).getHeaders().getContentType();
        if (contentType != null && contentType.getType().equalsIgnoreCase("text")) {
            return new TextResponseWrapper(response, (String number) -> String.format("{ \"number\" : \"%s\" }", number));
        }
        return response;
    }
}
