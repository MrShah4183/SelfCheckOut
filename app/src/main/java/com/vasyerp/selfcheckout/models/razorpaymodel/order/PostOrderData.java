package com.vasyerp.selfcheckout.models.razorpaymodel.order;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderData {
    @SerializedName("amount")
    @Expose
    public Double amount;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("receipt")
    @Expose
    public String receipt;
}
