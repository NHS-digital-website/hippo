package uk.nhs.digital.intranet.provider.stub;

import org.apache.commons.lang.StringUtils;
import uk.nhs.digital.intranet.factory.PersonFactory;
import uk.nhs.digital.intranet.json.User;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.GraphProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GraphProviderStub implements GraphProvider {

    private final PersonFactory personFactory = new PersonFactory();

    private static final User USER_1 = new User(Collections.singletonList("06666666666"), "John Doe",
        "Operations", "Manager", "john.doe@local.com", "05555555555", "Leeds", "jane.doe@domain.com", "2");
    private static final User USER_2 = new User(Collections.singletonList("04444444444"), "Jane Doe",
        "Director", "IT", "jane.doe@local.com", "07777777777", "Leeds", "john.doe@domain.com", "1");
    private static final User USER_3 = new User(Collections.singletonList("02222222222"), "Alice Bob",
        "Developer", "IT", "alice.bob@local.com", "03333333333", "Manchester", "alice.bob@domain.com", "3");
    private static final User USER_4 = new User(Collections.singletonList("01111111111"), "Richard Doe",
        "Developer", "IT", "rich.doe@local.com", "02222222222", "Manchester", "rich.doe@domain.com", "4");
    private static final User USER_5 = new User(Collections.singletonList("07777777777"), "Jake Bob",
        "Tester", "IT", "jake.bob@local.com", "08888888888", "Leeds", "jake.bob@domain.com", "5");
    private static final User USER_6 = new User(Collections.singletonList("09999999999"), "Sarah Jones",
        "Operations", "Manager", "sarah.jones@local.com", "0666666666", "Birmingham", "sarah.jones@domain.com", "6");
    private static final User USER_7 = new User(Collections.singletonList("03333333333"), "Albert Jones",
        "Director", "IT", "albery.jones@local.com", "01111111111", "Birmingham", "albert.jones@domain.com", "7");
    private static final List<User> USER = Arrays.asList(USER_1, USER_2, USER_3, USER_4, USER_5, USER_6, USER_7);

    @Override
    public List<Person> getPeople(final String searchTerm) {
        if (StringUtils.isBlank(searchTerm)) {
            return Collections.emptyList();
        }
        return personFactory.createPersons(
            USER
                .stream()
                .filter(user -> user.getDisplayName().toLowerCase().contains(searchTerm.toLowerCase()))
                .sorted((u1, u2) -> {
                    if (u1.getDisplayName().split(" ")[1].compareTo(u2.getDisplayName().split(" ")[1]) == 0) {
                        return u1.getDisplayName().split(" ")[0].compareTo(u2.getDisplayName().split(" ")[0]);
                    } else {
                        return u1.getDisplayName().split(" ")[1].compareTo(u2.getDisplayName().split(" ")[1]);
                    } 
                })
                .collect(Collectors.toList())
        );
    }

    @Override
    public Person getPerson(final String id) throws ProviderCommunicationException {
        return personFactory.createPerson(
            USER
                .stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst()
                .orElseThrow(ProviderCommunicationException::new), null
        );
    }
}
