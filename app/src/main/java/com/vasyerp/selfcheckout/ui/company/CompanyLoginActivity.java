package com.vasyerp.selfcheckout.ui.company;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.media.AudioManager;

import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.SelfCheckOutApp;
import com.vasyerp.selfcheckout.adapters.company_list.CompanyListAdapter;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityCompanyLoginBinding;
import com.vasyerp.selfcheckout.models.customer.CreateCustomerBody;
import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.CompanyCustomerBody;
import com.vasyerp.selfcheckout.repositories.CompanyLoginRepository;
import com.vasyerp.selfcheckout.ui.CameraPermissionActivity;
import com.vasyerp.selfcheckout.ui.main.MainActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.viewmodels.companylogin.CompanyLoginViewModel;
import com.vasyerp.selfcheckout.viewmodels.companylogin.CompanyLoginViewModelFactory;

import java.util.ArrayList;

import java.util.List;

//public class CompanyLoginActivity extends AppCompatActivity {
public class CompanyLoginActivity extends CameraPermissionActivity {
    ActivityCompanyLoginBinding companyLoginBinding;
    private final String TAG = CompanyLoginActivity.this.getClass().getSimpleName();

    private SelfCheckOutApp selfCheckOutApp;
    private ArrayList<LogIn> storeList;
    CompanyCustomerBody companyCustomerBody;
    CreateCustomerBody createCustomerBody;

    /**
     * zxing barcode scanner
     */
    private CameraSettings cameraSettings;

    CompanyLoginViewModel companyLoginViewModel;
    //AtomicBoolean atomicBoolean;

    KProgressHUD kProgressHUD;
    private String barcodeId;
    String isShowing = "N";
    CompanyListAdapter companyListAdapter;
    boolean isInternetConnected = false;

    @SuppressLint({"ResourceAsColor", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        companyLoginBinding = ActivityCompanyLoginBinding.inflate(getLayoutInflater());
        setContentView(companyLoginBinding.getRoot());
        setSupportActionBar(companyLoginBinding.toolbarCompanyLogin);

        selfCheckOutApp = (SelfCheckOutApp) this.getApplication();

        companyLoginBinding.mainLlCom.setWeightSum(1.5f);
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.setVisibility(View.GONE);
        barcodeScannerViewSelection();

        //tempDBLoginList = new ArrayList<>();
        storeList = new ArrayList<>();
        kProgressHUD = CommonUtil.getKProgressHud(this);
        companyListAdapter = new CompanyListAdapter(this, storeList);
        //initDB();

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(CompanyLoginActivity.this);
        connectivityStatus.observe(this, aBoolean -> {
            Log.e(TAG, "onChanged net state: " + aBoolean);
            isInternetConnected = aBoolean;
            if (aBoolean) {
                if (companyLoginBinding.tvCompanyLoginNoInternet.getText().toString().equals(getString(R.string.toast_no_internet))) {
                    companyLoginBinding.tvCompanyLoginNoInternet.setText(getString(R.string.toast_internet));
                    companyLoginBinding.tvCompanyLoginNoInternet.setTextColor(R.color.green);
                    new Handler().postDelayed(() -> companyLoginBinding.tvCompanyLoginNoInternet.setVisibility(View.GONE), 1000);
                }
            } else {
                companyLoginBinding.tvCompanyLoginNoInternet.setText(getString(R.string.toast_no_internet));
                companyLoginBinding.tvCompanyLoginNoInternet.setTextColor(R.color.red);
                companyLoginBinding.tvCompanyLoginNoInternet.setVisibility(View.VISIBLE);
            }
        });

        setCustomerData();

        initViewModelAndRepository();

        companyListAdapter.setCompanyListClickListener((strCompanyId, strBranchId) -> {
            Log.e(TAG, "setLoginData: click listener" + strBranchId);
            CompanyCustomerBody companyCustomerBody = new CompanyCustomerBody();
            companyCustomerBody.setCompanyId(strCompanyId);
            companyCustomerBody.setBranchId(strBranchId);
            companyCustomerBody.setContactDetails(createCustomerBody);

            if (isInternetConnected) {
                companyLoginViewModel.companyLogin(companyCustomerBody);
            } else {
                CommonUtil.showSnackBar(companyLoginBinding.llSnackBar, companyLoginBinding.llSnackBar, getString(R.string.toast_no_internet));
            }
        });
        companyLoginBinding.rvShopList.setAdapter(companyListAdapter);

        companyLoginViewModel.error.observe(this, s -> {
            if (s != null) {
                CommonUtil.showSnackBar(companyLoginBinding.llSnackBar, companyLoginBinding.llSnackBar, "" + s);
                resumeScannerCases();
            }
        });

        companyLoginViewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                kProgressHUD.show();
            } else {
                kProgressHUD.dismiss();
            }
        });

        companyLoginViewModel.getCompanyLoginData.observe(this, logIn -> {
            if (logIn != null) {
                Log.e(TAG, "onCreate: " + logIn.getCustomerDetailsResponse().getContactId());
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getCompanyId(), CommonUtil.COMPANY_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getBranchId(), CommonUtil.BRANCH_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getUserFrontId(), CommonUtil.USER_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, String.valueOf(logIn.getCustomerDetailsResponse().getContactId()), CommonUtil.USER_CONTACT_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getBranchName(), CommonUtil.COMPANY_BRANCH_NAME);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getLogoPrefix(), CommonUtil.COMPANY_LOGO_PREFIX);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getLogo(), CommonUtil.COMPANY_LOGO);
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(CompanyLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }, 500);
            } else {
                CommonUtil.showSnackBar(companyLoginBinding.llSnackBar, companyLoginBinding.llSnackBar, "Store Not Found.");
            }
        });

        companyLoginViewModel.getCompanyLoginLength.observe(this, integer -> {
            if (integer > 0) {
                companyLoginViewModel.getCompanyList();
            }
        });

        companyLoginViewModel.getCompanyLoginList.observe(this, logIns -> {
            storeList.clear();
            storeList.addAll(logIns);
            companyListAdapter.notifyDataSetChanged();
        });


        companyLoginBinding.btnComOnOff.setOnClickListener(v -> {
            Log.v("Hide", "btn click start");
            if (isShowing.equals("Y")) {
                Log.v("Hide", "btn click if gone : " + isShowing);
                pauseScannerCases();
                hideScannerCases();
                isShowing = "N";
            } else {
                Log.v("Hide", "btn click else visible : " + isShowing);
                showScannerCases();
                resumeScannerCases();
                isShowing = "Y";
            }
            Log.v("Hide", "btn click end");
        });

        companyLoginBinding.zxQrDecoratedBarcodeViewCom.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                pauseScannerCases();
                hideScannerCases();
                isShowing = "N";
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 150);
                Log.d("MainActivity", "Barcode Result from zxing: " + result.toString());
                barcodeId = result.toString();
                Log.e(TAG, "barcodeResult: " + barcodeId);
                Gson gson = new Gson();
                try {
                    companyCustomerBody = gson.fromJson(barcodeId, CompanyCustomerBody.class);
                    companyCustomerBody.setContactDetails(createCustomerBody);
                    Log.e(TAG, "barcodeResult: " + companyCustomerBody.getCompanyId());
                    Log.e(TAG, "barcodeResult: " + companyCustomerBody.getBranchId());
                    Log.e(TAG, "barcodeResult: " + companyCustomerBody.getContactDetails().getFirstName());
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtil.showSnackBar(companyLoginBinding.llSnackBar, companyLoginBinding.llSnackBar, e.toString());
                }
                Log.e(TAG, "companyLogin: call model method");
                if (isInternetConnected) {
                    companyLoginViewModel.companyLogin(companyCustomerBody);
                } else {
                    CommonUtil.showSnackBar(companyLoginBinding.llSnackBar, companyLoginBinding.llSnackBar, getString(R.string.toast_no_internet));
                }

                //companyLoginBinding.zxQrDecoratedBarcodeViewCom.pause();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    private void setCustomerData() {
        createCustomerBody = new CreateCustomerBody();
        /*createCustomerBody.setFirstName(PreferenceManager.userFirstName(this));
        createCustomerBody.setLastName(PreferenceManager.userLastName(this));
        createCustomerBody.setMobileNo(PreferenceManager.userMobile(this));
        createCustomerBody.setAddressLine1(PreferenceManager.userAddress(this));*/
        //todo change for dynamic user
        createCustomerBody.setFirstName("deep");
        createCustomerBody.setLastName("modi");
        createCustomerBody.setMobileNo("7575061808");
        createCustomerBody.setAddressLine1(PreferenceManager.userAddress(this));
    }

    private void initViewModelAndRepository() {
        Api apiInterface = ApiGenerator.getApi(CommonUtil.tempBaseUrl).create(Api.class);
        companyLoginViewModel = new ViewModelProvider(this, new CompanyLoginViewModelFactory(CompanyLoginRepository.getInstance(apiInterface, selfCheckOutApp.selfCheckOutDao))).get(CompanyLoginViewModel.class);
    }

    private void hideScannerCases() {
        companyLoginBinding.mainLlCom.setWeightSum(1.5f);
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.setVisibility(View.GONE);
        unBindZxingCamera();
    }

    private void showScannerCases() {
        companyLoginBinding.mainLlCom.setWeightSum(2f);
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.setVisibility(View.VISIBLE);
        bindZxingCamera();
    }

    private void resumeScannerCases() {
        new Handler().postDelayed(() -> companyLoginBinding.zxQrDecoratedBarcodeViewCom.resume(), 1000);
    }

    private void pauseScannerCases() {
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.pause();
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
        companyLoginBinding.mainLlCom.setWeightSum(1.5f);
    }

    private void initZxingBarcodeScanner() {
        cameraSettings = new CameraSettings();
        cameraSettings.setAutoFocusEnabled(true);
        cameraSettings.setRequestedCameraId(0); // front/back/etc
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.getBarcodeView().setCameraSettings(cameraSettings);
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.setStatusText("Place barcode inside scanning area.");
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.resume();
    }

    private void unBindZxingCamera() {
        Log.d(TAG, "BarcodeScannerSelection: zxing camera is paused.");
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.setEnabled(false);
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.getBarcodeView().pause();
    }

    private void bindZxingCamera() {
        Log.d(TAG, "BarcodeScannerSelection: zxing camera is resumed.");
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.setEnabled(true);
        companyLoginBinding.zxQrDecoratedBarcodeViewCom.getBarcodeView().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isShowing.equals("Y")) {
            hideScannerCases();
            isShowing = "N";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        companyLoginBinding.mainLlCom.setWeightSum(1.5f);
        requestCameraPermission();
        if (isShowing.equals("Y")) {
            hideScannerCases();
            isShowing = "N";
        }
        //initDB();
        Log.d(TAG, "onResume: Called.");
    }

    @Override
    public void onCameraPermissionGranted() {

    }
}