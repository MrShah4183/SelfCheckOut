package com.vasyerp.selfcheckout.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CompanyDetailsModel {
    String companyImg;
    int companyIV;
    String companyName;
    String companyAddress;
}
