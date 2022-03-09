package com.vasyerp.selfcheckout.models.customer;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Expose
    private int accountGroupId;
    @Expose
    private String accountGroupName;
    @Expose
    private String groupType;
    @Expose
    private String keyword;
}
