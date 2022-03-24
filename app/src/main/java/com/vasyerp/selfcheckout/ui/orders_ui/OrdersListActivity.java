package com.vasyerp.selfcheckout.ui.orders_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.adapters.orders_list.OrdersListAdapter;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityOrdersListBinding;
import com.vasyerp.selfcheckout.models.orderlist.OrderModel;
import com.vasyerp.selfcheckout.models.orderlist.OrdersListResponse;
import com.vasyerp.selfcheckout.repositories.OrdersListRepository;
import com.vasyerp.selfcheckout.ui.company.CompanyLoginActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.viewmodels.orderlist.OrdersListViewModel;
import com.vasyerp.selfcheckout.viewmodels.orderlist.OrdersListViewModelFactory;

import java.util.ArrayList;

public class OrdersListActivity extends AppCompatActivity {
    String TAG = OrdersListActivity.this.getClass().getSimpleName();
    ActivityOrdersListBinding ordersListBinding;
    //not using

    KProgressHUD kProgressHUD;
    ArrayList<OrderModel> ordersArrayList;
    private boolean isInternetConnected;
    OrdersListAdapter ordersListAdapter;

    private int companyId;
    private int userId;
    private int branchId;
    private String domainName;
    private int contactId;

    int pageNo = 0, limit = 20;
    int pageTotal = 0;
    boolean checkPage = false;

    //MainViewModel mainViewModel;
    OrdersListViewModel ordersListViewModel;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersListBinding = ActivityOrdersListBinding.inflate(getLayoutInflater());
        setContentView(ordersListBinding.getRoot());

        companyId = Integer.parseInt(PreferenceManager.getCompanyId(this));
        branchId = Integer.parseInt(PreferenceManager.getBranchId(this));
        userId = Integer.parseInt(PreferenceManager.getUserId(this));
        domainName = PreferenceManager.getDomain(this);
        contactId = Integer.parseInt(PreferenceManager.userContactId(this));

        ordersArrayList = new ArrayList<>();

        kProgressHUD = CommonUtil.getKProgressHud(OrdersListActivity.this);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setDimAmount(0.25f);

        initViewModelAndRepository();

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(OrdersListActivity.this);
        connectivityStatus.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInternetConnected = aBoolean;
                if (aBoolean) {
                    ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                } else {
                    CommonUtil.showSnackBar(ordersListBinding.llOrderListBottom, ordersListBinding.llOrderListBottom, "Check Internet Connection");
                }
            }
        });

        //todo set adapter
        ordersListAdapter = new OrdersListAdapter(this, ordersArrayList);
        ordersListBinding.rvOrderList.setAdapter(ordersListAdapter);

        ordersListBinding.ivOrderListBack.setOnClickListener(v -> OrdersListActivity.this.finish());

        ordersListBinding.tvBtnOrderPrev.setOnClickListener(v -> {
            int pageNo = Integer.parseInt(ordersListBinding.etOrderCurrPg.getText().toString());
            Log.e("checkPre1", "pageNO" + pageNo);
            if (pageNo > 1) {
                pageNo = pageNo - 2;
                Log.e("checkPre2", "pageNO" + pageNo);
                ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                Log.e("checkPre3", "after api call pageNO" + pageNo);
            }
        });

        ordersListBinding.tvBtnOrderNext.setOnClickListener(v -> {
            int pageNo = Integer.parseInt(ordersListBinding.etOrderCurrPg.getText().toString());
            if (pageTotal > pageNo) {
                //int pageNo = Integer.parseInt(ordersListBinding.etOrderCurrPg.getText().toString());
                Log.e("checkNext1", "pageNO" + pageNo);
                //pageNo = pageNo - 1;
                Log.e("checkNext2", "pageNO" + pageNo);
                ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                Log.e("checkNext3", "after api call pageNO" + pageNo);
            }
        });

        ordersListBinding.etOrderCurrPg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (checkPage) {
                        int pageNo = Integer.parseInt(ordersListBinding.etOrderCurrPg.getText().toString());
                        Log.e("check1", "pageNO" + pageNo);
                        pageNo = pageNo - 1;
                        Log.e("check2", "pageNO" + pageNo);
                        ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                        Log.e("check3", "after api call pageNO" + pageNo);
                    } else {
                        Log.e("checkwrong", "no call api");
                    }
                    return true;
                }
                return false;
            }
        });
        ordersListBinding.etOrderCurrPg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = String.valueOf(s);
                if (!str.equals("") && !str.equals("0") && Integer.parseInt(str) <= pageTotal) {
                    //Toast.makeText(PastOrderActivity.this, "Get page response" + s, Toast.LENGTH_SHORT).show();
                    Log.e("getData", "" + s);
                    checkPage = true;
                } else {
                    //Toast.makeText(PastOrderActivity.this, "Please enter valid page" + s, Toast.LENGTH_SHORT).show();
                    Log.e("noData", "" + s);
                    checkPage = false;
                }
                /*pageNo = s;
                apiCallPastOrderList();*/
            }
        });

        ordersListViewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    CommonUtil.showSnackBar(ordersListBinding.llOrderListBottom, ordersListBinding.llOrderListBottom, "" + s);
                }
            }
        });

        ordersListViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    kProgressHUD.show();
                } else {
                    kProgressHUD.dismiss();
                }
            }
        });

        ordersListViewModel.ordersList.observe(this, new Observer<OrdersListResponse>() {
            @Override
            public void onChanged(OrdersListResponse ordersListResponse) {
                ordersArrayList.clear();
                pageTotal = ordersListResponse.getTotal() / ordersListResponse.getLimit();
                pageTotal += 1;
                String strOrderTotal = "To " + pageTotal;
                ordersListBinding.tvOrderTotalPgs.setText(strOrderTotal);
                ordersListBinding.etOrderCurrPg.setText(String.valueOf(ordersListResponse.getPageNo() + 1));
                ordersArrayList.addAll(ordersListResponse.getData());
                ordersListAdapter.notifyDataSetChanged();
            }
        });
        /*orderListModelArrayList = new ArrayList<>();
        setOrderListData();
        orderListAdapter = new OrdersListAdapter(OrdersListActivity.this, orderListModelArrayList);
        ordersListBinding.rvOrderList.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();*/


    }

    private void initViewModelAndRepository() {
        //Api apiInterface = RazorpayApiGenerator.getApi(domainName).create(Api.class);
        String strBaseUrl = PreferenceManager.getDomain(OrdersListActivity.this);
        Api apiInterface = ApiGenerator.getApi(CommonUtil.tempBaseUrl).create(Api.class);
        ordersListViewModel = new ViewModelProvider(this, new OrdersListViewModelFactory(OrdersListRepository.getInstance(apiInterface), companyId, branchId, userId)).get(OrdersListViewModel.class);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}