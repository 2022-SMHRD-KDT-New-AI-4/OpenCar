package com.lmj.opencar.Drive;

public class InfoVO {
    private String day;
    private String hour;
    private String check;
    private String freq;

    public InfoVO(String day, String hour, String check, String freq) {
        this.day = day;
        this.hour = hour;
        this.check = check;
        this.freq = freq;
    }

    public InfoVO() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }
}
