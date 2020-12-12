package com.example.iccet2020;

public class CalculatingTimeSr {
    private String time;
    private String date;

    public CalculatingTimeSr(String date, String time) {
        this.time = time;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
