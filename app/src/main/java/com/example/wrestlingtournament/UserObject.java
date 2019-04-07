package com.example.wrestlingtournament;

/**
 * This class contains all of a users information.
 *
 * @author Team 02-01
 */
public class UserObject {

    private String email;
    private String firstName;
    private String lastName;
    private String userType;

    public UserObject() {
        this.email = "empty";
        this.firstName = "empty";
        this.lastName = "empty";
        this.userType = "empty";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
