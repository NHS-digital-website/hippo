package uk.nhs.digital.intranet.factory;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import org.junit.Test;
import uk.nhs.digital.intranet.json.User;
import uk.nhs.digital.intranet.model.Person;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class PersonFactoryTest {

    private static final String DEPT = "IT";
    private static final String BUSINESS_PHONE = "07777777777";
    private static final String ID = "12345";
    private static final String JOB = "Developer";
    private static final String MAIL_FORMAT = "%s@company.uk";
    private static final String MOBILE_PHONE = "05555555555";
    private static final String OFFICE = "UK";
    private static final String USER_PRINCIPAL_NAME_FORMAT = "%s.user.principal";
    private static final String PHOTO = "base-64-photo";
    private final PersonFactory personFactory = new PersonFactory();

    @Test
    public void createsPersonFromUserAndPhoto() {
        final String userName = "user 1";
        final String givenName = "user";
        final String surname = "1";
        final User user = getUser(userName, givenName, surname, String.format(MAIL_FORMAT, userName));

        final Person person = personFactory.createPerson(user, PHOTO);

        assertEquals(user.getDisplayName(), person.getDisplayName());
        assertEquals(user.getGivenName(), person.getGivenName());
        assertEquals(user.getSurname(), person.getSurname());
        assertEquals(user.getBusinessPhones(), person.getBusinessPhones());
        assertEquals(user.getDepartment(), person.getDepartment());
        assertEquals(user.getJobTitle(), person.getJobRole());
        assertEquals(user.getMail(), person.getEmail());
        assertEquals(user.getMobilePhone(), person.getPhoneNumber());
        assertEquals(user.getId(), person.getId());
        assertEquals(PHOTO, person.getPhoto());
    }

    @Test
    public void createsPersonFromUserAndPhotoUsingUserPrincipalNameAsMail() {
        final String userName = "user1";
        final String givenName = "user";
        final String surname = "1";
        final User user = getUser(userName, givenName, surname, null);

        final Person person = personFactory.createPerson(user, PHOTO);

        assertEquals(user.getDisplayName(), person.getDisplayName());
        assertEquals(user.getGivenName(), person.getGivenName());
        assertEquals(user.getSurname(), person.getSurname());
        assertEquals(user.getBusinessPhones(), person.getBusinessPhones());
        assertEquals(user.getDepartment(), person.getDepartment());
        assertEquals(user.getJobTitle(), person.getJobRole());
        assertEquals(user.getUserPrincipalName(), person.getEmail());
        assertEquals(user.getMobilePhone(), person.getPhoneNumber());
        assertEquals(user.getId(), person.getId());
        assertEquals(PHOTO, person.getPhoto());
    }

    @Test
    public void createsPersonListFromUserList() {
        final User user1 = getUser("user a", "user", "a", null);
        final User user2 = getUser("user b", "user", "b", null);
        final User user3 = getUser("user c", "user", "c", null);
        final List<User> users = Arrays.asList(user1, user2, user3);

        final List<Person> persons = personFactory.createPersons(users);

        assertEquals(3, persons.size());
        IntStream.range(0, 3).forEach(index -> {
            assertEquals(users.get(index).getDisplayName(), persons.get(index).getDisplayName());
            assertEquals(users.get(index).getGivenName(), persons.get(index).getGivenName());
            assertEquals(users.get(index).getSurname(), persons.get(index).getSurname());
            assertEquals(users.get(index).getDepartment(), persons.get(index).getDepartment());
            assertEquals(users.get(index).getId(), persons.get(index).getId());
            assertNull(persons.get(index).getBusinessPhones());
            assertNull(persons.get(index).getEmail());
            assertNull(persons.get(index).getPhoneNumber());
            assertNull(persons.get(index).getJobRole());
            assertNull(persons.get(index).getOfficeLocation());
            assertNull(persons.get(index).getPhoto());
        });
    }

    @Test
    public void createsPersonListFromUserListFiltersNonUsersWithCaps() {
        final User user1 = getUser("user A", "user", "A", null);
        final User user2 = getUser("user a", "user", "a", null);
        final User user3 = getUser("user b", "user", "b", null);
        final User user4 = getUser("B, User", "user", "B", null);
        final User user5 = getUser("user C", "user", "C", null);
        final List<User> users = Arrays.asList(user1, user2, user3, user4, user5);

        final List<Person> persons = personFactory.createPersons(users);

        assertEquals(2, persons.size());
        assertEquals(user2.getDisplayName(), persons.get(0).getDisplayName());
        assertEquals(user3.getDisplayName(), persons.get(1).getDisplayName());
    }

    private User getUser(final String displayName, final String givenName, 
                         final String surname, String email) {
        final User user = new User();
        user.setDisplayName(displayName);
        user.setGivenName(givenName);
        user.setSurname(surname);
        user.setDepartment(DEPT);
        user.setBusinessPhones(Collections.singletonList(BUSINESS_PHONE));
        user.setId(ID);
        user.setJobTitle(JOB);
        user.setMail(email);
        user.setMobilePhone(MOBILE_PHONE);
        user.setOfficeLocation(OFFICE);
        user.setUserPrincipalName(String.format(USER_PRINCIPAL_NAME_FORMAT, displayName));
        return user;
    }
}
