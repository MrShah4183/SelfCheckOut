package com.vasyerp.selfcheckout.ui.main;

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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Image;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.common.util.concurrent.ListenableFuture;
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
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.databinding.ActivityMainBinding;
import com.vasyerp.selfcheckout.databinding.BottomSheetOrderSummaryBinding;
import com.vasyerp.selfcheckout.ui.CameraPermissionActivity;
import com.vasyerp.selfcheckout.ui.order_list.OrdersListActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.utils.ScreenUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends CameraPermissionActivity {
    private static final String TAG = "MainActivity";

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
    private BottomSheetOrderSummaryBinding bottomSheetOrderSummaryBinding;
    private BottomSheetDialog bottomSheetBillDetails;

    String subData = "symbology : ";
    Api apiInterface;

    double changeAmount = 0.0;
    String isShowing = "N";

    ActivityMainBinding activityMainBinding;
    KProgressHUD kProgressHUD;
    private long selectedScannerId = 3;
    private String barcodeId;

    public long getSelectedScannerId() {
        return selectedScannerId;
    }

    public void setSelectedScannerId(long selectedScannerId) {
        this.selectedScannerId = selectedScannerId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolbarMain);
        kProgressHUD = CommonUtil.getProgressView(this);

        initBillDetailsBinding();
        initBottomSheetBillDetails();

        //setSelectedScannerId(Long.parseLong(PreferenceManager.getScanditApiKey(MainActivity.this)));
        //PreferenceManager.setBarcodeSelectionId(MainActivity.this, CommonUtil.SCANNER_SELECTION_ID, remoteConfig.getLong(CommonUtil.REMOTE_CONFIG_SCANNER_KEY));
        barcodeScannerViewSelection();

        //todo call when api required
        //apiInterface = ApiGenerator.getSingle().create(Api.class);

        ConnectivityStatus status = new ConnectivityStatus(this);
        status.observe(this, aBoolean -> {
            if (aBoolean) {
                kProgressHUD.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //todo load product data
                        /*if (getAllProducts.size() == 0) {
                            mainViewModel.getAllProducts(companyId);
                        }*/
                        kProgressHUD.dismiss();
                    }
                }, 1500);
            }
        });

        //activityMainBinding.tvBtnScanOnOffMain.setOnClickListener(v -> {
        activityMainBinding.btnMainOnOff.setOnClickListener(v -> {
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

        activityMainBinding.zxQrDecoratedBarcodeViewMain.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 150);
                Log.d("MainActivity", "Barcode Result from zxing: " + result.toString());
                if (kProgressHUD != null && !kProgressHUD.isShowing()) {
                    kProgressHUD.show();
                }
                barcodeId = result.toString();
                Toast.makeText(MainActivity.this, "Product Scanned", Toast.LENGTH_SHORT).show();
                //todo call api after scanning at zxing
                new Handler().postDelayed(() -> {
                    resumeScannerCases();
                    kProgressHUD.dismiss();
                }, 1000);
                /*mainViewModel.getProductByProductId(
                        getCurrentFinancialYear(),
                        barcodeId,
                        true);*/
                activityMainBinding.zxQrDecoratedBarcodeViewMain.pause();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        activityMainBinding.btnMainCheckOut.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Show bottom sheet", Toast.LENGTH_SHORT).show();
            this.bottomSheetBillDetails.show();
            /*Toast.makeText(MainActivity.this, "Order Post", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(intent);*/
        });

        //activityMainBinding.btnOrderCancle.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Remove All items", Toast.LENGTH_SHORT).show());

    }

    @SuppressLint("NonConstantResourceId")
    private void initBillDetailsBinding() {
        this.bottomSheetOrderSummaryBinding = BottomSheetOrderSummaryBinding.inflate(getLayoutInflater());

        this.bottomSheetOrderSummaryBinding.btnCheckOut.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Order Placed with unpaid.", Toast.LENGTH_SHORT).show();
            bottomSheetOrderSummaryBinding.rGrpPayType.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.radioOnline:
                        /*bottomSheetOrderSummaryBinding.llPaymentOptions.setVisibility(View.VISIBLE);*/
                        Toast.makeText(MainActivity.this, "Make Payment Online,\n Now, select Payment Options", Toast.LENGTH_SHORT).show();
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
            /*if (finalBillList.size() > 0) {
                homeViewModel.saveSales(finalBillList, Double.parseDouble(binding.grandTotalTV.getText().toString()), userFrontId);
            } else {
                CommonUtil.showSnackBar(binding.bottomRel, binding.bottomRel, "Please add items.");
            }*/
        });
    }

    private void initBottomSheetBillDetails() {
        this.bottomSheetBillDetails = new BottomSheetDialog(MainActivity.this);
        this.bottomSheetBillDetails.setContentView(this.bottomSheetOrderSummaryBinding.getRoot());
        this.bottomSheetBillDetails.setCancelable(true);
        ScreenUtils screenUtils = new ScreenUtils(this);
        this.bottomSheetBillDetails.getBehavior().setPeekHeight(screenUtils.getHeight());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                Toast.makeText(MainActivity.this, "Clear all cart item", Toast.LENGTH_SHORT).show();
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
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this))) {
            case "1":
                /*activityMainBinding.mainLl.setWeightSum(1.5f);
                activityMainBinding.llscanner.setVisibility(View.GONE);
                pauseFrameSource();*/
                break;
            case "2":
                activityMainBinding.mainLlMain.setWeightSum(1.5f);
                activityMainBinding.viewFinderMain.setVisibility(View.GONE);
                unBindMLCamera();
                atomicBoolean.set(false);
                break;
            case "3":
                activityMainBinding.mainLlMain.setWeightSum(1.5f);
                activityMainBinding.zxQrDecoratedBarcodeViewMain.setVisibility(View.GONE);
                unBindZxingCamera();
                break;
        }
    }

    private void showScannerCases() {
        Log.d(TAG, "showScannerCases: " + String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this)));
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this))) {
            case "1":
                /*activityMainBinding.mainLl.setWeightSum(2f);
                activityMainBinding.llscanner.setVisibility(View.VISIBLE);
                resumeFrameSource();*/
                break;
            case "2":
                activityMainBinding.mainLlMain.setWeightSum(2f);
                activityMainBinding.viewFinderMain.setVisibility(View.VISIBLE);
                bindMLCamera();
                atomicBoolean.set(true);
                break;
            case "3":
                activityMainBinding.mainLlMain.setWeightSum(2f);
                activityMainBinding.zxQrDecoratedBarcodeViewMain.setVisibility(View.VISIBLE);
                bindZxingCamera();
                break;
        }
    }

    private void resumeScannerCases() {
        Log.d(TAG, "ResumeScannerCase: " + String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this)));
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this))) {
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
                activityMainBinding.zxQrDecoratedBarcodeViewMain.resume();
                break;
        }
    }

    private void pauseScannerCases() {
        Log.d(TAG, "PauseScannerCases: " + String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this)));
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this))) {
            case "1":
                //barcodeCapture.setEnabled(false);
                break;
            case "2":
                atomicBoolean.set(false);
                break;
            case "3":
                activityMainBinding.zxQrDecoratedBarcodeViewMain.pause();
                break;
        }
    }

    private void viewObserversCollection() {
        //todo call when data is get
    }

    private void barcodeScannerViewSelection() {
        switch (String.valueOf(PreferenceManager.getBarcodeSelectionId(MainActivity.this))) {
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
        activityMainBinding.mainLlMain.setWeightSum(1.5f);
    }

    private void initMLKitBarcodeScanner() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(MainActivity.this);
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

        preview.setSurfaceProvider(activityMainBinding.viewFinderMain.getSurfaceProvider());

        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        ViewPort viewPort = ((PreviewView) findViewById(R.id.viewFinderMain)).getViewPort();

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
                    }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
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

    private void processBarcode
            (List<com.google.mlkit.vision.barcode.common.Barcode> barcodes) {
        if (barcodes.size() > 0) {
            Log.d(TAG, "processBarcode: " + Arrays.toString(barcodes.toArray()));
            for (com.google.mlkit.vision.barcode.common.Barcode barcode : barcodes) {
                Rect rect = barcode.getBoundingBox();
                barcodeId = barcode.getDisplayValue();
                //todo call api after scanning at mlkit
                new Handler().postDelayed(() -> {
                    resumeScannerCases();
                    kProgressHUD.dismiss();
                }, 1000);
                /*mainViewModel.getProductByProductId(
                        getCurrentFinancialYear(),
                        barcodeId,
                        true);*/
            }
            atomicBoolean.set(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    atomicBoolean.set(true);
                }
            }, 3000);
        }
    }

    @OptIn(markerClass = {androidx.camera.lifecycle.ExperimentalUseCaseGroupLifecycle.class,
            androidx.camera.core.ExperimentalUseCaseGroup.class})
    private void bindMLCamera() {
        processCameraProvider.bindToLifecycle(MainActivity.this, cameraSelector, useCaseGroup.build());
    }

    private void unBindMLCamera() {
        processCameraProvider.unbindAll();
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
            hideScannerCases();
            isShowing = "N";
        }
        Log.d(TAG, "onResume: Called.");
    }

    @Override
    public void onCameraPermissionGranted() {

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
}