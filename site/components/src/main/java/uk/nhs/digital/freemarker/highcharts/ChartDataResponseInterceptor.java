package uk.nhs.digital.freemarker.highcharts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import uk.nhs.digital.crisp.TextResponseWrapper;

import java.io.IOException;
import java.util.Base64;

public class ChartDataResponseInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChartDataResponseInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        LOGGER.debug(" Inside ChartDataResponseInterceptor " + request.getURI());
        return new TextResponseWrapper(execution.execute(request, body), (String b) -> String.format("{ \"data\" : \"%s\" }", Base64.getEncoder().encodeToString(b.getBytes())));
    }
}
