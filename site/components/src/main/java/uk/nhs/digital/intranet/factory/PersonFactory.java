package uk.nhs.digital.intranet.factory;

import org.springframework.util.StringUtils;
import uk.nhs.digital.intranet.json.User;
import uk.nhs.digital.intranet.model.Person;

import java.util.List;
import java.util.stream.Collectors;

public class PersonFactory {

    public List<Person> createPersons(final List<User> userList) {
        return userList
            .stream()
            .sorted((u1, u2) -> {
                if (u1.getSurname().compareTo(u2.getSurname()) == 0) {
                    return u1.getGivenName().compareTo(u2.getGivenName());
                } else {
                    return u1.getSurname().compareTo(u2.getSurname());
                } 
            })
            .map(this::createPerson)
            .collect(Collectors.toList());
    }

    private Person createPerson(final User user) {
        return new Person(
            user.getId(),
            user.getDisplayName(),
            user.getGivenName(),
            user.getSurname(),
            user.getDepartment()
        );
    }

    public Person createPerson(final User user, final String photo) {
        return new Person(
            user.getId(),
            user.getDisplayName(),
            user.getGivenName(),
            user.getSurname(),
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
