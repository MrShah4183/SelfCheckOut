package com.vasyerp.selfcheckout.api;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    @Expose
    @SerializedName("status")
    boolean status;
    @Expose
    @SerializedName("message")
    String message;
    @Expose
    @SerializedName("response")
    T response;
}
