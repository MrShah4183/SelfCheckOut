package com.vasyerp.selfcheckout.ui.order_list;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.adapters.OrderListAdapter;
import com.vasyerp.selfcheckout.databinding.ActivityOrdersListBinding;
import com.vasyerp.selfcheckout.models.OrderListModel;
import com.vasyerp.selfcheckout.utils.CommonUtil;

import java.util.ArrayList;

public class OrdersListActivity extends AppCompatActivity {

    ActivityOrdersListBinding ordersListBinding;
    OrderListAdapter orderListAdapter;
    ArrayList<OrderListModel> orderListModelArrayList;
    OrderListModel orderListModel;
    KProgressHUD kProgressHUD;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersListBinding = ActivityOrdersListBinding.inflate(getLayoutInflater());
        setContentView(ordersListBinding.getRoot());
        orderListModelArrayList = new ArrayList<>();
        kProgressHUD = CommonUtil.getProgressView(this);
        setOrderListData();
        orderListAdapter = new OrderListAdapter(OrdersListActivity.this, orderListModelArrayList);
        ordersListBinding.rvOrderList.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();

        ordersListBinding.ivOrderListBack.setOnClickListener(v -> OrdersListActivity.this.finish());
    }

    private void setOrderListData() {
        orderListModel = new OrderListModel(
                "Inv001",
                "2022-03-01",
                100.00
        );
        orderListModelArrayList.add(orderListModel);

        orderListModel = new OrderListModel(
                "Inv002",
                "2022-03-02",
                200.00
        );
        orderListModelArrayList.add(orderListModel);

        orderListModel = new OrderListModel(
                "Inv003",
                "2022-03-03",
                300.00
        );
        orderListModelArrayList.add(orderListModel);

        orderListModel = new OrderListModel(
                "Inv004",
                "2022-03-04",
                400.00
        );
        orderListModelArrayList.add(orderListModel);

        orderListModel = new OrderListModel(
                "Inv005",
                "2022-03-05",
                500.00
        );
        orderListModelArrayList.add(orderListModel);

        orderListModel = new OrderListModel(
                "Inv006",
                "2022-03-06",
                600.00
        );
        orderListModelArrayList.add(orderListModel);
    }
}