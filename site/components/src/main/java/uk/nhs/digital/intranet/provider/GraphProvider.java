package uk.nhs.digital.intranet.provider;

import org.springframework.web.client.RestTemplate;

public class GraphProvider {

    private static final String BASE_URL_V1 = "https://graph.microsoft.com/v1.0/";
    private static final String BASE_URL_BETA = "https://graph.microsoft.com/beta/";

    private final RestTemplate restTemplate;

    public GraphProvider(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


}
