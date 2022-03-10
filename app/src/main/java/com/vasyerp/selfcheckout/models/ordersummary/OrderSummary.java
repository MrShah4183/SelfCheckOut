package com.vasyerp.selfcheckout.models.ordersummary;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {
    @SerializedName("sales")
    @Expose
    private Sales sales;

    @SerializedName("salesItems")
    @Expose
    private List<SalesItems> salesItems;

    @SerializedName("receipt")
    @Expose
    private List<Receipt> receipt;

}
