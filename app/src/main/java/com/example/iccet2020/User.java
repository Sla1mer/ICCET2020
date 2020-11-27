package com.example.iccet2020;

public class User {
    private String birhday, email, firstname, lastname, middlename, nomerOMS,
            phone, seriaOMS, snils;

    public User(){

    }
    public User(String birhday, String email, String firstname, String lastname, String middlename, String nomerOMS, String phone, String seriaOMS, String snils) {
        this.birhday = birhday;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.nomerOMS = nomerOMS;
        this.phone = phone;
        this.seriaOMS = seriaOMS;
        this.snils = snils;
    }

    public String getBirhday() {
        return birhday;
    }

    public void setBirhday(String birhday) {
        this.birhday = birhday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getNomerOMS() {
        return nomerOMS;
    }

    public void setNomerOMS(String nomerOMS) {
        this.nomerOMS = nomerOMS;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSeriaOMS() {
        return seriaOMS;
    }

    public void setSeriaOMS(String seriaOMS) {
        this.seriaOMS = seriaOMS;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }
}
