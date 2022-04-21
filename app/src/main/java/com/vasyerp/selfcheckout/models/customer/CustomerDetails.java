package com.vasyerp.selfcheckout.models.customer;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetails {
    @Expose
    private Long contactId;
    @Expose
    private String companyName;
    @Expose
    private String name;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String mobNo;
    @Expose
    private String telephone;
    @Expose
    private String email;
    @Expose
    private String dateOfBirth;
    @Expose
    private String anniversaryDate;
    @Expose
    private String gstin;
    @Expose
    private Long shopifySync;
    @Expose
    private Long tds;
    @Expose
    private Long associatedmerchant;
    @Expose
    private Long shopifySourceId;
    @Expose
    private String panNo;
    @Expose
    private String bankAcNo;
    @Expose
    private String bankName;
    @Expose
    private String bankBranch;
    @Expose
    private String bankIfsc;
    @Expose
    private String contactType;
    @Expose
    private String type;
    @Expose
    private Long acceptMarketing;
    @Expose
    private String note;
    @Expose
    private String tag;
    @Expose
    private int branchId;
    @Expose
    private int companyId;
    @Expose
    private int alterBy;
    @Expose
    private int createdBy;
    @Expose
    private int isDeleted;
    @Expose
    private int active;
    @Expose
    private String createdOn;
    @Expose
    private String modifiedOn;
    @Expose
    private String gstType;
    @Expose
    private String creditDays;
    @Expose
    private String creditLimit;
    @Expose
    private String instruction;
    @Expose
    private String imgLocation;
    @Expose
    private Double credit;
    @Expose
    private Double debit;
    @Expose
    private List<ContactAddressVo> contactAddressVos;
    @Expose
    private AccountCustomVo accountCustomVo;
    @Expose
    private PaymentMasterVo paymentMasterVo;
    @Expose
    private PaymentTermVo paymentTermVo;
    @Expose
    private MemberShipMasterVo memberShipMasterVo;
    @Expose
    private String fromMemeberShipDate;
    @Expose
    private String toMemeberShipDate;
    @Expose
    private int isFeedbackGiven;
    @Expose
    private String whatsappNo;
    @Expose
    private int wooCommerceCustomerId;
    @Expose
    private String remark;
    @Expose
    private String code;
    @Expose
    private String supplierCode;
    @Expose
    private int memberShipNo;
    @Expose
    private double walletBalance;
    @Expose
    private double commission;
    @Expose
    private String createdby_name;
    @Expose
    private String dueAmount;
    @Expose
    private String opening;
    @Expose
    private String otp;
    @Expose
    private String password;
    @Expose
    private String tempotp;
    @Expose
    private boolean existInTransaction;
}
