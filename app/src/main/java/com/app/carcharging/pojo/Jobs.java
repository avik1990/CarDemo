package com.app.carcharging.pojo;

import android.databinding.BaseObservable;

public class Jobs extends BaseObservable {

    String id;
    String job_id;
    String job_heading;
    String job_details;
    String job_image;

    public Jobs(String id, String job_id, String job_heading, String job_details, String job_image) {
        this.id = id;
        this.job_id = job_id;
        this.job_heading = job_heading;
        this.job_details = job_details;
        this.job_image = job_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_heading() {
        return job_heading;
    }

    public void setJob_heading(String job_heading) {
        this.job_heading = job_heading;
    }

    public String getJob_details() {
        return job_details;
    }

    public void setJob_details(String job_details) {
        this.job_details = job_details;
    }

    public String getJob_image() {
        return job_image;
    }

    public void setJob_image(String job_image) {
        this.job_image = job_image;
    }
}
