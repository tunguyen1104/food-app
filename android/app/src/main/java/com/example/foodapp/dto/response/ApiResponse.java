package com.example.foodapp.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    @SerializedName("status")
    private String status;
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private T data;
    @SerializedName("errors")
    private Map<String, String> errors;
    @SerializedName("error")
    private String error;

    public boolean isSuccess() {
        return "SUCCESS".equals(status);
    }
}
