package uk.nhs.digital.intranet.provider.impl;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.json.BatchResponse;
import uk.nhs.digital.intranet.json.Response;
import uk.nhs.digital.intranet.json.User;
import uk.nhs.digital.intranet.json.UserResponse;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.GraphProvider;
import uk.nhs.digital.intranet.utils.Constants;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class GraphProviderImpl implements GraphProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphProviderImpl.class);
    private static final String BASE_URL = "https://graph.microsoft.com/v1.0/";
    private static final String SELECTED_FIELDS_LONG = "businessPhones,displayName,jobTitle,mail,mobilePhone,officeLocation,userPrincipalName,department,id,odata.count";

    private final RestTemplate restTemplate;

    public GraphProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable("getUsers")
    public UserResponse getUsers(final String searchTerm, int limit) {
        final HttpEntity<String> httpRequest = getHttpRequest(MediaType.APPLICATION_JSON);
        final String cleanSearchTerm = searchTerm.trim();
        final String filter = String.format("("
            + "startsWith(displayName, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(givenName, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(surname, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(mail, '%s')", cleanSearchTerm)
            + ")"
            + " and (NOT(showInAddressList eq false) and NOT(companyName eq null))";

        final URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
            .pathSegment("users")
            .queryParam("$filter", filter)
            .queryParam("$select", SELECTED_FIELDS_LONG)
            .queryParam("$count", true)
            .queryParam("$top", limit)
            .queryParam("$orderby", "displayName")
            .build()
            .toUri();

        try {
            final ResponseEntity<UserResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, UserResponse.class);
            Assert.notNull(responseEntity.getBody(), "Received null response from Microsoft Graph API.");
            return responseEntity.getBody();
        } catch (final HttpStatusCodeException e) {
            LOGGER.error("Received exception with status {} from Microsoft Graph API.", e.getStatusCode(), e);
            return null;
        }
    }

    public List<Person> fetchPhotos(List<Person> people) {
        ArrayList<JSONObject> requests = new ArrayList<>();
        IntStream.range(0, people.size()).forEach(index -> {
            Person person = people.get(index);
            JSONObject request = new JSONObject();
            request.put("id", String.valueOf(index));
            request.put("method", "GET");
            request.put("url", "/users/" + person.getId() + "/photos/432x432/$value");
            requests.add(request);
        });

        List<Response> responses = batchRequests(requests);
        responses.forEach(response -> {
            if (response.getStatus() == 200) {
                int personIndex = Integer.parseInt(response.getId());
                Person person = people.get(personIndex);
                person.setPhoto(response.getBody());
            }
        });

        return people;
    }

    private List<Response> batchRequests(List<JSONObject> requests) {
        List<Response> responses = new ArrayList<>();

        // https://docs.microsoft.com/en-us/graph/json-batching
        int batchLimit = 20;
        int batches = (int) Math.ceil((double) requests.size() / (double) batchLimit);
        for (int i = 0; i < batches; i++) {
            int batchStart = batches * i;
            int batchEnd = batchStart + batchLimit;
            if (batchEnd > requests.size()) {
                batchEnd = requests.size();
            }
            List<JSONObject> batchRequestsList = requests.subList(batchStart, batchEnd);
            JSONArray batchRequests = new JSONArray();
            batchRequests.addAll(batchRequestsList);

            JSONObject body = new JSONObject();
            body.put("requests", batchRequests);

            final URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .pathSegment("$batch")
                .build()
                .toUri();

            try {
                final ResponseEntity<BatchResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, getHttpPostRequest(body), BatchResponse.class);
                BatchResponse batchResponse = responseEntity.getBody();
                assert batchResponse != null;

                responses.addAll(batchResponse.getResponses());
            } catch (final HttpStatusCodeException e) {
                LOGGER.error("Received exception with status {} from Microsoft Graph API.", e.getStatusCode(), e);
            }
        }

        return responses;
    }

    @Override
    public User getUser(final String id) throws ProviderCommunicationException {
        final HttpEntity<String> httpRequest = getHttpRequest(MediaType.APPLICATION_JSON);

        final URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
            .pathSegment("users")
            .pathSegment(String.valueOf(id))
            .queryParam("$select", SELECTED_FIELDS_LONG)
            .build()
            .toUri();

        try {
            final ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, User.class);
            Assert.notNull(responseEntity.getBody(), "Received null response from Microsoft Graph API.");
            return responseEntity.getBody();
        } catch (final HttpStatusCodeException e) {
            LOGGER.error("Received exception with status {} from Microsoft Graph API.", e.getStatusCode(), e);
            return null;
        }
    }

    public String getPhoto(final String id) {
        final HttpEntity<String> httpRequest = getHttpRequest(MediaType.APPLICATION_OCTET_STREAM);

        final URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
            .pathSegment("users", id, "photo", "$value")
            .build()
            .toUri();

        try {
            final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, byte[].class);
            Assert.notNull(responseEntity.getBody(), "Received null response from Microsoft Graph API.");
            return new String(Base64.getEncoder().encode(responseEntity.getBody()));
        } catch (final HttpStatusCodeException e) {
            LOGGER.error("Received exception with status {} from Microsoft Graph API.", e.getStatusCode(), e);
            return null;
        }
    }

    private HttpEntity<String> getHttpRequest(final MediaType acceptMediaType) {
        final HttpHeaders headers = new HttpHeaders();
        final HstRequestContext context = RequestContextProvider.get();
        headers.setBearerAuth((String) context.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME));
        headers.setAccept(Collections.singletonList(acceptMediaType));
        headers.set("ConsistencyLevel", "eventual");
        return new HttpEntity<>(headers);
    }

    private HttpEntity<JSONObject> getHttpPostRequest(final JSONObject body) {
        final HttpHeaders headers = new HttpHeaders();
        final HstRequestContext context = RequestContextProvider.get();
        headers.setBearerAuth((String) context.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
