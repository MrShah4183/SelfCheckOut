package com.vasyerp.selfcheckout.models.product;

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
public class GetAllProducts {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("itemCode")
    @Expose
    private String itemCode;
}
