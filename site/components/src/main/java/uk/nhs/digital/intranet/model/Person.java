package uk.nhs.digital.intranet.model;

import uk.nhs.digital.intranet.enums.SearchResultType;

import java.util.List;

public class Person {

    private String id;
    private String displayName;
    private String email;
    private String jobRole;
    private String department;
    private String officeLocation;
    private String phoneNumber;
    private List<String> businessPhones;
    private String photo;

    public Person(String id, String displayName, String department) {
        this.id = id;
        this.displayName = displayName;
        this.department = department;
    }

    public Person(String id, String displayName, String email, String jobRole, String department,
                  String officeLocation, String phoneNumber, List<String> businessPhones, String photo) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.jobRole = jobRole;
        this.department = department;
        this.officeLocation = officeLocation;
        this.phoneNumber = phoneNumber;
        this.businessPhones = businessPhones;
        this.photo = photo;
    }

    public String getDisplayName() {
        return displayName;
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

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }
}
