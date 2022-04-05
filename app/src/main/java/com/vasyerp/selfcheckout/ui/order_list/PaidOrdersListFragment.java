package com.vasyerp.selfcheckout.ui.order_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.adapters.orders_list.OrdersListAdapter;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.FragmentPaidOrdersListBinding;
import com.vasyerp.selfcheckout.models.orderlist.OrderModel;
import com.vasyerp.selfcheckout.models.orderlist.OrdersListResponse;
import com.vasyerp.selfcheckout.repositories.OrdersListRepository;
import com.vasyerp.selfcheckout.ui.orders_ui.OrdersListActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.viewmodels.orderlist.OrdersListViewModel;
import com.vasyerp.selfcheckout.viewmodels.orderlist.OrdersListViewModelFactory;

import java.util.ArrayList;


public class PaidOrdersListFragment extends Fragment {

    FragmentPaidOrdersListBinding fragmentPaidOrdersListBinding;
    //String TAG = getActivity().getClass().getSimpleName();
    String TAG = "FragmentPaid";
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


    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;*/

    public PaidOrdersListFragment() {
        // Required empty public constructor
    }

    /*public static PaidOrdersListFragment newInstance(String param1, String param2) {
        PaidOrdersListFragment fragment = new PaidOrdersListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentPaidOrdersListBinding = FragmentPaidOrdersListBinding.inflate(getLayoutInflater());

        companyId = Integer.parseInt(PreferenceManager.getCompanyId(requireActivity()));
        branchId = Integer.parseInt(PreferenceManager.getBranchId(requireActivity()));
        userId = Integer.parseInt(PreferenceManager.getUserId(requireActivity()));
        domainName = PreferenceManager.getDomain(requireActivity());
        contactId = Integer.parseInt(PreferenceManager.userContactId(requireActivity()));

        ordersArrayList = new ArrayList<>();

        kProgressHUD = CommonUtil.getKProgressHud(requireActivity());
        kProgressHUD.setCancellable(false);
        kProgressHUD.setDimAmount(0.25f);

        initViewModelAndRepository();

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(requireActivity());
        connectivityStatus.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInternetConnected = aBoolean;
                if (aBoolean) {
                    ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                } else {
                    CommonUtil.showSnackBar(fragmentPaidOrdersListBinding.llOrderListPaidBottom, fragmentPaidOrdersListBinding.llOrderListPaidBottom, "Check Internet Connection");
                }
            }
        });

        ordersListAdapter = new OrdersListAdapter(requireActivity(), ordersArrayList);
        fragmentPaidOrdersListBinding.rvOrderListPaid.setAdapter(ordersListAdapter);

        fragmentPaidOrdersListBinding.tvBtnOrderPaidPrev.setOnClickListener(v -> {
            int pageNo = Integer.parseInt(fragmentPaidOrdersListBinding.etOrderPaidCurrPg.getText().toString());
            Log.e("checkPre1", "pageNO" + pageNo);
            if (pageNo > 1) {
                pageNo = pageNo - 2;
                Log.e("checkPre2", "pageNO" + pageNo);
                ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                Log.e("checkPre3", "after api call pageNO" + pageNo);
            }
        });

        fragmentPaidOrdersListBinding.tvBtnOrderPaidNext.setOnClickListener(v -> {
            int pageNo = Integer.parseInt(fragmentPaidOrdersListBinding.etOrderPaidCurrPg.getText().toString());
            if (pageTotal > pageNo) {
                //int pageNo = Integer.parseInt(fragmentPaidOrdersListBinding.etOrderPaidCurrPg.getText().toString());
                Log.e("checkNext1", "pageNO" + pageNo);
                //pageNo = pageNo - 1;
                Log.e("checkNext2", "pageNO" + pageNo);
                ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                Log.e("checkNext3", "after api call pageNO" + pageNo);
            }
        });

        fragmentPaidOrdersListBinding.etOrderPaidCurrPg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (checkPage) {
                        int pageNo = Integer.parseInt(fragmentPaidOrdersListBinding.etOrderPaidCurrPg.getText().toString());
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
        fragmentPaidOrdersListBinding.etOrderPaidCurrPg.addTextChangedListener(new TextWatcher() {
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
                    CommonUtil.showSnackBar(fragmentPaidOrdersListBinding.llOrderListPaidBottom, fragmentPaidOrdersListBinding.llOrderListPaidBottom, "" + s);
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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(OrdersListResponse ordersListResponse) {
                ordersArrayList.clear();
                pageTotal = ordersListResponse.getTotal() / ordersListResponse.getLimit();
                pageTotal += 1;
                String strOrderTotal = "To " + pageTotal;
                fragmentPaidOrdersListBinding.tvOrderPaidTotalPgs.setText(strOrderTotal);
                fragmentPaidOrdersListBinding.etOrderPaidCurrPg.setText(String.valueOf(ordersListResponse.getPageNo() + 1));
                ordersArrayList.addAll(ordersListResponse.getData());
                ordersListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initViewModelAndRepository() {
        //Api apiInterface = RazorpayApiGenerator.getApi(domainName).create(Api.class);
        String strBaseUrl = PreferenceManager.getDomain(requireActivity());
        Api apiInterface = ApiGenerator.getApi(CommonUtil.tempBaseUrl).create(Api.class);
        ordersListViewModel = new ViewModelProvider(this, new OrdersListViewModelFactory(OrdersListRepository.getInstance(apiInterface), companyId, branchId, userId)).get(OrdersListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //observerMethod();
        //return inflater.inflate(R.layout.fragment_paid_orders_list, container, false);
        return fragmentPaidOrdersListBinding.getRoot();
    }


    /*@Override
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
    }*/
}