package com.app.carcharging.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("message")
    @Expose
    public String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Datum {
        @SerializedName("userid")
        @Expose
        public String userid;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("emailid")
        @Expose
        public String emailid;
        @SerializedName("contactno")
        @Expose
        public String contactno;
        @SerializedName("ewel")
        @Expose
        public String ewel;
        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("country")
        @Expose
        public String country;
        @SerializedName("documentname")
        @Expose
        public String documentname;
        @SerializedName("documentno")
        @Expose
        public String documentno;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("profilepicture")
        @Expose
        public String profilepicture;
        @SerializedName("activestatus")
        @Expose
        public String activestatus;
        @SerializedName("roleid")
        @Expose
        public String roleid;
        @SerializedName("otpstatus")
        @Expose
        public String otpstatus;
        @SerializedName("otp")
        @Expose
        public String otp;
        @SerializedName("otptime")
        @Expose
        public String otptime;
        @SerializedName("latitude")
        @Expose
        public String latitude;
        @SerializedName("longitude")
        @Expose
        public String longitude;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmailid() {
            return emailid;
        }

        public void setEmailid(String emailid) {
            this.emailid = emailid;
        }

        public String getContactno() {
            return contactno;
        }

        public void setContactno(String contactno) {
            this.contactno = contactno;
        }

        public String getEwel() {
            return ewel;
        }

        public void setEwel(String ewel) {
            this.ewel = ewel;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDocumentname() {
            return documentname;
        }

        public void setDocumentname(String documentname) {
            this.documentname = documentname;
        }

        public String getDocumentno() {
            return documentno;
        }

        public void setDocumentno(String documentno) {
            this.documentno = documentno;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProfilepicture() {
            return profilepicture;
        }

        public void setProfilepicture(String profilepicture) {
            this.profilepicture = profilepicture;
        }

        public String getActivestatus() {
            return activestatus;
        }

        public void setActivestatus(String activestatus) {
            this.activestatus = activestatus;
        }

        public String getRoleid() {
            return roleid;
        }

        public void setRoleid(String roleid) {
            this.roleid = roleid;
        }

        public String getOtpstatus() {
            return otpstatus;
        }

        public void setOtpstatus(String otpstatus) {
            this.otpstatus = otpstatus;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getOtptime() {
            return otptime;
        }

        public void setOtptime(String otptime) {
            this.otptime = otptime;
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

}
