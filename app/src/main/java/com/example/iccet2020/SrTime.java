package com.example.iccet2020;

public class SrTime {
    private String time;
    private String count;

    public SrTime(String time, String count) {
        this.time = time;
        this.count = count;
    }

    public SrTime() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
