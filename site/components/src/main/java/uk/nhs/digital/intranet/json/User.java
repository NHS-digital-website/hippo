package uk.nhs.digital.intranet.json;

import java.util.List;

public class User {

    private List<String> businessPhones;
    private String displayName;
    private String givenName;
    private String surname;
    private String department;
    private String jobTitle;
    private String mail;
    private String mobilePhone;
    private String officeLocation;
    private String userPrincipalName;
    private String id;

    public User() {}

    public User(List<String> businessPhones, String displayName, String givenName, String surname,
                String department, String jobTitle, String mail, String mobilePhone,
                String officeLocation, String userPrincipalName, String id) {
        this.businessPhones = businessPhones;
        this.displayName = displayName;
        this.givenName = givenName;
        this.surname = surname;
        this.department = department;
        this.jobTitle = jobTitle;
        this.mail = mail;
        this.mobilePhone = mobilePhone;
        this.officeLocation = officeLocation;
        this.userPrincipalName = userPrincipalName;
        this.id = id;
    }

    public List<String> getBusinessPhones() {
        return businessPhones;
    }

    public void setBusinessPhones(List<String> businessPhones) {
        this.businessPhones = businessPhones;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{"
            + "businessPhones=" + businessPhones
            + ", displayName='" + displayName + '\''
            + ", givenName='" + givenName + '\''
            + ", surname='" + surname + '\''
            + ", department='" + department + '\''
            + ", jobTitle='" + jobTitle + '\''
            + ", mail='" + mail + '\''
            + ", mobilePhone='" + mobilePhone + '\''
            + ", officeLocation='" + officeLocation + '\''
            + ", userPrincipalName='" + userPrincipalName + '\''
            + ", id='" + id + '\''
            + '}';
    }
}
