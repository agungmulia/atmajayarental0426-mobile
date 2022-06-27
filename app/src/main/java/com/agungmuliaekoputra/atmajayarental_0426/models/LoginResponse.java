package com.agungmuliaekoputra.atmajayarental_0426.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("user")
    private  Login login;


    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Login getResponseList() {
        return login;
    }
}
