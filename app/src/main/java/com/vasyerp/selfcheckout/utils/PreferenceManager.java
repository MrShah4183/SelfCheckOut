package com.vasyerp.selfcheckout.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    //public static PreferenceManager preferenceManager;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    public static String SHAREDPREFERENCE = "MY_SHAREDPREFERENCE";

    public static void savePref(Context context, String value, String key) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static void removePref(Context context, String keyToRemove) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.remove(keyToRemove);
        mEditor.commit();
    }

    public static long getBarcodeSelectionId(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        long barCodeType = mSharedPreferences.getLong(CommonUtil.SCANNER_SELECTION_ID, 3); // false means default is scandit.
        return barCodeType;
    }

    public static void setBarcodeSelectionId(Context context, String key, long value) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public static String getScanditApiKey(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String apiKey = mSharedPreferences.getString(CommonUtil.SCANDIT_API_KEY, CommonUtil.SCANDIT_LICENSE_KEY); // false means default is scandit.
        return apiKey;
    }

    public static void setScanditApiKey(Context context, String key, String value) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static String getUserName(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(CommonUtil.USERNAME, "username not found.");
    }

    //domain
    public static String getDomain(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strDomainId = mSharedPreferences.getString(CommonUtil.DOMAIN_ID, null);
        return strDomainId;
    }

    public static long getPrinterType(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        long barCodeType = mSharedPreferences.getLong(CommonUtil.PRINTER_TYPE, 1); // false means default is scandit.
        return barCodeType;
    }

    public static void setPrinterType(Context context, String key, long value) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }
}
