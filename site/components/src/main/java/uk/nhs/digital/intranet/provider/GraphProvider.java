package uk.nhs.digital.intranet.provider;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.json.UserResponse;
import uk.nhs.digital.intranet.model.SearchResult;
import uk.nhs.digital.intranet.utils.Constants;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class GraphProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphProvider.class);
    private static final String BASE_URL_V1 = "https://graph.microsoft.com/v1.0/";
    // private static final String BASE_URL_BETA = "https://graph.microsoft.com/beta/";
    private static final String SELECTED_FIELDS_SHORT = "displayName,department";

    private final RestTemplate restTemplate;

    public GraphProvider(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SearchResult> getPeople(final String searchTerm) {
        final String cleanSearchTerm = searchTerm.trim();
        final String filter = String.format("startsWith(displayName, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(givenName, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(surname, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(mail, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(userPrincipalName, '%s')", cleanSearchTerm);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HstRequestContext context = RequestContextProvider.get();
        headers.setBearerAuth((String) context.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME));

        HttpEntity<String> httpRequest = new HttpEntity<>(headers);

        final URI uri = UriComponentsBuilder.fromUriString(BASE_URL_V1 + "users")
            .queryParam("$filter", filter)
            .queryParam("$select", SELECTED_FIELDS_SHORT)
            .build()
            .toUri();

        ResponseEntity<UserResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, UserResponse.class);

        LOGGER.info("Received response {}", responseEntity.getStatusCode().toString());
        return Collections.emptyList();
    }

}
