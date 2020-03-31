package uk.nhs.digital.intranet.model;

import uk.nhs.digital.intranet.enums.SearchResultType;

public class Person implements SearchResult {

    private String displayName;
    private String email;
    private String jobRole;
    private String department;
    private String officeLocation;
    private String phoneNumber;
    private String photo;
    private String id;

    public Person(String displayName, String department, String id) {
        this.displayName = displayName;
        this.department = department;
        this.id = id;
    }

    public Person(String displayName, String email, String jobRole, String department, String officeLocation, String phoneNumber, String photo, String id) {
        this.displayName = displayName;
        this.email = email;
        this.jobRole = jobRole;
        this.department = department;
        this.officeLocation = officeLocation;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.id = id;
    }

    @Override
    public String getTitle() {
        return displayName;
    }

    @Override
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

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }
}
