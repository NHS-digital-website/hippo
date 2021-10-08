package uk.nhs.digital.intranet.factory;

import org.springframework.util.StringUtils;
import uk.nhs.digital.intranet.json.User;
import uk.nhs.digital.intranet.json.UserResponse;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.GraphProvider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersonFactory {
    private final GraphProvider graphProvider;

    public PersonFactory(final GraphProvider graphProvider) {
        this.graphProvider = graphProvider;
    }

    public Person fetchPerson(final String id) throws ProviderCommunicationException {
        User user = this.graphProvider.getUser(id);
        return this.createPerson(user);
    }

    public List<Person> fetchPeople(final String searchTerm, int limit) {
        UserResponse userResponse = this.graphProvider.getUsers(searchTerm, limit);
        if (userResponse != null) {
            return createPersons(userResponse.getValue());
        }
        return Collections.emptyList();
    }

    public List<Person> fetchPhotos(List<Person> people) {
        return this.graphProvider.fetchPhotos(people);
    }

    public List<Person> createPersons(final List<User> userList) {
        return userList
            .stream()
            .map(this::createPerson)
            .collect(Collectors.toList());
    }

    private Person createPerson(final User user) {
        return new Person(
            user.getId(),
            user.getDisplayName(),
            StringUtils.hasText(user.getMail()) ? user.getMail() : user.getUserPrincipalName(),
            user.getJobTitle(),
            user.getDepartment(),
            user.getOfficeLocation(),
            user.getMobilePhone(),
            user.getBusinessPhones(),
            null
        );
    }

    public Person createPerson(final User user, final String photo) {
        return new Person(
            user.getId(),
            user.getDisplayName(),
            StringUtils.hasText(user.getMail()) ? user.getMail() : user.getUserPrincipalName(),
            user.getJobTitle(),
            user.getDepartment(),
            user.getOfficeLocation(),
            user.getMobilePhone(),
            user.getBusinessPhones(),
            photo
        );
    }


}
