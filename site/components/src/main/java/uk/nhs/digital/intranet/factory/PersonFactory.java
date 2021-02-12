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
            .map(this::createPerson)
            .sorted((p1, p2) -> {
                if (p1.getDisplayName().split(" ")[1].compareTo(p2.getDisplayName().split(" ")[1]) == 0) {
                    return p1.getDisplayName().split(" ")[0].compareTo(p2.getDisplayName().split(" ")[0]);
                } else {
                    return p1.getDisplayName().split(" ")[1].compareTo(p2.getDisplayName().split(" ")[1]);
                } 
            })
            .collect(Collectors.toList());
    }

    private Person createPerson(final User user) {
        return new Person(
            user.getId(),
            user.getDisplayName(),
            user.getDepartment()
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
