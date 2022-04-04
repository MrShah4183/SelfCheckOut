package com.vasyerp.selfcheckout.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.adapters.batch.BatchSelectionArrayAdapter;
import com.vasyerp.selfcheckout.adapters.cart_product.CartAdapter;
import com.vasyerp.selfcheckout.adapters.cart_product.SwipeToRemove;
import com.vasyerp.selfcheckout.adapters.getproductdata.AdapterGetProductData;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.api.razorpay.RazorpayApi;
import com.vasyerp.selfcheckout.api.razorpay.RazorpayApiAuthentication;
import com.vasyerp.selfcheckout.api.razorpay.RazorpayApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityMainBinding;
import com.vasyerp.selfcheckout.databinding.BottomSheetOrderSummaryBinding;
import com.vasyerp.selfcheckout.models.product.GetAllProducts;
import com.vasyerp.selfcheckout.models.product.Product;
import com.vasyerp.selfcheckout.models.product.ProductDto;
import com.vasyerp.selfcheckout.models.product.ProductVarientsDTO;
import com.vasyerp.selfcheckout.models.product.StockMasterVo;
import com.vasyerp.selfcheckout.models.razorpaymodel.order.PostOrderData;
import com.vasyerp.selfcheckout.models.razorpaymodel.order.SingleOrderModel;
import com.vasyerp.selfcheckout.models.savebill.SalesDTO;
import com.vasyerp.selfcheckout.models.savebill.SaveBill;
import com.vasyerp.selfcheckout.models.savebill.SaveBillResponse;
import com.vasyerp.selfcheckout.models.savebill.SaveBillStatusModel;
import com.vasyerp.selfcheckout.models.savebill.UpdateBillResponse;
import com.vasyerp.selfcheckout.repositories.MainRepository;
import com.vasyerp.selfcheckout.ui.CameraPermissionActivity;
import com.vasyerp.selfcheckout.ui.orders_ui.OrderDetailsActivity;
import com.vasyerp.selfcheckout.ui.orders_ui.OrdersListActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.utils.ScreenUtils;
import com.vasyerp.selfcheckout.viewmodels.main.MainViewModel;
import com.vasyerp.selfcheckout.viewmodels.main.MainViewModelFactory;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends CameraPermissionActivity implements PaymentResultListener {
    private static final String TAG = "MainActivity";

    /**
     * zxing barcode scanner
     */
    private CameraSettings cameraSettings;

    /**
     * mlkit barcode  scanner
     */
    //private AtomicBoolean atomicBoolean;


    private BottomSheetOrderSummaryBinding bottomSheetOrderSummaryBinding;
    //private BottomSheetBarcodeBinding bottomSheetBarcodeBinding;
    private BottomSheetDialog bottomSheetBillDetails;
    //BottomSheetDialog bottomSheetBarcode;

    String isShowing = "N";

    ActivityMainBinding activityMainBinding;
    KProgressHUD kProgressHUD;
    private String barcodeId;

    ArrayList<GetAllProducts> getAllProducts;
    List<StockMasterVo> cartItemsList;
    private ProductDto productDto;
    private ArrayList<StockMasterVo> stockMasterVos;
    ArrayList<Product> productArrayList;

    private double finalDisplayMrp = 0.0;
    private double backupFinalPrice = 0.0;

    private AdapterGetProductData adapterGetProductData;
    private BatchSelectionArrayAdapter batchSelectionArrayAdapter;
    CartAdapter cartAdapter;

    //DialogSelectProductBatchBinding dialogBatchSelectionBinding;

    private int companyId;
    private int userId;
    private int branchId;
    //private String domainName;

    private boolean isInternetConnected;

    //AlertDialog batchSelectionDialog;

    String newPattern = "(^|^-?)\\d+(\\.\\d+)?(?=$)|(?<=^)\\.\\d+(?=$)";
    //todo gst pattern String gstPatternRegex = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
    String numberPatternStr = "^\\d+$";
    String numberLengthRegex = "^.{10}$";
    String decimalRegex = "\\d+(\\.\\d{1,2})?";
    Pattern pattern, mobileNumberPattern, numberLengthReg, decimalPattern;
    NumberFormat numberFormat;
    private StockMasterVo selectedStockMasterVo;
    MainViewModel mainViewModel;
    int widthOfEtBarcode = 500;
    private SaveBillResponse saveBillResponseData;
    private SaveBill saveBill;

    public SaveBillResponse getSaveBillResponseData() {
        return saveBillResponseData;
    }

    public void setSaveBillResponseData(SaveBillResponse saveBillResponseData) {
        this.saveBillResponseData = saveBillResponseData;
    }

    public SaveBill getSaveBill() {
        return saveBill;
    }

    public void setSaveBill(SaveBill saveBill) {
        this.saveBill = saveBill;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public double getFinalDisplayMrp() {
        return finalDisplayMrp;
    }

    public void setFinalDisplayMrp(double finalDisplayMrp) {
        this.finalDisplayMrp = finalDisplayMrp;
    }

    public StockMasterVo getSelectedStockMasterVo() {
        return selectedStockMasterVo;
    }

    public void setSelectedStockMasterVo(StockMasterVo selectedStockMasterVo) {
        this.selectedStockMasterVo = selectedStockMasterVo;
    }

    public double getBackupFinalPrice() {
        return backupFinalPrice;
    }

    public void setBackupFinalPrice(double backupFinalPrice) {
        this.backupFinalPrice = backupFinalPrice;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolbarMain);

        /*storeName = "vasyERP ";
        storeImg = "https://s3-us-west-2.amazonaws.com/vasyerpsolutions/Gmart/pv_logo/logo-small.png";*/

        activityMainBinding.mainLlMain.setWeightSum(1.5f);
        activityMainBinding.zxQrDecoratedBarcodeViewMain.setVisibility(View.GONE);

        String strCompanyName = PreferenceManager.getCompanyBranchName(this);
        String strCompanyLogoPrefix = PreferenceManager.getCompanyLogoPrefix(this);
        String strCompanyLogoName = PreferenceManager.getCompanyLogo(this);
        if (strCompanyName != null) {
            activityMainBinding.tvMainCompanyName.setText(strCompanyName);
        }

        if (strCompanyLogoPrefix != null && strCompanyLogoName != null) {
            if (!strCompanyLogoPrefix.isEmpty() && !strCompanyLogoName.isEmpty()) {
                String logoPath = strCompanyLogoPrefix + strCompanyLogoName;
                Picasso.get().load(logoPath).into(activityMainBinding.ivMainCompanyImg);
            } else {
                Picasso.get().load(R.drawable.logo_dark).into(activityMainBinding.ivMainCompanyImg);
            }
        }

        pattern = Pattern.compile(newPattern, Pattern.DOTALL);
        numberLengthReg = Pattern.compile(numberLengthRegex, Pattern.DOTALL);
        mobileNumberPattern = Pattern.compile(numberPatternStr, Pattern.DOTALL);
        decimalPattern = Pattern.compile(decimalRegex, Pattern.DOTALL);
        numberFormat = NumberFormat.getInstance();
        companyId = Integer.parseInt(PreferenceManager.getCompanyId(this));
        branchId = Integer.parseInt(PreferenceManager.getBranchId(this));
        userId = Integer.parseInt(PreferenceManager.getUserId(this));
        //todo remove not used domainName = PreferenceManager.getDomain(this);

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(MainActivity.this);
        connectivityStatus.observe(this, aBoolean -> {
            isInternetConnected = aBoolean;
            if (aBoolean) {
                if (activityMainBinding.tvMainCheckInternet.getText().toString().equals(getString(R.string.toast_no_internet))) {
                    activityMainBinding.tvMainCheckInternet.setText(getString(R.string.toast_internet));
                    activityMainBinding.tvMainCompanyName.setTextColor(R.color.green);
                    new Handler().postDelayed(() -> activityMainBinding.tvMainCheckInternet.setVisibility(View.GONE), 1000);
                }
            } else {
                activityMainBinding.tvMainCheckInternet.setText(getString(R.string.toast_no_internet));
                activityMainBinding.tvMainCheckInternet.setTextColor(R.color.green);
                activityMainBinding.tvMainCheckInternet.setVisibility(View.VISIBLE);
            }
        });

        getWidthOfEditText();
        initArrayLists();
        initKProgressHud();
        initViewModelAndRepository();

        initBillDetailsBinding();
        initBottomSheetBillDetails();

        barcodeScannerViewSelection();

        setUpAdapterGetProductData();

        viewObserversCollection();

        initCartAdapter();

        batchSelectionArrayAdapter = new BatchSelectionArrayAdapter(MainActivity.this, stockMasterVos);
        //todo not use this code because of we don't show batch dialog for selecting batch
        /*dialogBatchSelectionBinding.spinnerBatchSelection.setAdapter(batchSelectionArrayAdapter);
        dialogBatchSelectionBinding.spinnerBatchSelection.setSelection(0);
        dialogBatchSelectionBinding.spinnerBatchSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedStockMasterVo((StockMasterVo) parent.getItemAtPosition(position));
                getSelectedStockMasterVo().setOldQuantity(getSelectedStockMasterVo().getQuantity());
//                if (getSelectedStockMasterVo().getHasNegativeSelling() == 1) {
//                    if (Double.valueOf(getSelectedStockMasterVo().getQuantity()).doubleValue() <= 0.0) {
//                        setSelectedStockMasterVo(null);
//                        batchSelectionDialog.dismiss();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                CommonUtil.showSnackBar(activityMainBinding.mainLl, activityMainBinding.llTotal, "Selected product has 1 batch with zero quantity.");
//                            }
//                        }, 500);
//                    }
//                }
//                Log.d(TAG, "onItemSelected: Selected batch: " + (StockMasterVo) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialogBatchSelectionBinding.dialogBtnCancelSelectBatchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batchSelectionDialog.dismiss();
                resumeScannerCases();
            }
        });

        dialogBatchSelectionBinding.dialogBtnSaveSelectBatchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedBatchCalculation();
                resumeScannerCases();
            }
        });*/

        enableSwipeToDeleteAndUndo();

        activityMainBinding.btnMainOnOff.setOnClickListener(v -> {
            if (isShowing.equals("Y")) {
                pauseScannerCases();
                hideScannerCases();
                isShowing = "N";
            } else {
                showScannerCases();
                resumeScannerCases();
                isShowing = "Y";
            }
            Log.v("Hide", "btn click end");
        });

        activityMainBinding.zxQrDecoratedBarcodeViewMain.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                pauseScannerCases();
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 150);
                Log.d("MainActivity", "Barcode Result from zxing: " + result.toString());
                if (kProgressHUD != null && !kProgressHUD.isShowing()) {
                    kProgressHUD.show();
                }
                barcodeId = result.toString();
                //String financialYear, String productId, boolean isSearchByBarcode) {
                if (isInternetConnected) {
                    mainViewModel.getProductByBarcodeId(getCurrentFinancialYear(), barcodeId, true);
                } else {
                    kProgressHUD.dismiss();
                    resumeScannerCases();
                    CommonUtil.showSnackBar(activityMainBinding.llBtn, activityMainBinding.llBtn, getString(R.string.toast_no_internet));
                }
                //activityMainBinding.zxQrDecoratedBarcodeViewMain.pause();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        activityMainBinding.btnMainCheckOut.setOnClickListener(v -> {
            if (isInternetConnected) {
                if (cartItemsList.size() > 0) {
                    //Toast.makeText(MainActivity.this, "Show bottom sheet", Toast.LENGTH_SHORT).show();
                    this.bottomSheetOrderSummaryBinding.tvOrderTotal.setText(activityMainBinding.tvTotalAmount.getText());
                    this.bottomSheetOrderSummaryBinding.tvOrderRoundOff.setText(activityMainBinding.etRound.getText());
                    String totalItems = String.valueOf(cartItemsList.size());
                    totalItems += ".0";
                    this.bottomSheetOrderSummaryBinding.tvOrderTotalItems.setText(totalItems);
                    double totalQty = 0.0;
                    double totalTax = 0.0;
                    for (int i = 0; i < cartItemsList.size(); i++) {
                        totalQty += CommonUtil.getDoubleFromString(cartItemsList.get(i).getQuantity(), 2);
                        totalTax += cartItemsList.get(i).getTotalTaxPrice();
                    }
                /*salesDTO.setTaxId(cartItemsList.get(i).getProductDto().getTax_id());
            salesDTO.setTaxRate(cartItemsList.get(i).getProductDto().getTax_rate());
            salesDTO.setTaxAmount(Double.parseDouble(String.format("%.3f", cartItemsList.get(i).getTotalTaxPrice())));
            salesDTO.setPrice(Double.parseDouble(String.format("%.3f", cartItemsList.get(i).getPrice())));
            salesDTO.setNetAmount(Double.parseDouble(String.format("%.3f", cartItemsList.get(i).getDisplayMrp())));*/
                    this.bottomSheetOrderSummaryBinding.tvOrderTotalQty.setText(String.valueOf(totalQty));
                    this.bottomSheetOrderSummaryBinding.tvOrderTotalTax.setText(String.valueOf(CommonUtil.getDoubleFromString(String.valueOf(totalTax), 2)));
                    this.bottomSheetBillDetails.show();
                } else {
                    CommonUtil.showSnackBar(activityMainBinding.llTotal, activityMainBinding.llTotal, "Cart is empty.");
                }
            } else {
                CommonUtil.showSnackBar(activityMainBinding.llBtn, activityMainBinding.llBtn, getString(R.string.toast_no_internet));
            }
        });

        activityMainBinding.etRound.addTextChangedListener(new TextWatcher() {
            String result = "";
            boolean isMatches = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SneakyThrows
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Matcher matcher = pattern.matcher(s.toString().trim());
                Log.d("MainActivity", "onTextChanged: pattern is: " + pattern);
                if (matcher.matches()) {
                    result = matcher.group(0);
                    Log.d("MainActivity", "onTextChanged result: " + matcher.group(0));
                    isMatches = matcher.matches();
                }
            }

            @SneakyThrows
            @Override
            public void afterTextChanged(Editable s) {

                double tempVal = 0;
                boolean isNegative = false;
                setFinalDisplayMrp(getBackupFinalPrice());

                if (!result.isEmpty() && !s.toString().isEmpty()) {
                    if (result.contains("-") && result.toString().length() > 1 && s.toString().length() > 1) {
                        String[] ar = result.split("-");
                        if (ar.length == 2) {
                            tempVal = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", numberFormat.parse(result.split("-")[1]).doubleValue()));
                            isNegative = true;
                        }
                    } else {
                        tempVal = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", numberFormat.parse(result).doubleValue()));
                        isNegative = false;
                    }

                    if (tempVal > getFinalDisplayMrp()) {
                        activityMainBinding.etRound.setError("Round off must not be greater than payable amount.");
                        setFinalDisplayMrp(getBackupFinalPrice());
                        //if (getFinalDisplayMrp())
                        if (getFinalDisplayMrp() % 1 == 0) {
                            activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
                        } else {
                            activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", getFinalDisplayMrp()));
                        }
                    } else {
                        if (isNegative) {
                            setFinalDisplayMrp(getFinalDisplayMrp() - tempVal);
                            if (getFinalDisplayMrp() % 1 == 0) {
                                activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
                            } else {
                                activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", getFinalDisplayMrp()));
                            }
                            //activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
                        } else {
                            setFinalDisplayMrp(getFinalDisplayMrp() + tempVal);
                            if (getFinalDisplayMrp() % 1 == 0) {
                                activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
                            } else {
                                activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", getFinalDisplayMrp()));
                            }
                            //activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
                        }
                    }
                } else {
                    setFinalDisplayMrp(getBackupFinalPrice());
                    if (getFinalDisplayMrp() % 1 == 0) {
                        activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
                    } else {
                        activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", getFinalDisplayMrp()));
                    }
                    //activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
                    //activityMainBinding.etRound.setText("0");
                }

            }
        });

        activityMainBinding.etRound.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pauseScannerCases();
                    hideScannerCases();
                    isShowing = "N";
                }
            }
        });

        //activityMainBinding.btnOrderCancle.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Remove All items", Toast.LENGTH_SHORT).show());

    }

    private void getWidthOfEditText() {
        ViewTreeObserver vto = activityMainBinding.tilScanBarCode.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    activityMainBinding.tilScanBarCode.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    //this.layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    activityMainBinding.tilScanBarCode.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    //this.layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = activityMainBinding.tilScanBarCode.getMeasuredWidth();
                int height = activityMainBinding.tilScanBarCode.getMeasuredHeight();
                Log.e(TAG, "onGlobalLayout: " + width);
                Log.e(TAG, "onGlobalLayout: " + height);
                widthOfEtBarcode = width;
            }
        });
    }

    private void initViewModelAndRepository() {
        Api apiInterface = ApiGenerator.getApi(CommonUtil.tempBaseUrl).create(Api.class);
        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(MainRepository.getInstance(apiInterface), companyId, branchId, userId)).get(MainViewModel.class);
    }

    private void initKProgressHud() {
        kProgressHUD = CommonUtil.getKProgressHud(MainActivity.this);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setDimAmount(0.25f);
    }

    private void discardCart() {
        if (cartAdapter != null && cartItemsList != null) {
            new MaterialAlertDialogBuilder(MainActivity.this)
                    .setMessage("Do, you really want to discard the items?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activityMainBinding.etRound.setText("0.0");
                            activityMainBinding.tvTotalAmount.setText("0.0");
                            activityMainBinding.tvCount.setText("0");
                            cartItemsList.clear();
                            cartAdapter.notifyDataSetChanged();
                            setBackupFinalPrice(00.00);
                            if (batchSelectionArrayAdapter != null) {
                                batchSelectionArrayAdapter.clear();
                            }
                            if (getSelectedStockMasterVo() != null) {
                                setSelectedStockMasterVo(null);
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private String getCurrentFinancialYear() {
        String fiscalyear = "";
        if ((Calendar.getInstance().get(Calendar.MONTH) + 1) <= 3) {
            fiscalyear = (Calendar.getInstance().get(Calendar.YEAR) - 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
        } else {
            fiscalyear = Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.YEAR) + 1);
        }
        return fiscalyear;
    }

    private void initCartAdapter() {
        cartAdapter = new CartAdapter(this, cartItemsList);
        cartAdapter.setCartQtyFocusCallback(hasFocus -> {
            if (hasFocus) {
                pauseScannerCases();
                hideScannerCases();
                isShowing = "N";
            }
        });
        activityMainBinding.rvCart.setAdapter(cartAdapter);
        cartAdapter.setItemQtyCallback((payableAmount, cartItems) -> {
            String roundOffStr = (Math.round(payableAmount) > payableAmount) ? String.format(Locale.getDefault(), "%.2f", Math.round(payableAmount) - payableAmount) : String.format(Locale.getDefault(), "%.2f", Math.round(payableAmount) - payableAmount);
            MainActivity.this.setFinalDisplayMrp(Math.round(payableAmount) - Double.parseDouble(roundOffStr));
            setBackupFinalPrice(Math.round(payableAmount) - Double.parseDouble(roundOffStr));

            activityMainBinding.etRound.setText(roundOffStr);
            //activityMainBinding.tvTotalAmount.setText(String.valueOf(String.format(Locale.getDefault(), "%.0f", MainActivity.this.getFinalDisplayMrp())));
            if (MainActivity.this.getFinalDisplayMrp() % 1 == 0) {
                activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.0f", getFinalDisplayMrp()));
            } else {
                activityMainBinding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", getFinalDisplayMrp()));
            }
            activityMainBinding.tvCount.setText(String.valueOf(cartItems));
        });
    }

    private void setUpAdapterGetProductData() {
        adapterGetProductData = new AdapterGetProductData(MainActivity.this, getAllProducts);
        //mainBinding.etBarcode.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        //widthOfEtBarcode = 500;
        Log.e(TAG, "setUpAdapterGetProductData:1 " + widthOfEtBarcode);
        new Handler().postDelayed(() -> {
            Log.e(TAG, "setUpAdapterGetProductData run: " + widthOfEtBarcode);
            Log.e(TAG, "setUpAdapterGetProductData:" + activityMainBinding.tilScanBarCode.getLayoutParams().width);
            activityMainBinding.etBarcode.setDropDownWidth(widthOfEtBarcode);
        }, 1000);
        Log.e(TAG, "setUpAdapterGetProductData:2 " + widthOfEtBarcode);
        activityMainBinding.etBarcode.setDropDownWidth(widthOfEtBarcode);
        activityMainBinding.etBarcode.setAdapter(adapterGetProductData);
        activityMainBinding.etBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    InputMethodManager methodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    //methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                    methodManager.hideSoftInputFromWindow(activityMainBinding.etBarcode.getWindowToken(), 0);
                    //methodManager.hideSoftInputFromWindow(this,0);
                    return false;
                }
                return true;
            }
        });
        activityMainBinding.etBarcode.setOnItemClickListener((parent, view, position, id) -> {
            GetAllProducts getAllProducts = (GetAllProducts) parent.getItemAtPosition(position);
            Log.d("ProductName", "onItemClick: " + getAllProducts.getId() + "name " + getAllProducts.getValue());
            activityMainBinding.etBarcode.setText("");
            if (!kProgressHUD.isShowing() && kProgressHUD != null) {
                kProgressHUD.show();
            }
            if (isInternetConnected) {
                mainViewModel.getProductByBarcodeId(getCurrentFinancialYear(), String.valueOf(getAllProducts.getId()), false);
            } else {
                kProgressHUD.dismiss();
                CommonUtil.showSnackBar(activityMainBinding.llBtn, activityMainBinding.llBtn, getString(R.string.toast_no_internet));
            }
        });

        activityMainBinding.etBarcode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                pauseScannerCases();
                hideScannerCases();
                isShowing = "N";
            }
        });
    }

    private void saveSelectedBatchCalculation() {
        Log.d(TAG, "onClick: Tax and display price calculation.");

        if (getSelectedStockMasterVo().getHasNegativeSelling() == 1) {
            double quantityForSelectedStock = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", Double.parseDouble(getSelectedStockMasterVo().getQuantity())));
            if (quantityForSelectedStock == 0.0) {
                CommonUtil.showSnackBar(activityMainBinding.llTotal, activityMainBinding.llTotal, "Product has zero stock quantity and negative selling is off.");
            } else {
                cartItemCalculations();
            }
        } else {
            cartItemCalculations();
        }
        //todo remove not used code
        /*if (batchSelectionDialog.isShowing()) {
            batchSelectionDialog.dismiss();
        }*/
        batchSelectionArrayAdapter.clear();
    }

    private void cartItemCalculations() {
        if (cartItemsList.size() > 0) {
            double tempQty = 0.0;
            boolean isItemFound = false;
            double netMrp = 0.0, taxPrice = 0.0, displayPrice = 0.0, totalTaxPrice = 0.0, taxRate = 0.0, taxRateAdd = 0.0, netMrpHelp = 0.0, tempTotal = 0.0, batchDiscount = 0.0, taxableAmount = 0.0;
            double discountPercent = 0.0;
            double mrpToDiscount = 0.0;
            for (int i = 0; i < cartItemsList.size(); i++) {
                if (cartItemsList.get(i).getBatchNo().equals(getSelectedStockMasterVo().getBatchNo())) {
                    tempQty += Double.parseDouble(cartItemsList.get(i).getQuantity());
                    tempQty += 1;
                    cartItemsList.get(i).setQuantity(String.valueOf(tempQty));
                    displayPrice = cartItemsList.get(i).getDisplayMrp();
                    displayPrice = displayPrice + cartItemsList.get(i).getTaxableAmount() + cartItemsList.get(i).getTaxPrice();
                    totalTaxPrice = cartItemsList.get(i).getTotalTaxPrice();
                    totalTaxPrice += cartItemsList.get(i).getTaxPrice();
                    cartItemsList.get(i).setDisplayMrp(displayPrice);
                    cartItemsList.get(i).setTotalTaxPrice(totalTaxPrice);
                    isItemFound = true;
                    if (cartAdapter != null) {
                        cartAdapter.notifyItemChanged(i);
                        activityMainBinding.rvCart.getLayoutManager().scrollToPosition(i);
                    }
                }
            }
            if (!isItemFound) {
                if (getSelectedStockMasterVo().getHasNegativeSelling() == 1) {
                    if (Double.valueOf(getSelectedStockMasterVo().getQuantity()).doubleValue() <= 0.0) {
                        //if (Double.parseDouble(getSelectedStockMasterVo().getQuantity()) <= 0.0) {
                        setSelectedStockMasterVo(null);
                        //batchSelectionDialog.dismiss();
                        CommonUtil.showSnackBar(activityMainBinding.mainLlMain, activityMainBinding.llTotal, "Selected batch has zero quantity.");
                    } else {
                        getSelectedStockMasterVo().setQuantity("1.0");
                        taxRate = getProductDto().getTax_rate();
                        if (getProductDto().getTax_included() == 1) {
                            Log.d(TAG, "onClick: ------------------Item not available and added to Cart and negative selling not allowed: Tax Included-------------------");
                            Log.d(TAG, "onClick: TaxRateAdd: " + String.valueOf(taxRateAdd));

                            taxRateAdd = 100 + taxRate;
                            Log.d(TAG, "onClick: taxRateAdd = 100 + taxRate: " + String.valueOf(taxRateAdd));

                            netMrp = getSelectedStockMasterVo().getMrp();
                            Log.d(TAG, "onClick: netMrp = getSelectedStockMasterVo().getMrp(): " + netMrp);

                            batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                            Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                            mrpToDiscount = batchDiscount;
                            Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                            netMrpHelp = 100 / taxRateAdd;
                            Log.d(TAG, "onClick: netMrpHelp = 100 / taxRateAdd: " + netMrpHelp);

                            netMrp = netMrp * netMrpHelp; // final net mrp with tax excluded
                            Log.d(TAG, "onClick: netMrp = netMrp * netMrpHelp: " + netMrp);

                            discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp();
                            double result = Double.isNaN(discountPercent) ? 0.0 : discountPercent;
                            result = Double.isInfinite(result) ? 0.0 : result;
                            Log.d(TAG, "onClick: discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp(): " + result);

                            batchDiscount = (netMrp * result) / 100;
                            Log.d(TAG, "onClick: batchDiscount = (netMrp * discountPercent) / 100: " + result);

                            taxableAmount = netMrp - batchDiscount;
                            Log.d(TAG, "onClick: taxableAmount = netMrp - batchDiscount: " + taxableAmount);

                            taxPrice = taxableAmount * taxRate;
                            Log.d(TAG, "onClick: taxPrice = taxableAmount * taxRate: " + taxPrice);

                            taxPrice = taxPrice / 100; // final tax price
                            Log.d(TAG, "onClick: taxPrice = taxPrice / 100: " + taxPrice);

                            displayPrice = taxableAmount + taxPrice;
                            Log.d(TAG, "onClick: displayPrice = netMrp + taxPrice: " + displayPrice);

                            totalTaxPrice = taxPrice;
                            Log.d(TAG, "onClick: totalTaxPrice = taxPrice: " + totalTaxPrice);

                            getSelectedStockMasterVo().setDiscountType("amount");
                            getSelectedStockMasterVo().setDiscount(batchDiscount);
                            getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                            getSelectedStockMasterVo().setTaxableAmount(taxableAmount);
                            getSelectedStockMasterVo().setNetMrp(netMrp);
                            getSelectedStockMasterVo().setPrice(netMrp);
                            getSelectedStockMasterVo().setTaxPrice(taxPrice);
                            getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                            getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                            getSelectedStockMasterVo().setProductDto(getProductDto());

                        } else {
                            Log.d(TAG, "onClick: ------------------Item not available and added to Cart and negative selling allowed: Tax not included-------------------");
                            netMrp = getSelectedStockMasterVo().getMrp();
                            Log.d(TAG, "onClick: netMrp: " + netMrp);

                            batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                            Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                            mrpToDiscount = batchDiscount;
                            Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                            netMrp = netMrp - batchDiscount;
                            Log.d(TAG, "onClick: netMrp with discount: " + netMrp);

                            taxPrice = netMrp * taxRate;
                            Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                            taxPrice = taxPrice / 100;
                            Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                            displayPrice = netMrp + taxPrice;
                            Log.d(TAG, "onClick: displayPrice: " + displayPrice);

                            totalTaxPrice = taxPrice;
                            Log.d(TAG, "onClick: totalTaxPrice: " + totalTaxPrice);

                            getSelectedStockMasterVo().setDiscountType("amount");
                            getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                            getSelectedStockMasterVo().setDiscount(batchDiscount);
                            getSelectedStockMasterVo().setNetMrp(netMrp);
                            getSelectedStockMasterVo().setTaxableAmount(netMrp);
                            getSelectedStockMasterVo().setPrice(getSelectedStockMasterVo().getMrp());
                            getSelectedStockMasterVo().setTaxPrice(taxPrice);
                            getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                            getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                            getSelectedStockMasterVo().setProductDto(getProductDto());
                        }
                        cartItemsList.add(getSelectedStockMasterVo());
                        if (cartAdapter != null) {
                            cartAdapter.notifyItemChanged(cartItemsList.size() - 1);
                            activityMainBinding.rvCart.getLayoutManager().scrollToPosition(cartItemsList.size() - 1);
                        }
                    }
                } else {
                    getSelectedStockMasterVo().setQuantity("1.0");
                    taxRate = getProductDto().getTax_rate();
                    if (getProductDto().getTax_included() == 1) {
                        Log.d(TAG, "onClick: ------------------Empty Cart and negative selling allowed: Tax Included-------------------");
                        Log.d(TAG, "onClick: TaxRateAdd: " + String.valueOf(taxRateAdd));

                        taxRateAdd = 100 + taxRate;
                        Log.d(TAG, "onClick: taxRateAdd = 100 + taxRate: " + String.valueOf(taxRateAdd));

                        netMrp = getSelectedStockMasterVo().getMrp();
                        Log.d(TAG, "onClick: netMrp = getSelectedStockMasterVo().getMrp(): " + netMrp);

                        batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                        Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                        mrpToDiscount = batchDiscount;
                        Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                        netMrpHelp = 100 / taxRateAdd;
                        Log.d(TAG, "onClick: netMrpHelp = 100 / taxRateAdd: " + netMrpHelp);

                        netMrp = netMrp * netMrpHelp; // final net mrp with tax excluded
                        Log.d(TAG, "onClick: netMrp = netMrp * netMrpHelp: " + netMrp);

                        discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp();
                        double result = Double.isNaN(discountPercent) ? 0.0 : discountPercent;
                        result = Double.isInfinite(result) ? 0.0 : result;
                        Log.d(TAG, "onClick: discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp(): " + result);

                        batchDiscount = (netMrp * result) / 100;
                        Log.d(TAG, "onClick: batchDiscount = (netMrp * discountPercent) / 100: " + result);

                        taxableAmount = netMrp - batchDiscount;
                        Log.d(TAG, "onClick: taxableAmount = netMrp - batchDiscount: " + taxableAmount);

                        taxPrice = taxableAmount * taxRate;
                        Log.d(TAG, "onClick: taxPrice = taxableAmount * taxRate: " + taxPrice);

                        taxPrice = taxPrice / 100; // final tax price
                        Log.d(TAG, "onClick: taxPrice = taxPrice / 100: " + taxPrice);

                        displayPrice = taxableAmount + taxPrice;
                        Log.d(TAG, "onClick: displayPrice = netMrp + taxPrice: " + displayPrice);

                        totalTaxPrice = taxPrice;
                        Log.d(TAG, "onClick: totalTaxPrice = taxPrice: " + totalTaxPrice);

                        getSelectedStockMasterVo().setDiscountType("amount");
                        getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                        getSelectedStockMasterVo().setDiscount(batchDiscount);
                        getSelectedStockMasterVo().setTaxableAmount(taxableAmount);
                        getSelectedStockMasterVo().setNetMrp(netMrp);
                        getSelectedStockMasterVo().setPrice(netMrp);
                        getSelectedStockMasterVo().setTaxPrice(taxPrice);
                        getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                        getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                        getSelectedStockMasterVo().setProductDto(getProductDto());

                    } else {
                        Log.d(TAG, "onClick: --------Empty cart and negative selling allowed: Tax Not included------------");
                        netMrp = getSelectedStockMasterVo().getMrp();
                        Log.d(TAG, "onClick: netMrp: " + netMrp);

                        batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                        Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                        mrpToDiscount = batchDiscount;
                        Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                        netMrp = netMrp - batchDiscount;
                        Log.d(TAG, "onClick: netMrp with discount: " + netMrp);

                        taxPrice = netMrp * taxRate;
                        Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                        taxPrice = taxPrice / 100;
                        Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                        displayPrice = netMrp + taxPrice;
                        Log.d(TAG, "onClick: displayPrice: " + displayPrice);

                        totalTaxPrice = taxPrice;
                        Log.d(TAG, "onClick: totalTaxPrice: " + totalTaxPrice);

                        getSelectedStockMasterVo().setDiscountType("amount");
                        getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                        getSelectedStockMasterVo().setDiscount(batchDiscount);
                        getSelectedStockMasterVo().setNetMrp(netMrp);
                        getSelectedStockMasterVo().setTaxableAmount(netMrp);
                        getSelectedStockMasterVo().setPrice(getSelectedStockMasterVo().getMrp());
                        getSelectedStockMasterVo().setTaxPrice(taxPrice);
                        getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                        getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                        getSelectedStockMasterVo().setProductDto(getProductDto());

                    }
                    cartItemsList.add(getSelectedStockMasterVo());
                    if (cartAdapter != null) {
                        cartAdapter.notifyItemChanged(cartItemsList.size() - 1);
                        activityMainBinding.rvCart.getLayoutManager().scrollToPosition(cartItemsList.size() - 1);
                    }
                }
            }
        } else {
            if (getSelectedStockMasterVo().getHasNegativeSelling() == 1) {
                if (Double.valueOf(getSelectedStockMasterVo().getQuantity()).doubleValue() <= 0.0) {
                    setSelectedStockMasterVo(null);
                    //batchSelectionDialog.dismiss();
                    CommonUtil.showSnackBar(activityMainBinding.mainLlMain, activityMainBinding.llTotal, "Selected batch has zero quantity.");
                } else {
                    double netMrp = 0.0, taxPrice = 0.0, displayPrice = 0.0, totalTaxPrice = 0.0, taxRate = 0.0, taxRateAdd = 0.0, netMrpHelp = 0.0, tempTotal = 0.0, batchDiscount = 0.0, taxableAmount = 0.0;
                    double mrpToDiscount = 0.0;
                    double discountPercent = 0.0;
                    getSelectedStockMasterVo().setQuantity("1.0");
                    taxRate = getProductDto().getTax_rate();
                    if (getProductDto().getTax_included() == 1) {
                        Log.d(TAG, "onClick: ------------------Empty Cart and negative selling not allowed: Tax Included-------------------");
                        Log.d(TAG, "onClick: TaxRateAdd: " + String.valueOf(taxRateAdd));

                        taxRateAdd = 100 + taxRate;
                        Log.d(TAG, "onClick: taxRateAdd = 100 + taxRate: " + String.valueOf(taxRateAdd));

                        netMrp = getSelectedStockMasterVo().getMrp();
                        Log.d(TAG, "onClick: netMrp = getSelectedStockMasterVo().getMrp(): " + netMrp);

                        Log.d(TAG, "onClick: netMrp = getSelectedStockMasterVo().getSellingPrice(): " + getSelectedStockMasterVo().getSellingPrice());

                        batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                        Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                        mrpToDiscount = batchDiscount;
                        Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                        netMrpHelp = 100 / taxRateAdd;
                        Log.d(TAG, "onClick: netMrpHelp = 100 / taxRateAdd: " + netMrpHelp);

                        netMrp = netMrp * netMrpHelp; // final net mrp with tax excluded
                        Log.d(TAG, "onClick: netMrp = netMrp * netMrpHelp: " + netMrp);

                        discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp();
                        double result = Double.isNaN(discountPercent) ? 0.0 : discountPercent;
                        result = Double.isInfinite(result) ? 0.0 : result;
                        Log.d(TAG, "onClick: discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp(): " + result);

                        batchDiscount = (netMrp * result) / 100;
                        Log.d(TAG, "onClick: batchDiscount = (netMrp * discountPercent) / 100: " + result);


                        taxableAmount = netMrp - batchDiscount;
                        Log.d(TAG, "onClick: taxableAmount = netMrp - batchDiscount: " + taxableAmount);

                        taxPrice = taxableAmount * taxRate;
                        Log.d(TAG, "onClick: taxPrice = taxableAmount * taxRate: " + taxPrice);

                        taxPrice = taxPrice / 100; // final tax price
                        Log.d(TAG, "onClick: taxPrice = taxPrice / 100: " + taxPrice);

                        displayPrice = taxableAmount + taxPrice;
                        Log.d(TAG, "onClick: displayPrice = netMrp + taxPrice: " + displayPrice);

                        totalTaxPrice = taxPrice;
                        Log.d(TAG, "onClick: totalTaxPrice = taxPrice: " + totalTaxPrice);

                        getSelectedStockMasterVo().setDiscountType("amount");
                        getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                        getSelectedStockMasterVo().setDiscount(batchDiscount);
                        getSelectedStockMasterVo().setTaxableAmount(taxableAmount);
                        getSelectedStockMasterVo().setNetMrp(netMrp);
                        getSelectedStockMasterVo().setPrice(netMrp);
                        getSelectedStockMasterVo().setTaxPrice(taxPrice);
                        getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                        getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                        getSelectedStockMasterVo().setProductDto(getProductDto());

                    } else {
                        Log.d(TAG, "onClick: --------Empty cart and negative selling not allowed: Tax Not included------------");
                        netMrp = getSelectedStockMasterVo().getMrp();
                        Log.d(TAG, "onClick: netMrp: " + netMrp);

                        batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                        Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                        mrpToDiscount = batchDiscount;
                        Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                        netMrp = netMrp - batchDiscount;
                        Log.d(TAG, "onClick: netMrp with discount: " + netMrp);

                        taxPrice = netMrp * taxRate;
                        Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                        taxPrice = taxPrice / 100;
                        Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                        displayPrice = netMrp + taxPrice;
                        Log.d(TAG, "onClick: displayPrice: " + displayPrice);

                        totalTaxPrice = taxPrice;
                        Log.d(TAG, "onClick: totalTaxPrice: " + totalTaxPrice);

                        getSelectedStockMasterVo().setDiscountType("amount");
                        getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                        getSelectedStockMasterVo().setDiscount(batchDiscount);
                        getSelectedStockMasterVo().setNetMrp(netMrp);
                        getSelectedStockMasterVo().setTaxableAmount(netMrp);
                        getSelectedStockMasterVo().setPrice(getSelectedStockMasterVo().getMrp());
                        getSelectedStockMasterVo().setTaxPrice(taxPrice);
                        getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                        getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                        getSelectedStockMasterVo().setProductDto(getProductDto());

                    }
                    cartItemsList.add(getSelectedStockMasterVo());
                    if (cartAdapter != null) {
                        cartAdapter.notifyItemChanged(cartItemsList.size() - 1);
                        activityMainBinding.rvCart.getLayoutManager().scrollToPosition(cartItemsList.size() - 1);
                    }
                }
            } else {
                double netMrp = 0.0, taxPrice = 0.0, displayPrice = 0.0, totalTaxPrice = 0.0, taxRate = 0.0, taxRateAdd = 0.0, netMrpHelp = 0.0, tempTotal = 0.0, batchDiscount = 0.0, taxableAmount = 0.0;
                double discountPercent = 0.0;
                double mrpToDiscount = 0.0;
                getSelectedStockMasterVo().setQuantity("1.0");
                taxRate = getProductDto().getTax_rate();
                if (getProductDto().getTax_included() == 1) {
                    Log.d(TAG, "onClick: ------------------Empty Cart and negative selling allowed: Tax Included-------------------");
                    Log.d(TAG, "onClick: TaxRateAdd: " + String.valueOf(taxRateAdd));

                    taxRateAdd = 100 + taxRate;
                    Log.d(TAG, "onClick: taxRateAdd = 100 + taxRate: " + String.valueOf(taxRateAdd));

                    netMrp = getSelectedStockMasterVo().getMrp();
                    Log.d(TAG, "onClick: netMrp = getSelectedStockMasterVo().getMrp(): " + netMrp);

                    batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                    Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                    mrpToDiscount = batchDiscount;
                    Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                    netMrpHelp = 100 / taxRateAdd;
                    Log.d(TAG, "onClick: netMrpHelp = 100 / taxRateAdd: " + netMrpHelp);

                    netMrp = netMrp * netMrpHelp; // final net mrp with tax excluded
                    Log.d(TAG, "onClick: netMrp = netMrp * netMrpHelp: " + netMrp);

                    discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp();
                    double result = Double.isNaN(discountPercent) ? 0.0 : discountPercent;
                    result = Double.isInfinite(result) ? 0.0 : result;
                    Log.d(TAG, "onClick: discountPercent = (batchDiscount * 100) / getSelectedStockMasterVo().getMrp(): " + result);

                    batchDiscount = (netMrp * result) / 100;
                    Log.d(TAG, "onClick: batchDiscount = (netMrp * discountPercent) / 100: " + result);

                    taxableAmount = netMrp - batchDiscount;
                    Log.d(TAG, "onClick: taxableAmount = netMrp - batchDiscount: " + taxableAmount);

                    taxPrice = taxableAmount * taxRate;
                    Log.d(TAG, "onClick: taxPrice = taxableAmount * taxRate: " + taxPrice);

                    taxPrice = taxPrice / 100; // final tax price
                    Log.d(TAG, "onClick: taxPrice = taxPrice / 100: " + taxPrice);

                    displayPrice = taxableAmount + taxPrice;
                    Log.d(TAG, "onClick: displayPrice = netMrp + taxPrice: " + displayPrice);

                    totalTaxPrice = taxPrice;
                    Log.d(TAG, "onClick: totalTaxPrice = taxPrice: " + totalTaxPrice);

                    getSelectedStockMasterVo().setDiscountType("amount");
                    getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                    getSelectedStockMasterVo().setDiscount(batchDiscount);
                    getSelectedStockMasterVo().setTaxableAmount(taxableAmount);
                    getSelectedStockMasterVo().setNetMrp(netMrp);
                    getSelectedStockMasterVo().setPrice(netMrp);
                    getSelectedStockMasterVo().setTaxPrice(taxPrice);
                    getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                    getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                    getSelectedStockMasterVo().setProductDto(getProductDto());

                } else {
                    Log.d(TAG, "onClick: --------Empty cart and negative selling allowed: Tax Not included------------");
                    netMrp = getSelectedStockMasterVo().getMrp();
                    Log.d(TAG, "onClick: netMrp: " + netMrp);

                    batchDiscount = getSelectedStockMasterVo().getMrp() - getSelectedStockMasterVo().getSellingPrice();
                    Log.d(TAG, "onClick: Batch discount: " + batchDiscount);

                    mrpToDiscount = batchDiscount;
                    Log.d(TAG, "onClick: MrpToDiscount: " + mrpToDiscount);

                    netMrp = netMrp - batchDiscount;
                    Log.d(TAG, "onClick: netMrp with discount: " + netMrp);

                    taxPrice = netMrp * taxRate;
                    Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                    taxPrice = taxPrice / 100;
                    Log.d(TAG, "onClick: taxPrice: " + taxPrice);

                    displayPrice = netMrp + taxPrice;
                    Log.d(TAG, "onClick: displayPrice: " + displayPrice);

                    totalTaxPrice = taxPrice;
                    Log.d(TAG, "onClick: totalTaxPrice: " + totalTaxPrice);

                    getSelectedStockMasterVo().setDiscountType("amount");
                    getSelectedStockMasterVo().setMrpToDiscount(CommonUtil.getDoubleDecimalValue(mrpToDiscount));
                    getSelectedStockMasterVo().setDiscount(batchDiscount);
                    getSelectedStockMasterVo().setNetMrp(netMrp);
                    getSelectedStockMasterVo().setTaxableAmount(netMrp);
                    getSelectedStockMasterVo().setPrice(getSelectedStockMasterVo().getMrp());
                    getSelectedStockMasterVo().setTaxPrice(taxPrice);
                    getSelectedStockMasterVo().setDisplayMrp(displayPrice);
                    getSelectedStockMasterVo().setTotalTaxPrice(totalTaxPrice);
                    getSelectedStockMasterVo().setProductDto(getProductDto());
                }
                cartItemsList.add(getSelectedStockMasterVo());
                if (cartAdapter != null) {
                    cartAdapter.notifyItemChanged(cartItemsList.size() - 1);
                    activityMainBinding.rvCart.getLayoutManager().scrollToPosition(cartItemsList.size() - 1);
                }
            }
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToRemove swipeToDeleteCallback = new SwipeToRemove(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                cartAdapter.removeItem(position);
                 /*Snackbar snackbar = Snackbar.make(mainBinding.mainLl, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {
                    cartAdapter.restoreItem(item, position);
                    mainBinding.rvCartlist.scrollToPosition(position);
                    cartAdapter.setTotal();
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();*/
                //Log.v("count", "size" + cartListData.size());
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(activityMainBinding.rvCart);
    }

    //todo remove not used code
    /*private void initBindings() {
        dialogBatchSelectionBinding = DialogSelectProductBatchBinding.inflate(getLayoutInflater());
    }*/

    private void initArrayLists() {
        cartItemsList = new ArrayList<>();
        getAllProducts = new ArrayList<>();
        stockMasterVos = new ArrayList<>();
        productArrayList = new ArrayList<>();
    }

    //todo remove not used code of batch dialog
    /*private void initBatchSelectionDialog() {
        AlertDialog.Builder batchSelectionBuilder = new AlertDialog.Builder(this);
        batchSelectionBuilder.setView(dialogBatchSelectionBinding.getRoot());
        batchSelectionBuilder.setCancelable(true);
        batchSelectionDialog = batchSelectionBuilder.create();
    }*/

    @SuppressLint("NonConstantResourceId")
    private void initBillDetailsBinding() {
        this.bottomSheetOrderSummaryBinding = BottomSheetOrderSummaryBinding.inflate(getLayoutInflater());

        this.bottomSheetOrderSummaryBinding.ivSummaryBack.setOnClickListener(v -> bottomSheetBillDetails.dismiss());

        bottomSheetOrderSummaryBinding.rGrpPayType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioOnline:
                    /*bottomSheetOrderSummaryBinding.llPaymentOptions.setVisibility(View.VISIBLE);*/
                    Toast.makeText(MainActivity.this, "Make Payment Online", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.radioCounter:
                    Toast.makeText(MainActivity.this, "Make Payment At Counter", Toast.LENGTH_SHORT).show();
                        /*bottomSheetOrderSummaryBinding.llPaymentOptions.setVisibility(View.INVISIBLE);
                        bottomSheetOrderSummaryBinding.radioDebitCard.setChecked(false);
                        bottomSheetOrderSummaryBinding.radioUPI.setChecked(false);
                        bottomSheetOrderSummaryBinding.radioNetBanking.setChecked(false);*/
                    break;
            }
        });

        this.bottomSheetOrderSummaryBinding.btnCheckOut.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Order Placed with unpaid.", Toast.LENGTH_SHORT).show();
            if (this.bottomSheetOrderSummaryBinding.radioCounter.isChecked()) {
                Toast.makeText(MainActivity.this, "Make Payment At Counter", Toast.LENGTH_SHORT).show();
                SaveBill saveBillPost = prepareSaveBillModel("");
                setSaveBill(saveBillPost);
                //mainViewModel.getProductByBarcodeId(getCurrentFinancialYear(), barcodeId, true);
                if (isInternetConnected) {
                    mainViewModel.postOrderData(
                            Integer.parseInt(PreferenceManager.getCompanyId(MainActivity.this)),
                            Integer.parseInt(PreferenceManager.getBranchId(MainActivity.this)),
                            Integer.parseInt(PreferenceManager.getCompanyId(MainActivity.this)),
                            saveBillPost
                    );
                } else {
                    CommonUtil.showSnackBar(activityMainBinding.llBtn, activityMainBinding.llBtn, getString(R.string.toast_no_internet));
                }
            } else if (this.bottomSheetOrderSummaryBinding.radioOnline.isChecked()) {
                Toast.makeText(MainActivity.this, "Make Payment Online", Toast.LENGTH_SHORT).show();
                SaveBill saveBillPost = prepareSaveBillModel("");
                setSaveBill(saveBillPost);

                /*Intent intent = new Intent(MainActivity.this, RazorpayPaymentActivity.class);
                startActivity(intent);*/
                if (isInternetConnected) {
                    mainViewModel.postOrderData(
                            Integer.parseInt(PreferenceManager.getCompanyId(MainActivity.this)),
                            Integer.parseInt(PreferenceManager.getBranchId(MainActivity.this)),
                            Integer.parseInt(PreferenceManager.getCompanyId(MainActivity.this)),
                            saveBillPost
                    );
                } else {
                    CommonUtil.showSnackBar(activityMainBinding.llBtn, activityMainBinding.llBtn, getString(R.string.toast_no_internet));
                }
                //todo place order but not paid
            } else {
                Toast.makeText(MainActivity.this, "Please, select payment method", Toast.LENGTH_SHORT).show();
            }
            /*Intent intent = new Intent(MainActivity.this, OrderDetailsActivity.class);
            intent.putExtra("checkStatus", 1);
            startActivity(intent);*/
        });
    }

    private SaveBill prepareSaveBillModel(String paymentMode) {
        //private SaveBill prepareSaveBillModel() {
        SaveBill saveBill = new SaveBill();
        saveBill.setCustomerId(Long.parseLong(PreferenceManager.userContactId(MainActivity.this)));
        /*if (paymentMode.trim().toLowerCase().equals("online")) {
            saveBill.setBankId(151);
        } else {
            saveBill.setBankId(0);
            //Double tenderedAmt = CommonUtil.getDoubleFromString(activityMainBinding.tvTotalAmount.getText().toString(), 2) + CommonUtil.getDoubleFromString(activityMainBinding.etRound.getText().toString(), 2);
            //saveBill.setTendered(activityMainBinding.tvTotalAmount.getText().toString());//todo ask sir for this payment
        }*/
        saveBill.setBankId(0);
        saveBill.setRoundOff(Double.parseDouble(activityMainBinding.etRound.getText().toString()));
        saveBill.setNetAmount(CommonUtil.getDoubleFromString(activityMainBinding.tvTotalAmount.getText().toString(), 2));
        saveBill.setTendered("0.0");
        Log.e(TAG, "prepareSaveBillModel:netAmt " + CommonUtil.getDoubleFromString(activityMainBinding.tvTotalAmount.getText().toString(), 2));
        saveBill.setPaymentMode(paymentMode);
        //paymentTypeForBill = paymentMode;
        /*if (paymentMode.toLowerCase().equals("upi")) {
            saveBill.setTendered("0.0");
        } else if (paymentMode.toLowerCase().equals("card")) {
            saveBill.setTendered("0.0");
        } else {*/
        //saveBill.setPaymentMode(paymentMode); //todo ask sir for payment

        //}
        saveBill.setFinancialYear(getCurrentFinancialYear());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        saveBill.setDate(format.format(new Date()));
        Log.d(TAG, "prepareModelCashOrder: saved bill date: " + saveBill.getDate());
        List<SalesDTO> salesDTOs = new ArrayList<>();
        for (int i = 0; i < cartItemsList.size(); i++) {
            SalesDTO salesDTO = new SalesDTO();
            salesDTO.setBatchId(cartItemsList.get(i).getStockId());
            salesDTO.setBatchNo(cartItemsList.get(i).getBatchNo());
            salesDTO.setDiscount(Double.parseDouble(String.format("%.3f", cartItemsList.get(i).getDiscount())) * Double.valueOf(cartItemsList.get(i).getQuantity()).doubleValue());
            //"discountAdditional": 0.0,
            salesDTO.setDiscountAdditional(0.0);
            salesDTO.setDiscountType(cartItemsList.get(i).getDiscountType());
            //"discountTypeAdditional": "percentage",
            salesDTO.setDiscountTypeAdditional("percentage");
            salesDTO.setLandingCost(cartItemsList.get(i).getLandingcost());
            salesDTO.setMrp(cartItemsList.get(i).getMrp());
            salesDTO.setMrpToDiscount(cartItemsList.get(i).getMrpToDiscount());
            salesDTO.setMrpToDiscountType("amount");
            //"mrpToDiscountTypeAdditional": "amount",
            //"mrpTodiscountAdditional": 0.0,
            salesDTO.setMrpToDiscountTypeAdditional("amount");
            salesDTO.setMrpTodiscountAdditional(0.0);
            salesDTO.setNetAmount(Double.parseDouble(String.format("%.3f", cartItemsList.get(i).getDisplayMrp())));
            salesDTO.setOrderBy(i + 1);
            salesDTO.setPrice(Double.parseDouble(String.format("%.3f", cartItemsList.get(i).getPrice())));
            salesDTO.setProductVarientId(cartItemsList.get(i).getProductVarientId());
            double profit = (cartItemsList.get(i).getDisplayMrp() - (cartItemsList.get(i).getLandingcost() * Double.valueOf(cartItemsList.get(i).getQuantity()).doubleValue()));
            salesDTO.setProfit(Double.parseDouble(String.format("%.3f", profit)));
            salesDTO.setQty(Double.parseDouble(cartItemsList.get(i).getQuantity()));
            salesDTO.setSellingPrice(cartItemsList.get(i).getSellingPrice());
            salesDTO.setTaxAmount(Double.parseDouble(String.format("%.3f", cartItemsList.get(i).getTotalTaxPrice())));
            salesDTO.setTaxId(cartItemsList.get(i).getProductDto().getTax_id());
            salesDTO.setTaxRate(cartItemsList.get(i).getProductDto().getTax_rate());
            salesDTO.setItemCode(cartItemsList.get(i).getItemCode());
            //todo set itemcode salesDTO.setItemCode(cartItemsList.get(i));
            salesDTOs.add(salesDTO);
        }
        saveBill.setMposItemSalesDTOs(salesDTOs);

        return saveBill;
    }

    private void initBottomSheetBillDetails() {
        this.bottomSheetBillDetails = new BottomSheetDialog(MainActivity.this);
        this.bottomSheetBillDetails.setContentView(this.bottomSheetOrderSummaryBinding.getRoot());
        this.bottomSheetBillDetails.setCancelable(true);
        ScreenUtils screenUtils = new ScreenUtils(this);
        this.bottomSheetBillDetails.getBehavior().setPeekHeight(screenUtils.getHeight());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_option_menu, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteCardMenu:
                discardCart();
                return true;
            case R.id.updateProfileMenu:
                Toast.makeText(MainActivity.this, "My Profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.myOrdersMenu:
                Toast.makeText(MainActivity.this, "My Orders", Toast.LENGTH_SHORT).show();
                Intent intentOrderList = new Intent(MainActivity.this, OrdersListActivity.class);
                startActivity(intentOrderList);
                return true;
            case R.id.logOutMenu:
                Toast.makeText(MainActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
            //return true;
        }
    }

    private void hideScannerCases() {
        activityMainBinding.mainLlMain.setWeightSum(1.5f);
        activityMainBinding.zxQrDecoratedBarcodeViewMain.setVisibility(View.GONE);
        unBindZxingCamera();
    }

    private void showScannerCases() {
        activityMainBinding.mainLlMain.setWeightSum(2f);
        activityMainBinding.zxQrDecoratedBarcodeViewMain.setVisibility(View.VISIBLE);
        bindZxingCamera();
    }

    private void resumeScannerCases() {
        activityMainBinding.zxQrDecoratedBarcodeViewMain.resume();
    }

    private void pauseScannerCases() {
        activityMainBinding.zxQrDecoratedBarcodeViewMain.pause();
    }

    private void razorpayPayment(SaveBillResponse saveBillResponse) {
        RazorpayApi apiInterface;
        String baseUrl = CommonUtil.baseUrlRazorpay;
        apiInterface = RazorpayApiGenerator.getApi(baseUrl).create(RazorpayApi.class);
        PostOrderData postOrderData = new PostOrderData();
        kProgressHUD.show();

        double orderTotalAmt = getSaveBill().getNetAmount() * 100;
        postOrderData.setAmount(orderTotalAmt);
        postOrderData.setCurrency("INR");
        postOrderData.setReceipt(String.valueOf(saveBillResponse.getSalesId()));

        Call<SingleOrderModel> callCreateOrder = apiInterface.createOrder(
                postOrderData,
                RazorpayApiAuthentication.getAuthToken()
        );

        callCreateOrder.enqueue(new Callback<SingleOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<SingleOrderModel> call, @NonNull Response<SingleOrderModel> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: order created");
                    assert response.body() != null;
                    Log.e(TAG, "onResponse: order data" + response.body().getId());
                    Log.e(TAG, "onResponse: order data" + response.body().toString());
                    kProgressHUD.dismiss();
                    Toast.makeText(MainActivity.this, "Your Payment Process Is Started", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> {
                        Log.e(TAG, "run: payment start");
                        makePaymentAtRazorpay(response.body());
                    }, 1000);
                } else {
                    kProgressHUD.dismiss();
                    Log.e(TAG, "onResponse: order create fail");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SingleOrderModel> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                kProgressHUD.dismiss();
            }
        });
    }

    private void makePaymentAtRazorpay(SingleOrderModel singleOrderModel) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_goYqawczzlm2Yp");
        try {
            JSONObject options = new JSONObject();
            String userFullName = PreferenceManager.userFirstName(MainActivity.this) + " " + PreferenceManager.userLastName(MainActivity.this);
            String companyLogoPath = PreferenceManager.getCompanyLogoPrefix(MainActivity.this) + PreferenceManager.getCompanyLogo(MainActivity.this);
            String userMobileNo = PreferenceManager.userMobile(MainActivity.this);
            options.put("name", userFullName);
            options.put("description", "Checking Order API");
            options.put("image", companyLogoPath);
            options.put("currency", singleOrderModel.getCurrency());
            options.put("amount", singleOrderModel.getAmount());
            options.put("order_id", singleOrderModel.getId());

            JSONObject preFill = new JSONObject();
            preFill.put("email", "shahmeeto1o82o@gmail.com");
            preFill.put("contact", userMobileNo);
            options.put("prefill", preFill);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            options.put("notify", notify);

            options.put("reminder_enable", true);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void viewObserversCollection() {
        mainViewModel.error.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    CommonUtil.showSnackBar(activityMainBinding.llTotal, activityMainBinding.llTotal, "" + s);
                    if (s.equals("Product not found.")) {
                        if (kProgressHUD != null && kProgressHUD.isShowing()) {
                            kProgressHUD.dismiss();
                        }
                        resumeScannerCases();
                    } else if (s.equals("Unable to resolve host.")) {
                        if (kProgressHUD != null && kProgressHUD.isShowing()) {
                            kProgressHUD.dismiss();
                        }
                        resumeScannerCases();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resumeScannerCases();
                        }
                    }, 1000);
                }
            }
        });

        mainViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    kProgressHUD.show();
                } else {
                    kProgressHUD.dismiss();
                }
            }
        });

        mainViewModel.getProduct.observe(this, new Observer<ProductVarientsDTO>() {
            @Override
            public void onChanged(ProductVarientsDTO mProduct) {
                //todo set product list of scanned or search
                if (kProgressHUD != null && kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
                if (mProduct != null) {
                    if (mProduct.getProductStatus().isProductAddable()) {
                        stockMasterVos.clear();
                        batchSelectionArrayAdapter.clear();
                        setProductDto(mProduct.getProductDto());
                        mProduct.setStockMasterVos(mProduct
                                .getStockMasterVos()
                                .stream().map(new Function<StockMasterVo, StockMasterVo>() {
                                    @Override
                                    public StockMasterVo apply(StockMasterVo stockMasterVo) {
                                        stockMasterVo.setOldQuantity(stockMasterVo.getQuantity());
                                        stockMasterVo.setDiscount(mProduct.getDiscount());
                                        stockMasterVo.setDiscountType(mProduct.getDiscountType());
                                        return stockMasterVo;
                                    }
                                }).collect(Collectors.toList()));
                        stockMasterVos.addAll(mProduct.getStockMasterVos());
                        batchSelectionArrayAdapter.notifyBatchChanges();
                        try {
                            if (stockMasterVos.size() > 1) {
                                /*setSelectedStockMasterVo((StockMasterVo) batchSelectionArrayAdapter.getItem(0));
                                dialogBatchSelectionBinding.spinnerBatchSelection.setSelection(0);
                                batchSelectionDialog.show();
                                pauseScannerCases();*/
                                setSelectedStockMasterVo((StockMasterVo) batchSelectionArrayAdapter.getItem(0));
                                saveSelectedBatchCalculation();
                                resumeScannerCases();
                            } else if (stockMasterVos.size() == 1) {
                                setSelectedStockMasterVo((StockMasterVo) batchSelectionArrayAdapter.getItem(0));
                                saveSelectedBatchCalculation();
                                resumeScannerCases();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtil.showSnackBar(activityMainBinding.mainLlMain, activityMainBinding.llTotal, "" + mProduct.getProductStatus().getReason());
                        resumeScannerCases();
                    }
                }
            }
        });

        mainViewModel.saveBillResponse.observe(this, new Observer<SaveBillResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(SaveBillResponse saveBillResponse) {
                if (saveBillResponse != null) {
                    if (bottomSheetOrderSummaryBinding.radioCounter.isChecked()) {
                        Log.e(TAG, "onChanged: at counter");
                        Log.e(TAG, "onChanged: " + saveBillResponse.getSalesNo());
                        Log.e(TAG, "onChanged: pass intent with salesNo to get data and show status");
                        clearDataAfterOrderPlace();
                        Intent intentOrderSummary = new Intent(MainActivity.this, OrderDetailsActivity.class);
                        intentOrderSummary.putExtra(CommonUtil.ORDER_DETAIL_SALE_NO, saveBillResponse.getSalesId());
                        intentOrderSummary.putExtra(CommonUtil.ORDER_DETAIL_STATUS, true);
                        startActivity(intentOrderSummary);
                    } else if (bottomSheetOrderSummaryBinding.radioOnline.isChecked()) {
                        Log.e(TAG, "onChanged: online");
                        razorpayPayment(saveBillResponse);
                        setSaveBillResponseData(saveBillResponse);
                    }
                }
            }
        });
        //{"branchId":"64","companyId":"64","contactDetails":{"addressLine1":"hxhxjc\nyxyd\nychc","firstName":"Shah","lastName":"Meet","mobileNo":"9106896990"}}
        mainViewModel.productList.observe(this, new Observer<List<GetAllProducts>>() {
            @Override
            public void onChanged(List<GetAllProducts> getAllProducts) {
                if (getAllProducts != null) {
                    MainActivity.this.getAllProducts.clear();
                    MainActivity.this.getAllProducts.addAll(getAllProducts);
                    if (adapterGetProductData != null) {
                        adapterGetProductData.notifyDataSetChanged();
                    }
                }
            }
        });
        mainViewModel.billStatusResponse.observe(this, new Observer<UpdateBillResponse>() {
            @Override
            public void onChanged(UpdateBillResponse updateBillResponse) {
                if (updateBillResponse != null) {
                    if (updateBillResponse.isStatus()) {
                        Toast.makeText(MainActivity.this, "Update Order Status successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Update Order Status failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearDataAfterOrderPlace() {
        bottomSheetOrderSummaryBinding.tvOrderTotal.setText("0.0");
        bottomSheetOrderSummaryBinding.tvOrderRoundOff.setText("0.0");
        bottomSheetOrderSummaryBinding.tvOrderTotalItems.setText("0.0");
        bottomSheetOrderSummaryBinding.tvOrderTotalQty.setText("0.0");
        bottomSheetOrderSummaryBinding.tvOrderTotalTax.setText("0.0");
        activityMainBinding.etRound.setText("0.0");
        activityMainBinding.tvTotalAmount.setText("0.0");
        activityMainBinding.tvCount.setText("0");
        cartItemsList.clear();
        cartAdapter.notifyDataSetChanged();
        setBackupFinalPrice(00.00);
        saveBill = null;
        saveBillResponseData = null;
        if (batchSelectionArrayAdapter != null) {
            batchSelectionArrayAdapter.clear();
        }
        if (getSelectedStockMasterVo() != null) {
            setSelectedStockMasterVo(null);
        }
        if (bottomSheetBillDetails.isShowing()) {
            bottomSheetBillDetails.dismiss();
        }
    }

    private void barcodeScannerViewSelection() {
        if (cameraSettings == null) {
            Log.d(TAG, "BarcodeScannerSelection: zxing barcode initialized.");
            initZxingBarcodeScanner();
            Log.d(TAG, "BarcodeScannerSelection: zxing barcode is selected.");
        } else {
            Log.d(TAG, "BarcodeScannerSelection: zxing barcode is already initialized.");
            bindZxingCamera();
        }
        activityMainBinding.mainLlMain.setWeightSum(1.5f);
    }

    private void initZxingBarcodeScanner() {
        cameraSettings = new CameraSettings();
        cameraSettings.setAutoFocusEnabled(true);
        cameraSettings.setRequestedCameraId(0); // front/back/etc
        activityMainBinding.zxQrDecoratedBarcodeViewMain.getBarcodeView().setCameraSettings(cameraSettings);
        activityMainBinding.zxQrDecoratedBarcodeViewMain.setStatusText("Place barcode inside scanning area.");
        activityMainBinding.zxQrDecoratedBarcodeViewMain.resume();
    }

    private void unBindZxingCamera() {
        Log.d(TAG, "BarcodeScannerSelection: zxing camera is paused.");
        activityMainBinding.zxQrDecoratedBarcodeViewMain.setEnabled(false);
        activityMainBinding.zxQrDecoratedBarcodeViewMain.getBarcodeView().pause();
    }

    private void bindZxingCamera() {
        Log.d(TAG, "BarcodeScannerSelection: zxing camera is resumed.");
        activityMainBinding.zxQrDecoratedBarcodeViewMain.setEnabled(true);
        activityMainBinding.zxQrDecoratedBarcodeViewMain.getBarcodeView().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isShowing.equals("Y")) {
            pauseScannerCases();
            hideScannerCases();
            isShowing = "N";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityMainBinding.mainLlMain.setWeightSum(1.5f);
        requestCameraPermission();
        if (isShowing.equals("Y")) {
            pauseScannerCases();
            hideScannerCases();
            isShowing = "N";
        }
        Log.d(TAG, "onResume: Called.");
    }

    @Override
    public void onCameraPermissionGranted() {

    }

    @Override
    public void onBackPressed() {
        if (cartItemsList != null && cartItemsList.size() > 0) {
            new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("Alert")
                    .setMessage("Do you really want to exit from the application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
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

    @Override
    public void onPaymentSuccess(String s) {
        try {
            activityMainBinding.tvSample.setVisibility(View.VISIBLE);
            activityMainBinding.tvSample.setText("Successful payment ID :" + s);
            SaveBillStatusModel saveBillStatusModel = new SaveBillStatusModel();
            saveBillStatusModel.setPaymentStatus("paid");
            saveBillStatusModel.setPaymentId(s);
            saveBillStatusModel.setPaymentMode(CommonUtil.paymentGatewayRazorpay.trim());
            saveBillStatusModel.setSalesId(getSaveBillResponseData().getSalesId());
            Log.e(TAG, "onPaymentSuccess: call api update status");
            if (isInternetConnected) {
                mainViewModel.updateOrderStatus(
                        Integer.parseInt(PreferenceManager.getCompanyId(MainActivity.this)),
                        Integer.parseInt(PreferenceManager.getBranchId(MainActivity.this)),
                        Integer.parseInt(PreferenceManager.getCompanyId(MainActivity.this)),
                        saveBillStatusModel
                );
            } else {
                CommonUtil.showSnackBar(activityMainBinding.llBtn, activityMainBinding.llBtn, getString(R.string.toast_no_internet));
            }
            clearDataAfterOrderPlace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Intent intentOrderSummary = new Intent(MainActivity.this, OrderDetailsActivity.class);
        intentOrderSummary.putExtra(CommonUtil.ORDER_DETAIL_SALE_NO, getSaveBillResponseData().getSalesId());
        intentOrderSummary.putExtra(CommonUtil.ORDER_DETAIL_STATUS, true);
        startActivity(intentOrderSummary);*/
        //new Handler().postDelayed(() -> activityMainBinding.tvSample.setVisibility(View.GONE), 10000);
    }

    @Override
    public void onPaymentError(int i, String s) {
        bottomSheetOrderSummaryBinding.tvErrorPaymentData.setVisibility(View.VISIBLE);
        bottomSheetOrderSummaryBinding.tvErrorPaymentData.setText("Failed and cause is :" + s);
        new Handler().postDelayed(() -> bottomSheetOrderSummaryBinding.tvErrorPaymentData.setVisibility(View.GONE), 5000);
    }

    /*private void barcodeScannerViewSelection() {
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this))) {
            case "1":
                Log.d(TAG, "Barcode engine: Scandit");
                *//*if (camera == null) {
                    Log.d(TAG, "BarcodeScannerSelection: Scandit camera is initialized.");
                    initializeAndStartBarcodeScanning();
                } else {
                    resumeFrameSource();
                }*//*
                Log.d(TAG, "BarcodeScannerSelection: Scandit scanner is selected.");
                break;
            case "2":
                if (cameraProviderFuture == null) {
                    initMLKitBarcodeScanner();
                } else {
                    atomicBoolean.set(true);
                }
                Log.d(TAG, "Barcode engine: MLKit");
                break;
            case "3":
                Log.d(TAG, "Barcode engine: ZXing");
                if (cameraSettings == null) {
                    Log.d(TAG, "BarcodeScannerSelection: zxing barcode initialized.");
                    initZxingBarcodeScanner();
                    Log.d(TAG, "BarcodeScannerSelection: zxing barcode is selected.");
                } else {
                    Log.d(TAG, "BarcodeScannerSelection: zxing barcode is already initialized.");
                    bindZxingCamera();
                }
                break;
        }
        activityMainBinding.mainLlMain.setWeightSum(1.5f);
    }*/

    /*private void razorpayPayment(SaveBillResponse saveBillResponse) {
        RazorpayApi apiInterface;
        String baseUrl = CommonUtil.baseUrlRazorpay;
        apiInterface = RazorpayApiGenerator.getApi(baseUrl).create(RazorpayApi.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PostOrderData postOrderData = new PostOrderData();
                Random random = new Random();
                //todo int asfdl = Math.round(Float.parseFloat(var) * 100);
                int totalAmount = random.nextInt(999 - 100) + 100;
                //int intAmount = Math.round(Float.parseFloat(strAmount) * 100);
                //totalAmount = Math.round(Float.parseFloat(String.valueOf(totalAmount)) * 100);
                String strPostTotal = String.valueOf(saveBillResponse.getSalesId());
                double postTotal = CommonUtil.getDoubleFromString(strPostTotal, 2);
                totalAmount = 8238;
                postOrderData.setAmount((double) totalAmount);
                postOrderData.setCurrency("INR");
                //int no = random.nextInt(999 - 100) + 100;
                postOrderData.setReceipt(String.valueOf(saveBillResponse.getSalesId()));

                Call<SingleOrderModel> callCreateOrder = apiInterface.createOrder(
                        postOrderData,
                        RazorpayApiAuthentication.getAuthToken()
                );

                callCreateOrder.enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SingleOrderModel> call, @NonNull Response<SingleOrderModel> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "onResponse: order created");
                            assert response.body() != null;
                            Log.e(TAG, "onResponse: order data" + response.body().getId());
                            Log.e(TAG, "onResponse: order data" + response.body().toString());
                            //orderId = response.body().getId();

                            Toast.makeText(MainActivity.this, "Your Payment Process Is Started", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(() -> {
                                Log.e(TAG, "run: payment start");
                                makePayments(response.body());
                            }, 2000);

                        } else {
                            Log.e(TAG, "onResponse: order create fail");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SingleOrderModel> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });


            }
        }, 1000);
    }*/

    /*private void makePaymentsAtRazorpay(SingleOrderModel singleOrderModel) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_goYqawczzlm2Yp");


        try {
            JSONObject options = new JSONObject();
            String userName = PreferenceManager.userFirstName(MainActivity.this) + " " + PreferenceManager.userLastName(MainActivity.this);
            String companyDetails = PreferenceManager.getCompanyBranchName(MainActivity.this);
            if (companyDetails != null) {
                options.put("description", companyDetails);
            } else {
                options.put("description", "VasyERP");
            }
            String companyLogoPrefix = PreferenceManager.getCompanyLogoPrefix(MainActivity.this);
            String companyLogoName = PreferenceManager.getCompanyLogo(MainActivity.this);
            if (companyLogoPrefix != null && companyLogoName != null) {
                String finalImg = companyLogoPrefix + companyLogoName;
                options.put("image", finalImg);
            } else {
                options.put("image", "https://i.postimg.cc/sxc8sSTj/IMG-20200929-WA0000.jpg");
            }

            //options.put("image", "https://i.postimg.cc/sxc8sSTj/IMG-20200929-WA0000.jpg");
            options.put("name", userName);
            options.put("currency", singleOrderModel.getCurrency());
            options.put("amount", singleOrderModel.getAmount());
            options.put("order_id", singleOrderModel.getId());
            //notifyModel.setEmail(true);
            //        notifyModel.setSms(true);

            JSONObject preFill = new JSONObject();
            Random random = new Random();
            int randomNum = random.nextInt(999 - 100) + 100;
            String strEmailId = "shahmeet" + randomNum + "@gmail.com";
            preFill.put("email", strEmailId);
            preFill.put("contact", PreferenceManager.userMobile(MainActivity.this));
            options.put("prefill", preFill);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            options.put("notify", notify);

            options.put("reminder_enable", true);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }*/

    /*private void initBarcodeBinding() {
        this.bottomSheetBarcodeBinding = BottomSheetBarcodeBinding.inflate(getLayoutInflater());
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap("INV4183", BarcodeFormat.CODE_128, 700, 150);
            bottomSheetBarcodeBinding.ivBarcode.setImageBitmap(bitmap);
            bottomSheetBarcodeBinding.tvOrderNo.setText("INV4183");
        } catch (WriterException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: some interrupt ");
        }
    }
    private void initBottomSheetBarcodeDetails() {
        this.bottomSheetBarcode = new BottomSheetDialog(MainActivity.this);
        this.bottomSheetBarcode.setContentView(this.bottomSheetBarcodeBinding.getRoot());
        this.bottomSheetBarcode.setCancelable(true);
        ScreenUtils screenUtils = new ScreenUtils(this);
        this.bottomSheetBarcode.getBehavior().setPeekHeight(screenUtils.getHeight());
    }*/
}