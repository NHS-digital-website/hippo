package uk.nhs.digital.intranet.provider;

import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;

import java.util.List;

public interface GraphProvider {

    List<Person> getPeople(final String searchTerm) throws ProviderCommunicationException;

    Person getPerson(final String id) throws ProviderCommunicationException;
}
