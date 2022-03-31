package com.vasyerp.selfcheckout.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
//import com.vasyerp.selfcheckout.BuildConfig;
import com.vasyerp.selfcheckout.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.http.PUT;

public class CommonUtil {
    public static final String baseUrlRazorpay = "https://api.razorpay.com/v1/";
    public static final String tempBaseUrlTesting = "http://192.168.175.207:8080";
    public static final String tempBaseUrl = "https://croods.vasyerp.com";
    public static final String paymentGatewayRazorpay = "razorpay";
    public static final String paymentGatewayInstaMojo = "instamojo";
    public static final String paymentGatewayPaytm = "paytm";
    public static final String paymentGatewayCashFree = "cashfree";
    //public static final String tempBaseUrlServer = "https://croods.vasyerp.com";
    //public static final String SCANDIT_LICENSE_KEY = BuildConfig.SCANDIT_API_KEY;
    public static final String SCANDIT_LICENSE_KEY = "";
    //public final static String DOMAIN_ID = "DOMAIN_ID";
    public static final String SCANNER_SELECTION_ID = "barcodeSelectionId";
    public static final String REMOTE_CONFIG_SCANNER_KEY = "SCANNER";
    public static final String SCANDIT_API_KEY = "SCANDIT_API_KEY";
    /*public static final String USERNAME = "USERNAME";
    public static final String PRINTER_TYPE = "printerType";*/
    public final static String BRANCH_ID = "branchId";
    public final static String COMPANY_ID = "companyId";
    public static final String USER_ID = "userFrontId";
    public static final String COMPANY_LOGO_PREFIX = "companyLogoPrefix";
    public static final String COMPANY_LOGO = "companyLogo";
    public static final String COMPANY_BRANCH_NAME = "companyBranchName";
    public static final String DOMAIN = "domain";
    public static final String USER_F_NAME = "userFirstName";
    public static final String USER_L_NAME = "userLastName";
    public static final String USER_MOBILE = "userMobile";
    public static final String USER_ADDRESS = "userAddress";
    public static final String USER_CONTACT_ID = "userContactId";
    public static final String ORDER_DETAIL_SALE_NO = "orderSalesNo";//order no
    public static final String ORDER_DETAIL_STATUS = "orderStatus";//true for current order false for previous order

    /*public static final String PRINT_BILL = "printBill";
    public static final String BLUETOOTH_NAME = "bluetoothName";
    public static final String SELECTED_BT_MAC_ADDRESS = "SELECTED_BT_MAC_ADDRESS";
    public static final String TCP_IP = "tcpIp";
    public static final String TCP_PORT = "tcpPort";
    public static final String TERMS_CONDITION = "termsAndCondition";
    public static final String COMPANY_UPI = "companyUpi";
    public static final String COMPANY_TELEPHONE = "companyTelephone";

    public static final String BRANCH_ID = "BranchId";
    public static final String MONTH_INTERVAL = "MonthInterval";
    public static final String LAST_DATE_FINANCIAL_YEAR = "LastDateFinancialYear";
    public static final String CITY_CODE = "CityCode";
    public static final String CONTACT_NAME = "ContactName";
    public static final String BRANCH_NAME = "BranchName";
    public static final String FIRST_DATE_FINANCIAL_YEAR = "FirstDateFinancialYear";
    public static final String COUNTRIES_CODE = "CountriesCode";
    public static final String USER_ID = "UserFrontId";
    public static final String CORPORATE_ID = "CorporateId";
    public static final String FINANCIAL_YEAR = "FinancialYear";
    public static final String COMPANY_ID = "CompanyId";
    public static final String LOGOPREFIX = "LogoPrefix";
    public static final String LOGO = "Logo";
    public static final String STATE_CODE = "StateCode";
    public static final String GSTIN_NO = "GstinNo";
    public static final String ADDRESS = "Address";
    public static final String FSSAI_NO = "FssaiNO";
    public static final String CASHIER_NAME = "CashierName";
    public static final String BASE_URL = "baseurl";
    public static final String BARCODE_SCANNER_SELECTION = "barcodeSelection";
    public static final String COMPANY_LOGO_SELECTION = "companyLogoSelection";
    public static final String QRCODE_SELECTION = "qrCodeSelection";*/

    private static Pattern numberLengthPattern, numberFormatPattern;
    private static String numberFormatStr = "^\\d+$";
    private static String numberLengthStr = "^.{10}$";

    public static KProgressHUD getKProgressHud(Context context) {
        return KProgressHUD.create(context);
    }

    public static void showSnackBar(View parentView, View anchorView, String message) {
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
        snackbar.setAnchorView(anchorView);
        snackbar.show();
    }
    /*public static double getDoubleDecimalValue(double value) {
        return Double.parseDouble(String.format(Locale.getDefault(), "%.2f", value));
    }

    public static double getDoubleFromString(String amount, int decimal) {
        return Double.parseDouble(String.format(Locale.getDefault(), "%." + decimal + "f", Double.parseDouble(amount)));
    }*/

    public static double getDoubleFromString(String amount, int decimal) {
        return Double.parseDouble(String.format(Locale.getDefault(), "%." + decimal + "f", Double.parseDouble(amount)));
    }

    public static double getDoubleDecimalValue(double value) {
        return Double.parseDouble(String.format(Locale.getDefault(), "%.2f", value));
    }

    private static Pattern getNumberFormatPatternInstance() {
        if (numberFormatPattern == null) {
            numberFormatPattern = Pattern.compile(numberFormatStr, Pattern.DOTALL);
            return numberFormatPattern;
        }
        return numberFormatPattern;
    }

    private static Pattern getNumberLengthPatternInstance() {
        if (numberLengthPattern == null) {
            numberLengthPattern = Pattern.compile(numberLengthStr, Pattern.DOTALL);
            return numberLengthPattern;
        }
        return numberLengthPattern;
    }

    public static boolean checkNumberLength(String number) {
        Pattern pattern = CommonUtil.getNumberLengthPatternInstance();
        if (pattern.matcher(String.valueOf(number)).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkNumberFormat(String number) {
        Pattern pattern = CommonUtil.getNumberFormatPatternInstance();
        if (pattern.matcher(String.valueOf(number)).matches()) {
            return true;
        } else {
            return false;
        }
    }


    /*public static KProgressHUD getProgressView(Context context) {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    public static void showSnackBar(View parentView, View anchorView, String message) {
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(anchorView);
        snackbar.show();
    }


    public static double getDoubleDecimalValue(double value) {
        return Double.parseDouble(String.format(Locale.getDefault(), "%.2f", value));
    }

    public final static Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9-]+[_A-Za-z0-9-]*(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public final static Pattern INVALID_EMAIL_PATTERN = Pattern.compile("^[0-9-]+[_0-9-]*(\\.[_0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public final static Pattern PASSWORD_VALIDATION = Pattern.compile("[A-Za-z0-9\\#\\'\\*\\+\\-\\:\\=\\@\\^\\_\\`]+$");
    public final static Pattern NUMBER_VALIDATION = Pattern.compile("\\d+");//[0-9]+
    public final static Pattern PASSWORD_VALIDATION2 =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +     //at least 1 digit
                    *//*"(?=.*[a-z])" +     //at least 1 lower case
                    "(?=.*[A-Z])" +     //at least 1 upper case*//*
                    "(?=.*[a-zA-Z])" +     //any letter
                    "(?=\\S+$)" +       //no white space
                    ".{6,}" +           //at least 6 characters
                    "$");
    public final static Pattern FIRST_LAST_NAME_PATTERN = Pattern.compile("^[A-Za-z]+[A-Za-z0-9-\\.\\-\\_\\']*$");

//INVALID_EMAIL_PATTERN.matcher(email).matches() &&

    public static boolean checkEmail(String email) {
        if (EMAIL_PATTERN.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkFirstLastName(String name) {
        return FIRST_LAST_NAME_PATTERN.matcher(name).matches();
    }

    public static boolean checkPassword(String password) {
        return PASSWORD_VALIDATION.matcher(password).matches();
    }

    public static boolean checkPassword2(String password) {
        return PASSWORD_VALIDATION2.matcher(password).matches();
    }

    public static boolean checkNo(String number) {
        return NUMBER_VALIDATION.matcher(number).matches();
    }

    public static boolean isNullString(String string) {
        try {
            if (string.trim().equalsIgnoreCase("null") || string.trim() == null || string.trim().length() < 0 || string.trim().equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }*/
}
