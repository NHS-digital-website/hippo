package uk.nhs.digital.intranet.provider;

import uk.nhs.digital.intranet.json.User;
import uk.nhs.digital.intranet.json.UserResponse;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;

import java.util.List;

public interface GraphProvider {

    User getUser(final String id) throws ProviderCommunicationException;

    UserResponse getUsers(final String searchTerm, int limit);

    String getPhoto(final String id);

    List<Person> fetchPhotos(List<Person> people);
}
