package com.vasyerp.selfcheckout.models.ordersummary;

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
public class Sales {
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("sez")
    private long sez;
    @Expose
    @SerializedName("termsAndConditionIds")
    private String termsAndConditionIds;
    @Expose
    @SerializedName("stockTransferId")
    private long stockTransferId;
    @Expose
    @SerializedName("modifiedOn")
    private String modifiedOn;
    @Expose
    @SerializedName("deliveryStatus")
    private String deliveryStatus;

    @Expose
    @SerializedName("reverseCharge")
    private long reverseCharge;

    @Expose
    @SerializedName("dueDate")
    private long dueDate;

    @Expose
    @SerializedName("gstApply")
    private long gstApply;

    @Expose
    @SerializedName("isFeedbackMessageSent")
    private long isFeedbackMessageSent;

    @Expose
    private String appPaymentMethod;
    @Expose
    private String shippingCountriesName;
    @Expose
    private String billingCompanyName;
    @Expose
    private int discountId;
    @Expose
    private String shippingCityCode;
    @Expose
    private String shippingCityName;
    @Expose
    private String shippingStateName;
    @Expose
    private int taxType;
    @Expose
    private String onlinePaymentId;
    @Expose
    private int companyId;
    @Expose
    private int branchId;
    @Expose
    @SerializedName("total")
    private double total;
    @Expose
    private int salesId;
    @Expose
    private String billingCityCode;
    @Expose
    private int contactId;
    @Expose
    private String billingCountriesCode;
    @Expose
    private String billingStateCode;
    @Expose
    private String flatDiscountType;
    @Expose
    private String creditNoteId;
    @Expose
    private float flatDiscount;
    @Expose
    private String billingCountriesName;
    @Expose
    private String billingStateName;
    @Expose
    private int wooCommerceStatus;
    @Expose
    private int wooCommerceOrderId;
    @Expose
    private int agentId;
    @Expose
    private long paymentTermId;
    @Expose
    private float onlinePaymentAmount;
    @Expose
    private float additionalChargeTotalAmount;
    @Expose
    private int isWooCommerceOrder;
    @Expose
    private String wooCommerceTransactionId;
    @Expose
    private String receiptpaymentType;
    @Expose
    private String onlinePaymentDate;
    @Expose
    private double billdiscount;
    @Expose
    @SerializedName("tendered")
    private double tendered;
    @Expose
    private String orderType;
    @Expose
    private int isReturn;
    @Expose
    private int defaultSyncInvoice;
    @Expose
    private double creditAmount;
    @Expose
    private String salesDate;
    @Expose
    private String customerName;
    @Expose
    private double roundOff;
    @Expose
    private long salesNo;
    @Expose
    private String paymentType;
    @Expose
    private long tokenNo;
    @Expose
    private String pdfToken;
    @Expose
    private int isDeleted;
    @Expose
    @SerializedName("imgLocation")
    private String imgLocation;
    @Expose
    @SerializedName("createdOn")
    private String createdOn;
    @Expose
    private int shopifyOrderId;
    @Expose
    private double paidAmount;
    @Expose
    private double flatDiscountInAmount;
    @Expose
    private String billDiscountType;
    @Expose
    private double totalDisocuntProductwise;
    @Expose
    private String shippingAddressLine1;
    @Expose
    private String shippingCountriesCode;
    @Expose
    private String shippingAddressLine2;
    @Expose
    private String billingAddressLine1;
    @Expose
    private String shippingStateCode;
    @Expose
    private String billingFirstName;
    @Expose
    private String billingAddressLine2;
    @Expose
    private String shippingCompanyName;
    @Expose
    private String shippingFirstName;
    @Expose
    private String shippingLastName;
    @Expose
    private String billingLastName;
    @Expose
    private String billingPinCode;
    @Expose
    private String shippingPinCode;
    @Expose
    private String billingCityName;
    @Expose
    private String mobileNumber;
    @Expose
    private int transportId;
    @Expose
    private int createdById;
    @Expose
    private float totalReceipt;
    @Expose
    private float alterById;
    @Expose
    private int parentId;
    @Expose
    private int onlineOrder;
    @Expose
    private String prefix;
    @Expose
    private String status;

}
