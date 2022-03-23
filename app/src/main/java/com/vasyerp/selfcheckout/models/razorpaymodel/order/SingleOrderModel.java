package com.vasyerp.selfcheckout.models.razorpaymodel.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SingleOrderModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("entity")
    @Expose
    public String entity;
    @SerializedName("amount")
    @Expose
    public int amount;
    @SerializedName("amount_paid")
    @Expose
    public int amountPaid;
    @SerializedName("amount_due")
    @Expose
    public int amountDue;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("receipt")
    @Expose
    public String receipt;
    @SerializedName("offer_id")
    @Expose
    public Object offerId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("attempts")
    @Expose
    public int attempts;
    @SerializedName("notes")
    @Expose
    public List<Object> notes = null;
    @SerializedName("created_at")
    @Expose
    public int createdAt;
}
