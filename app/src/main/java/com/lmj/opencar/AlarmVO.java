package com.lmj.opencar;

public class AlarmVO {
    private String msg;
    private String time;
    private int imgRes;

    public AlarmVO(int imgRes,String msg, String time) {
        this.msg = msg;
        this.time = time;
        this.imgRes = imgRes;
    }

    public AlarmVO(String msg, String time) {
        this.msg = msg;
        this.time = time;
    }

    public AlarmVO() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}