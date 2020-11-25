package com.example.iccet2020;

public class User {
    public String lastname, firstname, middlename, birhday, snils, email, phone, token;

    public User(String lastname, String firstname, String middlename, String birhday, String snils, String email, String phone, String token) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.birhday = birhday;
        this.snils = snils;
        this.email = email;
        this.phone = phone;
        this.token = token;
    }

    public User(){

    }
}
