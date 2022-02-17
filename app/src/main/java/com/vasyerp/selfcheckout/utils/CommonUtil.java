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

public class CommonUtil {

    //public static final String SCANDIT_LICENSE_KEY = BuildConfig.SCANDIT_API_KEY;
    public static final String SCANDIT_LICENSE_KEY = "";
    public final static String DOMAIN_ID = "DOMAIN_ID";
    public static final String SCANNER_SELECTION_ID = "barcodeSelectionId";
    public static final String REMOTE_CONFIG_SCANNER_KEY = "SCANNER";
    public static final String SCANDIT_API_KEY = "SCANDIT_API_KEY";
    public static final String USERNAME = "USERNAME";
    public static final String PRINTER_TYPE = "printerType";

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


    public static KProgressHUD getProgressView(Context context) {
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
                    /*"(?=.*[a-z])" +     //at least 1 lower case
                    "(?=.*[A-Z])" +     //at least 1 upper case*/
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
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectionDetector cd = new ConnectionDetector(context);
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_no_internet), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return isInternetPresent;
        }
    }


    public static File get_Picture_bitmap(String imagePath) {

        File imageFile = new File(imagePath);

        long size_file = getFileSize(imageFile);

        Log.i("CommonUtil", "Initial image size : " + size_file);

        size_file = (size_file) / 1000;// in Kb now
        int ample_size = 1;

        if (size_file <= 250) {

            Log.i("CommonUtil", "SSSSS1111= " + size_file);
            ample_size = 2;

        } else if (size_file > 251 && size_file < 1500) {

            Log.i("CommonUtil", "SSSSS2222= " + size_file);
            ample_size = 4;

        } else if (size_file >= 1500 && size_file < 3000) {

            Log.i("CommonUtil", "SSSSS3333= " + size_file);
            ample_size = 8;

        } else if (size_file >= 3000 && size_file <= 4500) {

            Log.i("CommonUtil", "SSSSS4444= " + size_file);
            ample_size = 12;

        } else if (size_file >= 4500) {

            Log.i("CommonUtil", "SSSSS5555= " + size_file);
            ample_size = 16;
        } else {
            Log.i("CommonUtil", "SSSSS6666= " + size_file);
        }

        Bitmap bitmap = null;

        BitmapFactory.Options bitoption = new BitmapFactory.Options();
        bitoption.inSampleSize = ample_size;

        Bitmap bitmapPhoto = BitmapFactory.decodeFile(imagePath, bitoption);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
        int orientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        Matrix matrix = new Matrix();

        if ((orientation == 3)) {
            matrix.postRotate(180);
            bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
                    bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
                    true);

        } else if (orientation == 6) {
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
                    bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
                    true);

        } else if (orientation == 8) {
            matrix.postRotate(270);
            bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
                    bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
                    true);

        } else {
            matrix.postRotate(0);
            bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
                    bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
                    true);

        }

        try {
            imageFile.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("CommonUtil", "Final image size : " + getFileSize(imageFile));

        return imageFile;
    }


    public static long getFileSize(final File file) {
        if (file == null || !file.exists())
            return 0;
        if (!file.isDirectory())
            return file.length();
        final List<File> dirs = new LinkedList<File>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists())
                continue;
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0)
                continue;
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory())
                    dirs.add(child);
            }
        }

        return result;
    }

    /*public static void setCircularImageToGlide(final Context context, final CircleImageView imageView, String imageUrl) {
        Glide.with(context).load("" + imageUrl).asBitmap().placeholder(R.drawable.ic_person).error(R.drawable.ic_person).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(circularBitmapDrawable);
    }
});
        }*/
}
