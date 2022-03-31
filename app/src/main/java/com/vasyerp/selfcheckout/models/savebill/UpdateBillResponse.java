package com.vasyerp.selfcheckout.models.savebill;

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

public class UpdateBillResponse {
    @Expose
    @SerializedName("status")
    boolean status;
    @Expose
    @SerializedName("message")
    String message;
}
