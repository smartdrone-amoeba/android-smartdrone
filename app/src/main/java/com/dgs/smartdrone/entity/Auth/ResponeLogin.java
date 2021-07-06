package com.dgs.smartdrone.entity.Auth;

import com.google.gson.annotations.SerializedName;

public class    ResponeLogin {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
