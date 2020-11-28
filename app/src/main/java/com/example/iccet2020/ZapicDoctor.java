package com.example.iccet2020;

public class ZapicDoctor {
    private String lastname, name, middlename, birthday, snils, email, phone,
    seriaOMS, nomerOMS, doctor, data, time;

    public ZapicDoctor(String lastname, String name, String middlename, String birthday, String snils, String email, String phone, String seriaOMS, String nomerOMS, String doctor, String data, String time) {
        this.lastname = lastname;
        this.name = name;
        this.middlename = middlename;
        this.birthday = birthday;
        this.snils = snils;
        this.email = email;
        this.phone = phone;
        this.seriaOMS = seriaOMS;
        this.nomerOMS = nomerOMS;
        this.doctor = doctor;
        this.data = data;
        this.time = time;
    }

    public ZapicDoctor() {
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
