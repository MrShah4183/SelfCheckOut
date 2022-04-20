package com.vasyerp.selfcheckout.models.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusModel {
    long salesid;
    String status;
}
