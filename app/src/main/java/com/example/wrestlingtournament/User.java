package com.example.wrestlingtournament;

public class User {

    private String _email;
    private String _firstName;
    private String _lastName;
    private String _password;
    private int _id;

    User() {

    }

    public User login() {
        User databaseReturnedUserType = new User();
        return databaseReturnedUserType;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String email) {
        _email = email;
    }

    public String get_firstName() {
        return _firstName;
    }

    void set_firstName(String name) {
        _firstName = name;
    }

    public String get_lastName() {
        return _lastName;
    }

    void set_lastName(String name) {
        _lastName = name;
    }
}
