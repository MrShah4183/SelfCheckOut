package com.vasyerp.selfcheckout.db;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.product.StockMasterVo;
import com.vasyerp.selfcheckout.models.savebill.SalesDTO;
import com.vasyerp.selfcheckout.models.savebill.SaveBill;
import com.vasyerp.selfcheckout.models.savebill.SaveBillResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*@Database(entities = {LogIn.class,
        SaveBillResponse.class,
        SaveBill.class,
        SalesDTO.class},
        version = 1, exportSchema = true)*/

@Database(entities = {LogIn.class,
        SaveBillResponse.class,
        SaveBill.class,
        StockMasterVo.class,
        SalesDTO.class},
        version = 2,
        autoMigrations = {
                @AutoMigration(from = 1, to = 2)
        },
        exportSchema = true)
public abstract class SelfCheckOutDB extends RoomDatabase {
    public abstract SelfCheckOutDao getSelfCheckOutDao();

    private static volatile SelfCheckOutDB instance = null;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static synchronized SelfCheckOutDB getInstance(Context context) {
        if (instance != null) {
            return instance;
        } else {
            instance = Room.databaseBuilder(
                    context,
                    SelfCheckOutDB.class,
                    "self_check_out_db"
            ).build();
            return instance;
        }
    }
}
