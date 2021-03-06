package com.vasyerp.selfcheckout.ui.orders_ui;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.adapters.OrderProductsAdapter;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityOrderDetailsBinding;
import com.vasyerp.selfcheckout.models.firebase.OrderStatusModel;
import com.vasyerp.selfcheckout.models.ordersummary.OrderSummary;
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
    //todo set qrcode of POS2455
    //prefix+salesNo POS2455
    //{"status":true,"message":"Success","response":{"salesId":715117,"prefix":"POS","token":371,"salesNo":2455,"upiData":"upi://pay?pa=9409434973@paytm&pn=VASYERP&tr=715117&tn=POS2455&am=99.0&cu=INR","customerAddress":null}}
    //response":{"sales":{"type":"pos","contactId":209880,"salesNo":2455,"total":99.0,"billingCityCode":null,"billingStateCode":null,
    //private ArrayList<Receipt> receipt;
    KProgressHUD kProgressHUD;
    boolean isInternetConnected;
    private int companyId;
    private int userId;
    private int branchId;
    private String domainName;
    /*ArrayList<OrderDetailsModel> orderDetailsModelArrayList;
    OrderDetailsModel orderDetailsModel;
    OrderDetailsAdapter orderDetailsAdapter;*/
    boolean orderDetailsStatus;
    long orderDetailsSalesNo;
    String tempOrderNo;
    OrderProductsAdapter orderProductsAdapter;
    OrderSummaryViewModel orderSummaryViewModel;

    private DatabaseReference mDatabase;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailsBinding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(orderDetailsBinding.getRoot());
        salesItems = new ArrayList<>();
        Intent intent = getIntent();
        orderDetailsStatus = intent.getBooleanExtra(CommonUtil.ORDER_DETAIL_STATUS, true);
        orderDetailsSalesNo = intent.getLongExtra(CommonUtil.ORDER_DETAIL_SALE_NO, 0);
        //todo true for paid false for unpaid

        Log.e(TAG, "onCreate: " + orderDetailsSalesNo);

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

        if (!orderDetailsStatus) {
            orderDetailsBinding.rlBarcodeView.setVisibility(View.VISIBLE);
            tempOrderNo = intent.getStringExtra(CommonUtil.ORDER_DETAIL_NO);
            setQrCode(tempOrderNo);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            OrderStatusModel orderStatusModel = new OrderStatusModel();
            orderStatusModel.setSalesid(orderDetailsSalesNo);
            orderStatusModel.setStatus("unpaid");
            mDatabase.child(String.valueOf(orderDetailsSalesNo)).setValue(orderStatusModel);

            mDatabase.child(String.valueOf(orderDetailsSalesNo)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e(TAG, "onDataChange: " + snapshot.toString());
                    OrderStatusModel tempOrderStatusModel = snapshot.getValue(OrderStatusModel.class);
                    Log.e(TAG, "onDataChange: model value" + tempOrderStatusModel.toString());
                    if (tempOrderStatusModel.getStatus().trim().toLowerCase().equals("paid")) {
                        orderDetailsBinding.rlBarcodeView.setVisibility(View.GONE);
                        orderDetailsBinding.tvStatus.setText("Paid");
                        Drawable buttonDrawable = orderDetailsBinding.tvStatus.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        //the color is a direct color int and not a color resource
                        DrawableCompat.setTint(buttonDrawable, Color.rgb(3, 60, 6));
                        orderDetailsBinding.tvStatus.setBackground(buttonDrawable);
                    } else if (tempOrderStatusModel.getStatus().trim().toLowerCase().equals("unpaid")) {
                        orderDetailsBinding.rlBarcodeView.setVisibility(View.VISIBLE);
                        setQrCode(tempOrderNo);
                        orderDetailsBinding.tvStatus.setText("Unpaid");
                        Drawable buttonDrawable = orderDetailsBinding.tvStatus.getBackground();
                        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                        DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 0, 0));
                        orderDetailsBinding.tvStatus.setBackground(buttonDrawable);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.toString());
                }
            });
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
                    orderSummaryViewModel.getOrderSummaryDetails(orderDetailsSalesNo);
                } else {
                    CommonUtil.showSnackBar(orderDetailsBinding.totalAmountCardView, orderDetailsBinding.totalAmountCardView, "Check Internet Connection");
                }
            }
        });

        orderDetailsBinding.ivBackOrderSummary.setOnClickListener(v -> OrderDetailsActivity.this.finish());

        orderDetailsBinding.btnCancleOrder.setOnClickListener(v -> {
            Toast.makeText(OrderDetailsActivity.this, "Cancle Order", Toast.LENGTH_SHORT).show();
            //Toast.makeText(OrderDetailsActivity.this, "Cancle Order", Toast.LENGTH_SHORT).show();
        });

        orderDetailsBinding.btnRepaid.setOnClickListener(v -> {
            Toast.makeText(OrderDetailsActivity.this, "Repayment Process", Toast.LENGTH_SHORT).show();
            //Toast.makeText(OrderDetailsActivity.this, "Cancle Order", Toast.LENGTH_SHORT).show();
        });

        orderSummaryViewModel.orderSummary.observe(this, new Observer<OrderSummary>() {
            @SneakyThrows
            @Override
            public void onChanged(OrderSummary orderSummary) {
                //orderProductsAdapter.submitList(salesItems);
                salesItems.clear();
                salesItems.addAll(orderSummary.getSalesItems());
                orderProductsAdapter.submitList(salesItems);
                //_totalAmount.postValue(sales.getTotal() - Double.parseDouble(String.format(Locale.getDefault(), "%.2f", sales.getRoundOff())));
                ordTotal = orderSummary.getSales().getTotal() -
                        Double.parseDouble(String.format(Locale.getDefault(), "%.2f", orderSummary.getSales().getRoundOff()));
                orderDetailsBinding.tvOrderTotalAmt.setText(String.format(Locale.getDefault(), "%.2f", ordTotal));
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                /*if (orderSummary.getReceipt().size() > 0) {
                    Date oldDate = sdf2.parse(orderSummary.getReceipt().get(0).getReceiptDate());
                    assert oldDate != null;
                    String oldFormattedDate = sdf.format(oldDate.getTime());
                    orderDetailsBinding.tvOrderDate.setText(oldFormattedDate);
                } else {
                    Date oldDate = sdf2.parse(orderSummary.getSales().getSalesDate());
                    assert oldDate != null;
                    String oldFormattedDate = sdf.format(oldDate.getTime());
                    orderDetailsBinding.tvOrderDate.setText(oldFormattedDate);
                }*/

                Date oldDate = sdf2.parse(orderSummary.getSales().getSalesDate());
                assert oldDate != null;
                String oldFormattedDate = sdf.format(oldDate.getTime());
                orderDetailsBinding.tvOrderDate.setText(oldFormattedDate);

                orderDetailsBinding.tvOrderRoundOff.setText(String.valueOf(CommonUtil.getDoubleFromString(String.valueOf(orderSummary.getSales().getRoundOff()), 2)));
                String strOrderNo = orderSummary.getSales().getPrefix() + String.valueOf(orderSummary.getSales().getSalesNo());
                orderDetailsBinding.tvOrderNoCombination.setText(strOrderNo);
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
        //String strBaseUrl = PreferenceManager.getDomain(OrderDetailsActivity.this);
        Api apiInterface = ApiGenerator.getApi(CommonUtil.tempBaseUrl).create(Api.class);
        orderSummaryViewModel = new ViewModelProvider(this, new OrderSummaryViewModelFactory(OrderSummaryRepository.getInstance(apiInterface), companyId, branchId, userId)).get(OrderSummaryViewModel.class);
    }

    @SuppressLint("ResourceAsColor")
    private void setQrCode(String tempOrderNo) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            //Bitmap bitmap = barcodeEncoder.encodeBitmap(String.valueOf(orderDetailsSalesNo), BarcodeFormat.CODE_128, 700, 150);
            Bitmap bitmap = barcodeEncoder.encodeBitmap(tempOrderNo, BarcodeFormat.CODE_128, 700, 150);
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
            orderDetailsBinding.tvOrderNo.setText(tempOrderNo);
        } catch (WriterException e) {
            e.printStackTrace();
            //orderDetailsBinding.tvStatus.setBackground(ContextCompat.getColor(OrderDetailsActivity.this, R.color.offwhite));
            //orderDetailsBinding.tvStatus.setTin
            Log.e(TAG, "onCreate: some interrupt ");
        }
    }
}