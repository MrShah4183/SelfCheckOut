package com.vasyerp.selfcheckout;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.vasyerp.selfcheckout.db.SelfCheckOutDB;
import com.vasyerp.selfcheckout.db.SelfCheckOutDao;

public class SelfCheckOutApp extends Application {
    public SelfCheckOutDB selfCheckOutDB;
    public SelfCheckOutDao selfCheckOutDao;

    @Override
    public void onCreate() {
        selfCheckOutDB = SelfCheckOutDB.getInstance(this);
        selfCheckOutDao = selfCheckOutDB.getSelfCheckOutDao();
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
