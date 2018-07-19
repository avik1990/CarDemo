package com.app.carcharging.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCarManufacturer {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("message")
    @Expose
    private String message;

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

        @SerializedName("manufacturerid")
        @Expose
        private String manufacturerid;
        @SerializedName("manufacturername")
        @Expose
        private String manufacturername;
        @SerializedName("carmodel")
        @Expose
        private List<Carmodel> carmodel = null;

        public String getManufacturerid() {
            return manufacturerid;
        }

        public void setManufacturerid(String manufacturerid) {
            this.manufacturerid = manufacturerid;
        }

        public String getManufacturername() {
            return manufacturername;
        }

        public void setManufacturername(String manufacturername) {
            this.manufacturername = manufacturername;
        }

        public List<Carmodel> getCarmodel() {
            return carmodel;
        }

        public void setCarmodel(List<Carmodel> carmodel) {
            this.carmodel = carmodel;
        }
    }

    public class Carmodel {

        @SerializedName("manufacturerid")
        @Expose
        private String manufacturerid;
        @SerializedName("carmodelid")
        @Expose
        private String carmodelid;
        @SerializedName("carmodelname")
        @Expose
        private String carmodelname;

        public String getManufacturerid() {
            return manufacturerid;
        }

        public void setManufacturerid(String manufacturerid) {
            this.manufacturerid = manufacturerid;
        }

        public String getCarmodelid() {
            return carmodelid;
        }

        public void setCarmodelid(String carmodelid) {
            this.carmodelid = carmodelid;
        }

        public String getCarmodelname() {
            return carmodelname;
        }

        public void setCarmodelname(String carmodelname) {
            this.carmodelname = carmodelname;
        }
    }

}
