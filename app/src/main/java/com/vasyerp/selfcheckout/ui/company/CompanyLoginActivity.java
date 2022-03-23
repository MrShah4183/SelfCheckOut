package com.vasyerp.selfcheckout.ui.company;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Image;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.BuildConfig;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.SelfCheckOutApp;
import com.vasyerp.selfcheckout.adapters.company_list.CompanyListAdapter;
import com.vasyerp.selfcheckout.adapters.listeners.CompanyListClickListener;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityCompanyLoginBinding;
import com.vasyerp.selfcheckout.db.SelfCheckOutDB;
import com.vasyerp.selfcheckout.db.SelfCheckOutDao;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

//public class CompanyLoginActivity extends AppCompatActivity {
public class CompanyLoginActivity extends CameraPermissionActivity {
    ActivityCompanyLoginBinding companyLoginBinding;
    private static final String TAG = "CompanyLoginActivity";

    private SelfCheckOutApp selfCheckOutApp;
    private SelfCheckOutDao selfCheckOutDao;
    int totalRow = -1;
    private static ArrayList<LogIn> tempDBLoginList;
    private ArrayList<LogIn> storeList;
    CompanyCustomerBody companyCustomerBody;
    CreateCustomerBody createCustomerBody;

    /**
     * zxing barcode scanner
     */
    private CameraSettings cameraSettings;

    /**
     * mlkit barcode  scanner
     */
    private Preview preview;
    private ImageAnalysis imageAnalysis;
    private CameraSelector cameraSelector;
    private ProcessCameraProvider processCameraProvider;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private AtomicBoolean atomicBoolean;
    private UseCaseGroup.Builder useCaseGroup;
    CompanyLoginViewModel companyLoginViewModel;

    KProgressHUD kProgressHUD;
    FirebaseRemoteConfig remoteConfig;
    private boolean isBarcodeRemoteConfigInited = false;
    private long selectedScannerId = 3;
    private String barcodeId;
    String isShowing = "N";
    //ArrayList<CompanyDetailsModel> companyDetailsModelArrayList;
    //CompanyDetailsModel companyDetailsModel;
    CompanyListAdapter companyListAdapter;
    private boolean isInternetConnected;

    public long getSelectedScannerId() {
        return selectedScannerId;
    }

    public void setSelectedScannerId(long selectedScannerId) {
        this.selectedScannerId = selectedScannerId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        companyLoginBinding = ActivityCompanyLoginBinding.inflate(getLayoutInflater());
        setContentView(companyLoginBinding.getRoot());
        setSupportActionBar(companyLoginBinding.toolbarCompanyLogin);

        tempDBLoginList = new ArrayList<>();
        storeList = new ArrayList<>();
        kProgressHUD = CommonUtil.getKProgressHud(this);
        initDB();
        ConnectivityStatus connectivityStatus = new ConnectivityStatus(CompanyLoginActivity.this);
        connectivityStatus.observe(this, aBoolean -> {
            Log.e(TAG, "onChanged net state: " + aBoolean);
            isInternetConnected = aBoolean;
            createCustomerBody = new CreateCustomerBody();
            createCustomerBody.setFirstName(PreferenceManager.userFirstName(this));
            createCustomerBody.setLastName(PreferenceManager.userLastName(this));
            //todo set preferenceManager Mobile no createCustomerBody.setMobileNo(PreferenceManager.userMobile(this));
            createCustomerBody.setMobileNo("9106896990");
            createCustomerBody.setAddressLine1(PreferenceManager.userAddress(this));
        });
        initViewModelAndRepository();
        //companyDetailsModelArrayList = new ArrayList<>();
        //setCompanyData();
        companyListAdapter = new CompanyListAdapter(this, storeList);
        companyListAdapter.setCompanyListClickListener(new CompanyListClickListener() {
            @Override
            public void setLoginData(String strCompanyId, String strBranchId) {
                Log.e(TAG, "setLoginData: click listener" + strBranchId);
                CompanyCustomerBody companyCustomerBody = new CompanyCustomerBody();
                companyCustomerBody.setCompanyId(strCompanyId);
                companyCustomerBody.setBranchId(strBranchId);
                companyCustomerBody.setContactDetails(createCustomerBody);
                companyLoginViewModel.companyLogin(companyCustomerBody);
            }
        });
        companyLoginBinding.rvShopList.setAdapter(companyListAdapter);
        //set shop list adapter

        if (!isBarcodeRemoteConfigInited) {
            atomicBoolean = new AtomicBoolean(true);
            initBarcodeRemoteConfig();
            isBarcodeRemoteConfigInited = true;
        }

        setSelectedScannerId(remoteConfig.getLong(CommonUtil.REMOTE_CONFIG_SCANNER_KEY));
        PreferenceManager.setBarcodeSelectionId(CompanyLoginActivity.this, CommonUtil.SCANNER_SELECTION_ID, remoteConfig.getLong(CommonUtil.REMOTE_CONFIG_SCANNER_KEY));
        remoteConfig.fetchAndActivate().addOnCompleteListener(CompanyLoginActivity.this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
                    if (updated) {
                        setSelectedScannerId(remoteConfig.getLong(CommonUtil.REMOTE_CONFIG_SCANNER_KEY));
                        PreferenceManager.setBarcodeSelectionId(CompanyLoginActivity.this, CommonUtil.SCANNER_SELECTION_ID, getSelectedScannerId());
                        PreferenceManager.setScanditApiKey(CompanyLoginActivity.this, CommonUtil.SCANDIT_API_KEY, remoteConfig.getString(CommonUtil.SCANDIT_API_KEY));
                        barcodeScannerViewSelection();
                        Log.d(TAG, "Updated successfully: " + getSelectedScannerId());
                    } else {
                        Log.d(TAG, "Failed to update default is: " + getSelectedScannerId());
                        barcodeScannerViewSelection();
                    }
                } else {
                    Log.d(TAG, "Task is unsuccessful: " + getSelectedScannerId());
                }
            }
        });

        companyLoginViewModel.error.observe(this, s -> {
            if (s != null) {
                CommonUtil.showSnackBar(companyLoginBinding.llSecond, companyLoginBinding.llSecond, "" + s);
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
                Log.e(TAG, "onCreate: " + String.valueOf(logIn.getCustomerDetailsResponse().getContactId()));
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getCompanyId(), CommonUtil.COMPANY_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getBranchId(), CommonUtil.BRANCH_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getUserFrontId(), CommonUtil.USER_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, String.valueOf(logIn.getCustomerDetailsResponse().getContactId()), CommonUtil.USER_CONTACT_ID);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getBranchName(), CommonUtil.COMPANY_BRANCH_NAME);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getLogoPrefix(), CommonUtil.COMPANY_LOGO_PREFIX);
                PreferenceManager.savePref(CompanyLoginActivity.this, logIn.getLogo(), CommonUtil.COMPANY_LOGO);
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(CompanyLoginActivity.this, MainActivity.class);
                    String imgPath = logIn.getLogoPrefix() + logIn.getLogo();
                    intent.putExtra("storeName", logIn.getBranchName());
                    intent.putExtra("storeImg", imgPath);
                    startActivity(intent);
                }, 1000);
            } else {
                CommonUtil.showSnackBar(companyLoginBinding.llSecond, companyLoginBinding.llSecond, "Store Not Found.");
            }
            /*Log.e(TAG, "onChanged: " + logIn.getBranchId());
            Log.e(TAG, "onChanged: " + logIn.getBranchId());*/
        });

        companyLoginBinding.btnComOnOff.setOnClickListener(v -> {
            Log.v("Hide", "btn click start");
            if (isShowing.equals("Y")) {
                //dataCaptureView.setEnabled(false);
                Log.v("Hide", "btn click if gone : " + isShowing);
                hideScannerCases();
                isShowing = "N";
            } else {
                Log.v("Hide", "btn click else visible : " + isShowing);
                //dataCaptureView.setEnabled(true);
                //resumeFrameSource();
                showScannerCases();
                isShowing = "Y";
            }
            Log.v("Hide", "btn click end");
        });

        companyLoginBinding.zxQrDecoratedBarcodeViewCom.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                hideScannerCases();
                isShowing = "N";
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 150);
                Log.d("MainActivity", "Barcode Result from zxing: " + result.toString());
                /*if (kProgressHUD != null && !kProgressHUD.isShowing()) {
                    kProgressHUD.show();
                }*/
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
                    CommonUtil.showSnackBar(companyLoginBinding.llSecond, companyLoginBinding.llSecond, e.toString());
                }
                Log.e(TAG, "companyLogin: call model method");
                companyLoginViewModel.companyLogin(companyCustomerBody);
                companyLoginBinding.zxQrDecoratedBarcodeViewCom.pause();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    private void initViewModelAndRepository() {
        String strBaseUrl = PreferenceManager.getDomain(CompanyLoginActivity.this);
        Api apiInterface = ApiGenerator.getApi(strBaseUrl).create(Api.class);
        companyLoginViewModel = new ViewModelProvider(this, new CompanyLoginViewModelFactory(CompanyLoginRepository.getInstance(apiInterface, selfCheckOutApp.selfCheckOutDao))).get(CompanyLoginViewModel.class);
        //homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory(HomeRepository.getInstance(api, gajanandApp.gajanandDao), userFrontId)).get(HomeViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initDB() {
        kProgressHUD.show();
        try {
            Log.e(TAG, "initDB: start method");
            selfCheckOutApp = (SelfCheckOutApp) this.getApplication();
            selfCheckOutDao = selfCheckOutApp.selfCheckOutDao;

            SelfCheckOutDB.databaseWriteExecutor.execute(() -> totalRow = selfCheckOutDao.getTotalRow());
            new Handler().postDelayed(() -> {
                if (totalRow > 0) {
                    tempDBLoginList.clear();
                    Log.e(TAG, "initDB: get row");
                    SelfCheckOutDB.databaseWriteExecutor.execute(() -> tempDBLoginList.addAll(selfCheckOutDao.getAllStoreData()));
                    new Handler().postDelayed(() -> {
                        Log.e(TAG, "initDB: size" + tempDBLoginList.size());
                        if (tempDBLoginList.size() > 0) {
                            Log.e(TAG, "initDB: add data");
                            storeList.clear();
                            storeList.addAll(tempDBLoginList);
                            companyListAdapter.notifyDataSetChanged();
                        } else
                            Log.e(TAG, "run: size is zero");
                    }, 1000);
                }
            }, 1000);
            kProgressHUD.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            kProgressHUD.dismiss();
        }
    }

    private void hideScannerCases() {
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this))) {
            case "1":
                /*companyLoginBinding.mainLl.setWeightSum(1.5f);
                companyLoginBinding.llscanner.setVisibility(View.GONE);
                pauseFrameSource();*/
                break;
            case "2":
                companyLoginBinding.mainLlCom.setWeightSum(1.5f);
                companyLoginBinding.viewFinderCom.setVisibility(View.GONE);
                unBindMLCamera();
                atomicBoolean.set(false);
                break;
            case "3":
                companyLoginBinding.mainLlCom.setWeightSum(1.5f);
                companyLoginBinding.zxQrDecoratedBarcodeViewCom.setVisibility(View.GONE);
                unBindZxingCamera();
                break;
        }
    }

    private void showScannerCases() {
        Log.d(TAG, "showScannerCases: " + PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this));
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this))) {
            case "1":
                /*companyLoginBinding.mainLl.setWeightSum(2f);
                companyLoginBinding.llscanner.setVisibility(View.VISIBLE);
                resumeFrameSource();*/
                break;
            case "2":
                companyLoginBinding.mainLlCom.setWeightSum(2f);
                companyLoginBinding.viewFinderCom.setVisibility(View.VISIBLE);
                bindMLCamera();
                atomicBoolean.set(true);
                break;
            case "3":
                companyLoginBinding.mainLlCom.setWeightSum(2f);
                companyLoginBinding.zxQrDecoratedBarcodeViewCom.setVisibility(View.VISIBLE);
                bindZxingCamera();
                break;
        }
    }

    private void resumeScannerCases() {
        Log.d(TAG, "ResumeScannerCase: " + String.valueOf(PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this)));
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this))) {
            case "1":
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        barcodeCapture.setEnabled(true);
                    }
                }, 1500);*/
                break;
            case "2":
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        atomicBoolean.set(true);
                    }
                }, 1500);
                break;
            case "3":
                companyLoginBinding.zxQrDecoratedBarcodeViewCom.resume();
                break;
        }
    }

    private void pauseScannerCases() {
        Log.d(TAG, "PauseScannerCases: " + String.valueOf(PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this)));
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this))) {
            case "1":
                //barcodeCapture.setEnabled(false);
                break;
            case "2":
                atomicBoolean.set(false);
                break;
            case "3":
                companyLoginBinding.zxQrDecoratedBarcodeViewCom.pause();
                break;
        }
    }

    private void barcodeScannerViewSelection() {
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(CompanyLoginActivity.this))) {
            case "1":
                Log.d(TAG, "Barcode engine: Scandit");
                /*if (camera == null) {
                    Log.d(TAG, "BarcodeScannerSelection: Scandit camera is initialized.");
                    initializeAndStartBarcodeScanning();
                } else {
                    resumeFrameSource();
                }*/
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
        companyLoginBinding.mainLlCom.setWeightSum(1.5f);
    }

    private void initMLKitBarcodeScanner() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(CompanyLoginActivity.this);
        cameraProviderFuture.addListener(() -> {
            try {
                processCameraProvider = cameraProviderFuture.get();
                bindCameraPreview(processCameraProvider);
            } catch (Exception e) {

            }
        }, ContextCompat.getMainExecutor(this));
    }

    @OptIn(markerClass = androidx.camera.core.ExperimentalUseCaseGroup.class)
    private void bindCameraPreview(ProcessCameraProvider cameraProvider) {
        preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        preview.setSurfaceProvider(companyLoginBinding.viewFinderCom.getSurfaceProvider());

        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        ViewPort viewPort = ((PreviewView) findViewById(R.id.viewFinderCom)).getViewPort();

        useCaseGroup = new UseCaseGroup.Builder();
        useCaseGroup.setViewPort(viewPort);
        useCaseGroup.addUseCase(preview);
        useCaseGroup.addUseCase(imageAnalysis);

        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(com.google.mlkit.vision.barcode.common.Barcode.FORMAT_ALL_FORMATS)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                @SuppressLint("UnsafeOptInUsageError") Image image = imageProxy.getImage();
                if (image != null && atomicBoolean.get()) {
                    InputImage inputImage = InputImage.
                            fromMediaImage(image, imageProxy.getImageInfo().getRotationDegrees());
                    Task<List<Barcode>> result = scanner.process(inputImage);
                    result.addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(@NonNull List<com.google.mlkit.vision.barcode.common.Barcode> barcodes) {
                            processBarcode(barcodes);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Could not detect barcode!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<List<com.google.mlkit.vision.barcode.common.Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<com.google.mlkit.vision.barcode.common.Barcode>> task) {
                            image.close();
                            imageProxy.close();
                        }
                    });
                } else {
                    if (image != null) {
                        image.close();
                        imageProxy.close();
                    }
                }
            }
        });
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    private void processBarcode(List<com.google.mlkit.vision.barcode.common.Barcode> barcodes) {
        if (barcodes.size() > 0) {
            atomicBoolean.set(false);
            hideScannerCases();
            isShowing = "N";
            Log.d(TAG, "processBarcode: " + Arrays.toString(barcodes.toArray()));
            for (com.google.mlkit.vision.barcode.common.Barcode barcode : barcodes) {
                Rect rect = barcode.getBoundingBox();
                barcodeId = barcode.getDisplayValue();
                Log.e(TAG, "processBarcode: mlkit" + barcodeId);
                Gson gson = new Gson();
                try {
                    companyCustomerBody = gson.fromJson(barcodeId, CompanyCustomerBody.class);
                    companyCustomerBody.setContactDetails(createCustomerBody);
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtil.showSnackBar(companyLoginBinding.llSecond, companyLoginBinding.llSecond, e.toString());
                }
                Log.e(TAG, "companyLogin: call model method");
                companyLoginViewModel.companyLogin(companyCustomerBody);
                /*new Handler().postDelayed(() -> {
                    Intent intent = new Intent(CompanyLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    resumeScannerCases();
                    kProgressHUD.dismiss();
                }, 1000);*/
                /*mainViewModel.getProductByProductId(
                        getCurrentFinancialYear(),
                        barcodeId,
                        true);*/
            }

            //new Handler().postDelayed(() -> atomicBoolean.set(true), 3000);
        }
    }

    @OptIn(markerClass = {androidx.camera.lifecycle.ExperimentalUseCaseGroupLifecycle.class,
            androidx.camera.core.ExperimentalUseCaseGroup.class})
    private void bindMLCamera() {
        processCameraProvider.bindToLifecycle(CompanyLoginActivity.this, cameraSelector, useCaseGroup.build());
    }

    private void unBindMLCamera() {
        processCameraProvider.unbindAll();
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


    private void initBarcodeRemoteConfig() {
        long time = BuildConfig.DEBUG ? 0 : 600;
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(time)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
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
        initDB();
        Log.d(TAG, "onResume: Called.");
    }

    @Override
    public void onCameraPermissionGranted() {

    }

    /*private void setCompanyData() {
        companyDetailsModel = new CompanyDetailsModel(
                "img1",
                R.drawable.m1,
                "Shah traders",
                "Saraspur, Milan Cinema Road, Nr.Hari bhai godani hospital, Ahmedabad-380018."
        );
        companyDetailsModelArrayList.add(companyDetailsModel);
        companyDetailsModel = new CompanyDetailsModel(
                "img2",
                R.drawable.m2,
                "Krisha Cards",
                "Guruji Bridge, daxini Soc., Maninagar, Ahmedabad-38----."
        );
        companyDetailsModelArrayList.add(companyDetailsModel);
        companyDetailsModel = new CompanyDetailsModel(
                "img3",
                R.drawable.m3,
                "Marvel Studio",
                "Frank G. Wells Building 2nd Floor 500 South Buena Vista Street, Burbank, California , United States"
        );
        companyDetailsModelArrayList.add(companyDetailsModel);
        companyDetailsModel = new CompanyDetailsModel(
                "img4",
                R.drawable.m4,
                "Warner Bros. Studios",
                "4000 Warner Boulevard, Burbank, California, United States"
        );
        companyDetailsModelArrayList.add(companyDetailsModel);
    }*/
}