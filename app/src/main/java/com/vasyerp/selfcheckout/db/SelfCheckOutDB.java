package com.vasyerp.selfcheckout.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vasyerp.selfcheckout.models.login.LogIn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LogIn.class}, version = 1,exportSchema = true)
public abstract class SelfCheckOutDB extends RoomDatabase{
    public abstract SelfCheckOutDao getSelfCheckOutDao();

    private static volatile SelfCheckOutDB instance = null;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static synchronized  SelfCheckOutDB getInstance(Context context){
        if (instance != null){
            return instance;
        }else {
            instance = Room.databaseBuilder(
                    context,
                    SelfCheckOutDB.class,
                    "self_check_out_db"
            ).build();
            return instance;
        }
    }
}
