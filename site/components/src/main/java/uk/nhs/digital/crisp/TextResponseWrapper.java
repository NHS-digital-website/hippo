package uk.nhs.digital.crisp;

import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class TextResponseWrapper implements ClientHttpResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextResponseWrapper.class);
    private final byte[] jsonContent;
    private ByteArrayInputStream responseInputStream;
    private final ClientHttpResponse originalResponse;

    public TextResponseWrapper(ClientHttpResponse originalResponse, Function<String, String> transformer) {
        this.originalResponse = originalResponse;
        LOGGER.debug("Value of originalResponse  " + originalResponse);
        try {
            jsonContent = transformer.apply(CharStreams.toString(new InputStreamReader(originalResponse.getBody(), StandardCharsets.UTF_8))).getBytes();
            LOGGER.debug("Value of JSON content " + jsonContent);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem reading/decoding the response coming from the service ", e);
        }
    }

    @Override
    public HttpStatusCode getStatusCode() throws IOException {
        return originalResponse.getStatusCode();
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return originalResponse.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return originalResponse.getStatusText();
    }

    @Override
    public void close() {
        if (responseInputStream != null) {
            try {
                responseInputStream.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close the InputStream", e);
            }
        }
    }

    @Override
    public InputStream getBody() {
        if (responseInputStream == null) {
            responseInputStream = new ByteArrayInputStream(jsonContent);
        }
        return responseInputStream;
    }

    @Override
    public HttpHeaders getHeaders() {
        return originalResponse.getHeaders();
    }
}
