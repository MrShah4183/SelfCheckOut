package com.vasyerp.selfcheckout.ui.order_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.vasyerp.selfcheckout.adapters.order_filter.FilterOrderViewPagerAdapter;
import com.vasyerp.selfcheckout.databinding.ActivityFilterOrdersListBinding;

public class FilterOrdersListActivity extends AppCompatActivity {

    ActivityFilterOrdersListBinding orderSummaryBinding;
    /*PaidOrdersListFragment paidOrdersListFragment = new PaidOrdersListFragment(true);
    PaidOrdersListFragment unpaidOrdersListFragment = new PaidOrdersListFragment(false);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderSummaryBinding = ActivityFilterOrdersListBinding.inflate(getLayoutInflater());
        setContentView(orderSummaryBinding.getRoot());

        FilterOrderViewPagerAdapter filterOrderViewPagerAdapter = new FilterOrderViewPagerAdapter(this.getSupportFragmentManager());
        filterOrderViewPagerAdapter.addFragment(new PaidOrdersListFragment(true), "Paid Orders");
        filterOrderViewPagerAdapter.addFragment(new PaidOrdersListFragment(false), "Unpaid Orders");

        /*filterOrderViewPagerAdapter.addFragment(paidOrdersListFragment, "Paid Orders");
        filterOrderViewPagerAdapter.addFragment(unpaidOrdersListFragment, "Unpaid Orders");*/

        orderSummaryBinding.viewPagerOrderList.setAdapter(filterOrderViewPagerAdapter);
        orderSummaryBinding.tabLayoutFilter.setupWithViewPager(orderSummaryBinding.viewPagerOrderList);

        orderSummaryBinding.ivOrderListBack.setOnClickListener(v -> FilterOrdersListActivity.this.finish());
    }
}