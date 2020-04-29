package uk.nhs.digital.intranet.provider.stub;

import org.apache.commons.lang.StringUtils;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.GraphProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GraphProviderStub implements GraphProvider {

    private static final Person PERSON_1 = new Person("1", "John Doe", "john.doe@local.com",
        "Manager", "Operations", "Leeds", "07777777777", Collections.singletonList("06666666666"), null);
    private static final Person PERSON_2 = new Person("2", "Jane Doe", "jane.doe@local.com",
        "Director", "IT", "Leeds", "05555555555", Collections.singletonList("04444444444"), null);
    private static final Person PERSON_3 = new Person("3", "Alice Bob", "alice.bob@local.com",
        "Developer", "IT", "Manchester", "03333333333", Collections.singletonList("02222222222"), null);
    private static final Person PERSON_4 = new Person("4", "Richard Doe", "rich.doe@local.com",
        "Developer", "IT", "Manchester", "02222222222", Collections.singletonList("01111111111"), null);
    private static final List<Person> PEOPLE = Arrays.asList(PERSON_1, PERSON_2, PERSON_3, PERSON_4);


    @Override
    public List<Person> getPeople(final String searchTerm) {
        if (StringUtils.isBlank(searchTerm)) {
            return Collections.emptyList();
        }
        return PEOPLE
            .stream()
            .filter(person -> person.getDisplayName().toLowerCase().contains(searchTerm.toLowerCase()))
            .collect(Collectors.toList());
    }

    @Override
    public Person getPerson(final String id) throws ProviderCommunicationException {
        return PEOPLE
            .stream()
            .filter(person -> id.equals(person.getId()))
            .findFirst()
            .orElseThrow(ProviderCommunicationException::new);
    }
}
