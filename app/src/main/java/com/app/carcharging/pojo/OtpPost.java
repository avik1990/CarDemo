package com.app.carcharging.pojo;

public class OtpPost {

    String Userid;
    String otpstatus;
    String contactno;
    String password;
    String latitude;
    String longitude;

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getOtpstatus() {
        return otpstatus;
    }

    public void setOtpstatus(String otpstatus) {
        this.otpstatus = otpstatus;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
