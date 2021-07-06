package com.dgs.smartdrone.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponeAPI {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("count")
    private int count;
    @SerializedName("data")
    private List<ProjectEntity> data;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProjectEntity> getData() {
        return data;
    }

    public void setData(List<ProjectEntity> data) {
        this.data = data;
    }
}
