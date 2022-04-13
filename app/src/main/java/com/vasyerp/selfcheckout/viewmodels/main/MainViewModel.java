package com.vasyerp.selfcheckout.viewmodels.main;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vasyerp.selfcheckout.models.product.GetAllProducts;
import com.vasyerp.selfcheckout.models.product.ProductVarientsDTO;
import com.vasyerp.selfcheckout.models.product.StockMasterVo;
import com.vasyerp.selfcheckout.models.savebill.SaveBill;
import com.vasyerp.selfcheckout.models.savebill.SaveBillStatusModel;
import com.vasyerp.selfcheckout.models.savebill.SaveBillResponse;
import com.vasyerp.selfcheckout.models.savebill.UpdateBillResponse;
import com.vasyerp.selfcheckout.repositories.DataSource;
import com.vasyerp.selfcheckout.repositories.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel {
    private MainRepository mainRepository;

    /*private MutableLiveData<LogIn> _getCompanyLoginData = new MutableLiveData<>();
    public LiveData<LogIn> getCompanyLoginData;*/

    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error;

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading;

    private MutableLiveData<List<GetAllProducts>> _productList = new MutableLiveData<>();
    public LiveData<List<GetAllProducts>> productList;

    private MutableLiveData<ProductVarientsDTO> _getProduct = new MutableLiveData<>();
    public LiveData<ProductVarientsDTO> getProduct;

    private MutableLiveData<UpdateBillResponse> _billStatusResponse = new MutableLiveData<>();
    public LiveData<UpdateBillResponse> billStatusResponse;

    private MutableLiveData<SaveBillResponse> _saveBillResponse = new MutableLiveData<>();
    public LiveData<SaveBillResponse> saveBillResponse;

    private MutableLiveData<Integer> _cartListCountData = new MutableLiveData<>();
    public LiveData<Integer> cartListCountData;

    private MutableLiveData<List<StockMasterVo>> _cartListData = new MutableLiveData<>();
    public LiveData<List<StockMasterVo>> cartListData;

    private DataSource<List<GetAllProducts>> dataSourceGetAllProducts;
    private DataSource<List<ProductVarientsDTO>> dataSourceProductVarientDTO;

    private int companyId;
    private int branchId;
    private int userId;


    public MainViewModel(MainRepository mainRepository, int companyId, int branchId, int userId) {
        this.companyId = companyId;
        this.branchId = branchId;
        this.userId = userId;
        this.mainRepository = mainRepository;
        this.getProduct = _getProduct;
        this.productList = _productList;
        this.saveBillResponse = _saveBillResponse;
        this.billStatusResponse = _billStatusResponse;
        this.error = _error;
        this.isLoading = _isLoading;
        this.cartListCountData = _cartListCountData;
        this.cartListData = _cartListData;
        initGetProductDataSource();
        getAllProductList(companyId);
    }

    private void initGetProductDataSource() {
        dataSourceGetAllProducts = new DataSource<List<GetAllProducts>>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(List<GetAllProducts> data) {
                _productList.postValue(data);
            }
        };
    }

    private void getAllProductList(int intCompanyId) {
        mainRepository.getAllProductListApiCall(new DataSource<List<GetAllProducts>>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(List<GetAllProducts> data) {
                _productList.postValue(data);
            }
        }, intCompanyId);
    }

    public void postOrderData(int companyId, int branchId, int userId, SaveBill saveBill) {
        mainRepository.postOrder(new DataSource<SaveBillResponse>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(SaveBillResponse data) {
                _saveBillResponse.postValue(data);
            }
        }, companyId, branchId, userId, saveBill);
    }

    public void updateOrderStatus(int companyId, int branchId, int userId, SaveBillStatusModel saveBillStatusModel) {
        mainRepository.updateSaveBillStatusApiCall(new DataSource<UpdateBillResponse>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(UpdateBillResponse data) {
                _billStatusResponse.postValue(data);
            }
        }, companyId, branchId, userId, saveBillStatusModel);
    }

    public void saveDataOfOrder(SaveBillResponse saveBillResponse, SaveBill saveBill) {
        mainRepository.insertOrderData(saveBillResponse, saveBill);
    }

    public void updateSaveBillResponseStatus(SaveBillResponse saveBillResponse) {
        mainRepository.updateSaveBillResponseStatusRepo(saveBillResponse);
    }

    //public void getProductByBarcodeId(DataSource<ProductVarientsDTO> dataSource, String financialYear, String productId, boolean isSearchByBarcode,
    // String branchId, String companyId) {
    public void getProductByBarcodeId(String financialYear, String productId, boolean isSearchByBarcode) {
        mainRepository.getProductByBarcodeId(new DataSource<ProductVarientsDTO>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(ProductVarientsDTO data) {
                _getProduct.postValue(data);
            }
        }, financialYear, productId, isSearchByBarcode, String.valueOf(this.branchId), String.valueOf(this.companyId));
    }

    public void insertCartDataInDB(StockMasterVo stockMasterVo) {
        stockMasterVo.setCompanyId(companyId);
        stockMasterVo.setBranchId(branchId);
        stockMasterVo.setUserFrontId(userId);
        //stockMasterVo.setProductName(stockMasterVo.getProductDto().getDisplay_name());

        new Handler().postDelayed(() -> mainRepository.insertCartData(stockMasterVo), 200);
    }

    public void insertAllListCartDataInDb(List<StockMasterVo> stockMasterVoList) {
        for (int i = 0; i < stockMasterVoList.size(); i++) {
            stockMasterVoList.get(i).setCompanyId(companyId);
            stockMasterVoList.get(i).setBranchId(branchId);
            stockMasterVoList.get(i).setUserFrontId(userId);
        }
        new Handler().postDelayed(() -> mainRepository.insertAllCartList(stockMasterVoList), 300);
    }

    public void deleteSingleCartDataFromDB(long productVarientId) {
        mainRepository.deleteSingleCartData(productVarientId, branchId, companyId);
    }

    public void getAllCartProductListFromDB() {
        mainRepository.getAllCartProductList(new DataSource<List<StockMasterVo>>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(List<StockMasterVo> data) {
                _cartListData.postValue(data);
            }
        }, branchId, companyId);
    }

    public void deleteAllCartListFromDB() {
        mainRepository.deleteAllCartList(branchId, companyId);
    }

    public void getTotalProductCountDB() {
        mainRepository.getTotalProductsCount(new DataSource<Integer>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(Integer data) {
                _cartListCountData.postValue(data);
            }
        }, branchId, companyId);
    }


}
