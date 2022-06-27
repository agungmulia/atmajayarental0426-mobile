package com.agungmuliaekoputra.atmajayarental_0426.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriverResponse {
    private String message;

    @SerializedName("data")
    private List <Driver> driverList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Driver> getDriver() {
        return driverList;
    }

    public void setTransaksiList(List<Driver> driverList) {
        this.driverList = driverList;
    }


}
