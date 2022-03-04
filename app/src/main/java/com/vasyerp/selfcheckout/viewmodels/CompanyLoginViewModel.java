package com.vasyerp.selfcheckout.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.LoginBody;
import com.vasyerp.selfcheckout.repositories.CompanyLoginRepository;
import com.vasyerp.selfcheckout.repositories.DataSource;

public class CompanyLoginViewModel extends ViewModel {
    private CompanyLoginRepository companyLoginRepository;

    private MutableLiveData<LogIn> _getCompanyLoginData = new MutableLiveData<>();
    public LiveData<LogIn> getCompanyLoginData;

    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error;

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading;

    private DataSource<LogIn> dataSource;

    /*public HomeViewModel(HomeRepository homeRepository,long userFrontId) {
        this.homeRepository = homeRepository;
        this.getProductsDto = _getProductDto;
        this.error = _error;
        this.isLoading = _isLoading;
        this.salesResponse = _salesResponse;
        this.serviceableArea = _serviceableArea;
        this.stateList = _stateList;
        this.cityList = _cityList;
        this.savedSalesKey = _savedSalesKey;
        initGetProductDtoDataSource();
        homeRepository.getProduct(dataSource, false,userFrontId);
        getStateList("IN");
        getCityList("24");
        this.syncCountLiveData = homeRepository.getSyncLiveData(userFrontId);
        this.orderDetailsLiveData = homeRepository.getOrderDetailsLiveData(userFrontId);
    }*/

    public CompanyLoginViewModel(CompanyLoginRepository companyLoginRepository, LoginBody loginBody) {
        this.companyLoginRepository = companyLoginRepository;
        this.getCompanyLoginData = _getCompanyLoginData;
        this.error = _error;
        this.isLoading = _isLoading;
        initGetCompanyLoginDataSource();
        //homeRepository.getProduct(dataSource, false,userFrontId);
        companyLoginRepository.companyLoginFromRemote(dataSource, loginBody);
    }

    private void initGetCompanyLoginDataSource() {
        dataSource = new DataSource<LogIn>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(LogIn data) {
                _getCompanyLoginData.postValue(data);
            }
        };
    }


}
