package com.vasyerp.selfcheckout.models.ordersummary;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Expose
    private int salesId;
    @Expose
    private String paymentType;
    @Expose
    private int receiptId;
    @Expose
    private String receiptDate;
    @Expose
    private float totalReceipt;
    @Expose
    private float kasarAmount;
    @Expose
    private float receiptBillId;
    @Expose
    private String prefix;
}
