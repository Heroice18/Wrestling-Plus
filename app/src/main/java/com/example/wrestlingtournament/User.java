package com.example.wrestlingtournament;

public class User {

    private String _email;
    private String _name;
    private String _password;
    private int _id;

    public static void User() {

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

    public String get_name() {
        return _name;
    }

    public void set_name(String name) {
        _name = name;
    }


}
