package com.vasyerp.selfcheckout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.savebill.SalesDTO;
import com.vasyerp.selfcheckout.models.savebill.SaveBill;
import com.vasyerp.selfcheckout.models.savebill.SaveBillResponse;

import java.util.List;

@Dao
public interface SelfCheckOutDao {
    /**
     * insert query
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleStore(LogIn logIn);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSaveBillResponse(SaveBillResponse saveBillResponse);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSaveBillData(SaveBill saveBill);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSalesDto(List<SalesDTO> salesDTOList);

    /**
     * select query
     */
    @Query("SELECT COUNT(*) FROM LOGIN")
    int getTotalRow();

    @Query("SELECT LOGIN.tempId FROM LogIn WHERE LOGIN.branchId = :branchId AND LOGIN.companyId = :companyId")
    int checkDataIsExist(String branchId, String companyId);

    @Query("SELECT * FROM LOGIN")
    List<LogIn> getAllStoreData();


    @Update()
    void updateSaveBillResponse(SaveBillResponse saveBillResponse);

    /*UPDATE table_name
SET column1 = value1, column2 = value2, ...
WHERE condition;*/


    //@Query("UPDATE LOGIN ")
    //SELECT COUNT(*) FROM TableName
    //SELECT * FROM TRACKINGITEMDTO WHERE TRACKINGITEMDTO.productVarientId = :proVarId
}
