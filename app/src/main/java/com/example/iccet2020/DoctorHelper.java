package com.example.iccet2020;

public class DoctorHelper {
    private ZapicDoctor zapicDoctor;
    private String key;

    public DoctorHelper() {
    }

    public DoctorHelper(ZapicDoctor zapicDoctor, String key) {
        this.zapicDoctor = zapicDoctor;
        this.key = key;
    }

    public ZapicDoctor getZapicDoctor() {
        return zapicDoctor;
    }

    public void setZapicDoctor(ZapicDoctor zapicDoctor) {
        this.zapicDoctor = zapicDoctor;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
