package com.vasyerp.selfcheckout.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderListModel {
    String strOrderInv;
    String strOrderDate;
    Double orderTotal;
}
