package com.example.iccet2020;

public class User {
    public String lastname, firstname, middlename, birhday, snils, email, phone, seriaOMS, nomerOMS;

    public User(String lastname, String firstname, String middlename, String birhday, String snils,
                String email, String phone, String seriaOMS, String nomerOMS) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.birhday = birhday;
        this.snils = snils;
        this.email = email;
        this.phone = phone;
        this.seriaOMS = seriaOMS;
        this.nomerOMS = nomerOMS;
    }

    public User(){

    }
}
