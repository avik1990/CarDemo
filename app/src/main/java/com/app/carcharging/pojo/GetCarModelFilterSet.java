package com.app.carcharging.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCarModelFilterSet {

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
        @SerializedName("carmodelid")
        @Expose
        public String carmodelid;
        @SerializedName("carversion")
        @Expose
        public List<Carversion> carversion = null;
        @SerializedName("carmodelcolor")
        @Expose
        public List<Carmodelcolor> carmodelcolor = null;
        @SerializedName("city")
        @Expose
        public List<City> city = null;
        @SerializedName("fueltype")
        @Expose
        public List<Fueltype> fueltype = null;
        @SerializedName("transmissiontype")
        @Expose
        public List<Transmissiontype> transmissiontype = null;

        public String getCarmodelid() {
            return carmodelid;
        }

        public void setCarmodelid(String carmodelid) {
            this.carmodelid = carmodelid;
        }

        public List<Carversion> getCarversion() {
            return carversion;
        }

        public void setCarversion(List<Carversion> carversion) {
            this.carversion = carversion;
        }

        public List<Carmodelcolor> getCarmodelcolor() {
            return carmodelcolor;
        }

        public void setCarmodelcolor(List<Carmodelcolor> carmodelcolor) {
            this.carmodelcolor = carmodelcolor;
        }

        public List<City> getCity() {
            return city;
        }

        public void setCity(List<City> city) {
            this.city = city;
        }

        public List<Fueltype> getFueltype() {
            return fueltype;
        }

        public void setFueltype(List<Fueltype> fueltype) {
            this.fueltype = fueltype;
        }

        public List<Transmissiontype> getTransmissiontype() {
            return transmissiontype;
        }

        public void setTransmissiontype(List<Transmissiontype> transmissiontype) {
            this.transmissiontype = transmissiontype;
        }
    }

    public class Fueltype {

        @SerializedName("fueltypeid")
        @Expose
        public String fueltypeid;
        @SerializedName("fueltypename")
        @Expose
        public String fueltypename;

        public String getFueltypeid() {
            return fueltypeid;
        }

        public void setFueltypeid(String fueltypeid) {
            this.fueltypeid = fueltypeid;
        }

        public String getFueltypename() {
            return fueltypename;
        }

        public void setFueltypename(String fueltypename) {
            this.fueltypename = fueltypename;
        }
    }

    public class Transmissiontype {

        @SerializedName("transmissiontypeid")
        @Expose
        public String transmissiontypeid;
        @SerializedName("transmissionname")
        @Expose
        public String transmissionname;

        public String getTransmissiontypeid() {
            return transmissiontypeid;
        }

        public void setTransmissiontypeid(String transmissiontypeid) {
            this.transmissiontypeid = transmissiontypeid;
        }

        public String getTransmissionname() {
            return transmissionname;
        }

        public void setTransmissionname(String transmissionname) {
            this.transmissionname = transmissionname;
        }
    }

    public class City {

        @SerializedName("cityid")
        @Expose
        public String cityid;
        @SerializedName("cityname")
        @Expose
        public String cityname;

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }
    }

    public class Carversion {

        @SerializedName("versionid")
        @Expose
        public String versionid;
        @SerializedName("versionname")
        @Expose
        public String versionname;

        public String getVersionid() {
            return versionid;
        }

        public void setVersionid(String versionid) {
            this.versionid = versionid;
        }

        public String getVersionname() {
            return versionname;
        }

        public void setVersionname(String versionname) {
            this.versionname = versionname;
        }

    }

    public class Carmodelcolor {

        @SerializedName("colorid")
        @Expose
        public String colorid;
        @SerializedName("carmodelid")
        @Expose
        public String carmodelid;
        @SerializedName("colorname")
        @Expose
        public String colorname;

        public String getColorid() {
            return colorid;
        }

        public void setColorid(String colorid) {
            this.colorid = colorid;
        }

        public String getCarmodelid() {
            return carmodelid;
        }

        public void setCarmodelid(String carmodelid) {
            this.carmodelid = carmodelid;
        }

        public String getColorname() {
            return colorname;
        }

        public void setColorname(String colorname) {
            this.colorname = colorname;
        }
    }


}
