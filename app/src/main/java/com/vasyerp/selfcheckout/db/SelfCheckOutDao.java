package com.vasyerp.selfcheckout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vasyerp.selfcheckout.models.login.LogIn;

import java.util.List;

@Dao
public interface SelfCheckOutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addStore(List<LogIn> trackingItemDtos);

    @Query("SELECT COUNT(*) FROM LOGIN")
    int getTotalRow();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSingleStore(LogIn logIn);

    @Query("SELECT LOGIN.tempId FROM LogIn WHERE LOGIN.branchId = :branchId AND LOGIN.companyId = :companyId")
    int checkDataIsExist(String branchId, String companyId);

    @Query("SELECT * FROM LOGIN")
    List<LogIn> getAllStoreData();

    //@Query("UPDATE LOGIN ")
    //SELECT COUNT(*) FROM TableName
    //SELECT * FROM TRACKINGITEMDTO WHERE TRACKINGITEMDTO.productVarientId = :proVarId
}
