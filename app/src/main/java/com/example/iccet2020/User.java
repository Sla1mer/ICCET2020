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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getBirhday() {
        return birhday;
    }

    public void setBirhday(String birhday) {
        this.birhday = birhday;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNomerOMS() {
        return nomerOMS;
    }

    public void setNomerOMS(String nomerOMS) {
        this.nomerOMS = nomerOMS;
    }
}
