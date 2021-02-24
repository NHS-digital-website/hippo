package uk.nhs.digital.intranet.model;

import uk.nhs.digital.intranet.enums.SearchResultType;

import java.util.List;

public class Person {

    private String id;
    private String displayName;
    private String givenName;
    private String surname;
    private String email;
    private String jobRole;
    private String department;
    private String officeLocation;
    private String phoneNumber;
    private List<String> businessPhones;
    private String photo;

    public Person(String id, String displayName, String givenName, String surname, String department, String email) {
        this.id = id;
        this.displayName = displayName;
        this.givenName = givenName;
        this.surname = surname;
        this.department = department;
        this.email = email;
    }

    public Person(String id, String displayName, String givenName, String surname, String email,
                  String jobRole, String department, String officeLocation, String phoneNumber,
                  List<String> businessPhones, String photo) {
        this.id = id;
        this.displayName = displayName;
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.jobRole = jobRole;
        this.department = department;
        this.officeLocation = officeLocation;
        this.phoneNumber = phoneNumber;
        this.businessPhones = businessPhones;
        this.photo = photo;
    }

    public String getDisplayName() {
        return displayName
            + " s="
            + surname
            + " g="
            + givenName
            + " e="
            + email;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getSurname() {
        return surname;
    }

    public String getType() {
        return SearchResultType.PERSON.getValue();
    }

    public String getEmail() {
        return email;
    }

    public String getJobRole() {
        return jobRole;
    }

    public String getDepartment() {
        return department;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getBusinessPhones() {
        return businessPhones;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }
}
