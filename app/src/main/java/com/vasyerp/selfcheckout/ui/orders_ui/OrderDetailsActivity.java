package com.vasyerp.selfcheckout.ui.orders_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.adapters.OrderProductsAdapter;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityOrderDetailsBinding;
import com.vasyerp.selfcheckout.models.ordersummary.OrderSummary;
import com.vasyerp.selfcheckout.models.ordersummary.Receipt;
import com.vasyerp.selfcheckout.models.ordersummary.SalesItems;
import com.vasyerp.selfcheckout.repositories.OrderSummaryRepository;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.viewmodels.ordersummary.OrderSummaryViewModel;
import com.vasyerp.selfcheckout.viewmodels.ordersummary.OrderSummaryViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import lombok.SneakyThrows;

public class OrderDetailsActivity extends AppCompatActivity {

    private final String TAG = OrderDetailsActivity.this.getClass().getSimpleName();
    ActivityOrderDetailsBinding orderDetailsBinding;
    private ArrayList<SalesItems> salesItems;
    Double ordTotal = 0.0;
    private ArrayList<Receipt> receipt;
    KProgressHUD kProgressHUD;
    private boolean isInternetConnected;
    private int companyId;
    private int userId;
    private int branchId;
    private String domainName;
    /*ArrayList<OrderDetailsModel> orderDetailsModelArrayList;
    OrderDetailsModel orderDetailsModel;
    OrderDetailsAdapter orderDetailsAdapter;*/
    int viewQrCode;
    int salesId;
    OrderProductsAdapter orderProductsAdapter;
    OrderSummaryViewModel orderSummaryViewModel;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailsBinding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(orderDetailsBinding.getRoot());
        salesItems = new ArrayList<>();
        Intent intent = getIntent();
        viewQrCode = intent.getIntExtra("checkStatus", 0);
        salesId = intent.getIntExtra("salesId", 0);

        Log.e(TAG, "onCreate: " + salesId);

        companyId = Integer.parseInt(PreferenceManager.getCompanyId(this));
        branchId = Integer.parseInt(PreferenceManager.getBranchId(this));
        userId = Integer.parseInt(PreferenceManager.getUserId(this));
        domainName = PreferenceManager.getDomain(this);

        kProgressHUD = CommonUtil.getKProgressHud(OrderDetailsActivity.this);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setDimAmount(0.25f);

        orderProductsAdapter = new OrderProductsAdapter();
        orderDetailsBinding.rvOrderDetails.setAdapter(orderProductsAdapter);

        initViewModelAndRepository();

        if (viewQrCode == 1) {
            orderDetailsBinding.rlBarcodeView.setVisibility(View.VISIBLE);
            setQrCode();
        } else {
            orderDetailsBinding.rlBarcodeView.setVisibility(View.GONE);
            orderDetailsBinding.tvStatus.setText("Paid");
            Drawable buttonDrawable = orderDetailsBinding.tvStatus.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(buttonDrawable, Color.rgb(3, 60, 6));
            orderDetailsBinding.tvStatus.setBackground(buttonDrawable);
        }

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(OrderDetailsActivity.this);
        connectivityStatus.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInternetConnected = aBoolean;
                if (aBoolean) {
                    //ordersListViewModel.getAllCustomerOrders(pageNo, limit, contactId);
                    orderSummaryViewModel.getOrderSummaryDetails(salesId);
                } else {
                    CommonUtil.showSnackBar(orderDetailsBinding.totalAmountCardView, orderDetailsBinding.totalAmountCardView, "Check Internet Connection");
                }
            }
        });

        orderDetailsBinding.ivBackOrderSummary.setOnClickListener(v -> OrderDetailsActivity.this.finish());

        orderSummaryViewModel.orderSummary.observe(this, new Observer<OrderSummary>() {
            @SneakyThrows
            @Override
            public void onChanged(OrderSummary orderSummary) {
                //orderProductsAdapter.submitList(salesItems);
                salesItems.clear();
                salesItems.addAll(orderSummary.getSalesItems());
                orderProductsAdapter.submitList(salesItems);
                //_totalAmount.postValue(sales.getTotal() - Double.parseDouble(String.format(Locale.getDefault(), "%.2f", sales.getRoundOff())));
                ordTotal = orderSummary.getSales().getTotal() - Double.parseDouble(String.format(Locale.getDefault(), "%.2f", orderSummary.getSales().getRoundOff()));
                orderDetailsBinding.totalAmountTextView.setText(String.format(Locale.getDefault(), "%.2f", ordTotal));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                if (orderSummary.getReceipt().size() > 0) {
                    Date oldDate = sdf2.parse(orderSummary.getReceipt().get(0).getReceiptDate());
                    String oldFormattedDate = sdf.format(oldDate.getTime());
                    orderDetailsBinding.orderDate.setText(oldFormattedDate);
                } else {
                    orderDetailsBinding.orderDate.setText(" ");
                }

            }
        });

        orderSummaryViewModel.error.observe(this, s -> {
            if (s != null) {
                CommonUtil.showSnackBar(orderDetailsBinding.totalAmountCardView, orderDetailsBinding.totalAmountCardView, "" + s);
            }
        });

        orderSummaryViewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                kProgressHUD.show();
            } else {
                kProgressHUD.dismiss();
            }
        });

        /*ordersListAdapter = new OrdersListAdapter(this, ordersArrayList);
        ordersListBinding.rvOrderList.setAdapter(ordersListAdapter);

        ordersListBinding.ivOrderListBack.setOnClickListener(v -> OrdersListActivity.this.finish());*/



        /*setOrderDetailsData();
        orderDetailsModelArrayList = new ArrayList<>();
        orderDetailsAdapter = new OrderDetailsAdapter(this, orderDetailsModelArrayList);
        orderDetailsBinding.rvOrderDetails.setAdapter(orderDetailsAdapter);*/
        //orderDetailsAdapter.notifyDataSetChanged();

    }

    private void initViewModelAndRepository() {
        Api apiInterface = ApiGenerator.getApi(" ").create(Api.class);
        //Api apiInterface = ApiGenerator.getApi(" ").create(Api.class);
        orderSummaryViewModel = new ViewModelProvider(this, new OrderSummaryViewModelFactory(OrderSummaryRepository.getInstance(apiInterface), companyId, branchId, userId)).get(OrderSummaryViewModel.class);
    }

    @SuppressLint("ResourceAsColor")
    private void setQrCode() {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(String.valueOf(salesId), BarcodeFormat.CODE_128, 700, 150);
            orderDetailsBinding.tvStatus.setText("Unpaid");
            Drawable buttonDrawable = orderDetailsBinding.tvStatus.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //the color is a direct color int and not a color resource
            //DrawableCompat.setTint(buttonDrawable, R.color.red);
            DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 0, 0));
            orderDetailsBinding.tvStatus.setBackground(buttonDrawable);

            /*orderDetailsBinding.tvStatus.setBackgroundColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.red));
            orderDetailsBinding.tvStatus.setBackgroundResource(R.drawable.et_bg);*/
            //orderDetailsBinding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(R.color.red));
            orderDetailsBinding.ivBarcode.setImageBitmap(bitmap);
            orderDetailsBinding.tvOrderNo.setText(String.valueOf(salesId));
        } catch (WriterException e) {
            e.printStackTrace();
            //orderDetailsBinding.tvStatus.setBackground(ContextCompat.getColor(OrderDetailsActivity.this, R.color.offwhite));
            //orderDetailsBinding.tvStatus.setTin
            Log.e(TAG, "onCreate: some interrupt ");
        }
    }

    /*private void setOrderDetailsData() {
        orderDetailsModel = new OrderDetailsModel(
                "product 1",
                1.0,
                0.0,
                10.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);

        orderDetailsModel = new OrderDetailsModel(
                "product 2",
                2.0,
                0.0,
                20.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);

        orderDetailsModel = new OrderDetailsModel(
                "product 3",
                3.0,
                0.0,
                30.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);

        orderDetailsModel = new OrderDetailsModel(
                "product 4",
                4.0,
                0.0,
                40.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);
    }*/
}