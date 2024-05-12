package com.example.falldetective;

public class ContactInfoModel {

    String number;
    String name;

    private ContactInfoModel() {}

    public ContactInfoModel(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
