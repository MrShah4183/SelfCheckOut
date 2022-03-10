package com.vasyerp.selfcheckout.viewmodels.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.models.product.GetAllProducts;
import com.vasyerp.selfcheckout.models.product.ProductVarientsDTO;
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
        this.error = _error;
        this.isLoading = _isLoading;
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

}