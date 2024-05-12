package com.example.falldetective;

import java.util.List;

public class UserModel {

    String fullName;
    String email;
    String gender;
    String birthDate;
    List<ContactInfoModel> contacts;

    private UserModel() {}


    public void setContacts(List<ContactInfoModel> contacts) {
        this.contacts = contacts;
    }

    public UserModel(String fullName, String email, String gender, String birthDate, List<ContactInfoModel> contacts) {
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.contacts = contacts;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public List<ContactInfoModel> getContacts() {
        return contacts;
    }
}
