package com.assessment.orangehrm.data;

public class EmployeeData {
    private String firstName;
    private String lastName;
    private String employeeIdPrefix;
    private String jobTitle;
    private String employmentStatus;
    private String profilePicture;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmployeeIdPrefix() { return employeeIdPrefix; }
    public void setEmployeeIdPrefix(String employeeIdPrefix) { this.employeeIdPrefix = employeeIdPrefix; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}
