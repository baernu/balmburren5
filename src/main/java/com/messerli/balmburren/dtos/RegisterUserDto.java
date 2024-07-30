package com.messerli.balmburren.dtos;

public class RegisterUserDto {
    private String username;
    private String password;
    private String firstname;
    private String lastname;

    public String getUsername() {
        return username;
    }

    public RegisterUserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public RegisterUserDto setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public RegisterUserDto setLastname(String lastName) {
        this.lastname = lastName;
        return this;
    }


    @Override
    public String toString() {
        return "RegisterUserDto{" +
                "email='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

}
