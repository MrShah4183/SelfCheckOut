package com.vasyerp.selfcheckout.ui.order_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.vasyerp.selfcheckout.adapters.order_filter.FilterOrderViewPagerAdapter;
import com.vasyerp.selfcheckout.databinding.ActivityFilterOrdersListBinding;

public class FilterOrdersListActivity extends AppCompatActivity {

    ActivityFilterOrdersListBinding orderSummaryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderSummaryBinding = ActivityFilterOrdersListBinding.inflate(getLayoutInflater());
        setContentView(orderSummaryBinding.getRoot());
        /*AdapterBookingViewPager adapterBookingViewPager = new AdapterBookingViewPager(getActivity().getSupportFragmentManager());
        adapterBookingViewPager.addFragment(new PendingBookingFragment(), "Pending Booking");
        adapterBookingViewPager.addFragment(new CompletedBookingFragment(), "Complete Booking");

        bookingViewPager.setAdapter(adapterBookingViewPager);
        bookingTab.setupWithViewPager(bookingViewPager);*/


        FilterOrderViewPagerAdapter filterOrderViewPagerAdapter = new FilterOrderViewPagerAdapter(this.getSupportFragmentManager());
        filterOrderViewPagerAdapter.addFragment(new PaidOrdersListFragment(true), "Paid Orders");
        filterOrderViewPagerAdapter.addFragment(new PaidOrdersListFragment(false), "Unpaid Orders");

        orderSummaryBinding.viewPagerOrderList.setAdapter(filterOrderViewPagerAdapter);
        orderSummaryBinding.tabLayoutFilter.setupWithViewPager(orderSummaryBinding.viewPagerOrderList);

        orderSummaryBinding.ivOrderListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterOrdersListActivity.this.finish();
            }
        });
    }
}