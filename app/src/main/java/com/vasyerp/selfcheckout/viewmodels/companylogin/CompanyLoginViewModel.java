package com.vasyerp.selfcheckout.viewmodels.companylogin;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.CompanyCustomerBody;
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

    public CompanyLoginViewModel(CompanyLoginRepository companyLoginRepository) {
        this.companyLoginRepository = companyLoginRepository;
        this.getCompanyLoginData = _getCompanyLoginData;
        this.error = _error;
        this.isLoading = _isLoading;
        initGetCompanyLoginDataSource();
        //homeRepository.getProduct(dataSource, false,userFrontId);
        //companyLoginRepository.companyLoginFromRemote(dataSource, loginBody);
    }

    private void initGetCompanyLoginDataSource() {
        Log.e("TAG", "companyLogin: call model method");
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

    public void companyLogin(CompanyCustomerBody companyCustomerBody) {
        companyLoginRepository.companyLoginFromRemote(new DataSource<LogIn>() {
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
        }, companyCustomerBody);
    }


}
