package uk.nhs.digital.intranet.provider.impl;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.factory.PersonFactory;
import uk.nhs.digital.intranet.json.User;
import uk.nhs.digital.intranet.json.UserResponse;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.GraphProvider;
import uk.nhs.digital.intranet.utils.Constants;

import java.net.URI;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class GraphProviderImpl implements GraphProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphProviderImpl.class);
    private static final String BASE_URL_V1 = "https://graph.microsoft.com/v1.0/";
    private static final String BASE_URL_BETA = "https://graph.microsoft.com/beta/";
    private static final String SELECTED_FIELDS_SHORT = "displayName,department,id";
    private static final String SELECTED_FIELDS_LONG = "businessPhones,displayName,jobTitle,mail,mobilePhone,officeLocation,userPrincipalName,department,id";

    private final RestTemplate restTemplate;
    private final PersonFactory personFactory;

    public GraphProviderImpl(final RestTemplate restTemplate, final PersonFactory personFactory) {
        this.restTemplate = restTemplate;
        this.personFactory = personFactory;
    }

    @Override
    public List<Person> getPeople(final String searchTerm) throws ProviderCommunicationException {
        final HttpEntity<String> httpRequest = getHttpRequest(MediaType.APPLICATION_JSON);
        final String cleanSearchTerm = searchTerm.trim();
        final String filter = String.format("startsWith(displayName, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(givenName, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(surname, '%s')", cleanSearchTerm)
            + " or "
            + String.format("startsWith(mail, '%s')", cleanSearchTerm);

        final URI uri = UriComponentsBuilder.fromUriString(BASE_URL_V1)
            .pathSegment("users")
            .queryParam("$filter", filter)
            .queryParam("$select", SELECTED_FIELDS_SHORT)
            .build()
            .toUri();

        try {
            LOGGER.error("Value of URL is  {} ", uri);
            final ResponseEntity<UserResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, UserResponse.class);
            Assert.notNull(responseEntity.getBody(), "Received null response from Microsoft Graph API.");
            return personFactory.createPersons(responseEntity.getBody().getValue());
        } catch (final HttpStatusCodeException e) {
            LOGGER.error("Received exception with status {} from Microsoft Graph API.", e.getStatusCode(), e);
            throw new ProviderCommunicationException();
        }
    }

    @Override
    public Person getPerson(final String id) throws ProviderCommunicationException {
        final User user = getUser(id);
        final String photo = getPhoto(id);
        return personFactory.createPerson(user, photo);
    }

    private User getUser(final String id) throws ProviderCommunicationException {
        final HttpEntity<String> httpRequest = getHttpRequest(MediaType.APPLICATION_JSON);

        final URI uri = UriComponentsBuilder.fromUriString(BASE_URL_V1)
            .pathSegment("users")
            .pathSegment(id)
            .queryParam("$select", SELECTED_FIELDS_LONG)
            .build()
            .toUri();

        try {
            final ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, User.class);
            Assert.notNull(responseEntity.getBody(), "Received null response from Microsoft Graph API.");
            return responseEntity.getBody();
        } catch (final HttpStatusCodeException e) {
            LOGGER.error("Received exception with status {} from Microsoft Graph API.", e.getStatusCode(), e);
            throw new ProviderCommunicationException();
        }
    }

    private String getPhoto(final String id) {
        final HttpEntity<String> httpRequest = getHttpRequest(MediaType.APPLICATION_OCTET_STREAM);

        final URI uri = UriComponentsBuilder.fromUriString(BASE_URL_BETA)
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
        headers.setAccept(Collections.singletonList(acceptMediaType));
        headers.setBearerAuth((String) context.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME));
        return new HttpEntity<>(headers);
    }
}
