package com.vasyerp.selfcheckout.viewmodels.login_registration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vasyerp.selfcheckout.models.customer.City;
import com.vasyerp.selfcheckout.models.customer.Country;
import com.vasyerp.selfcheckout.models.customer.CreateCustomerBody;
import com.vasyerp.selfcheckout.models.customer.CustomerDetails;
import com.vasyerp.selfcheckout.models.customer.LoginCustomerDetails;
import com.vasyerp.selfcheckout.models.customer.State;
import com.vasyerp.selfcheckout.repositories.DataSource;
import com.vasyerp.selfcheckout.repositories.LoginRegistrationRepository;

import java.util.List;

public class LoginRegistrationViewModel extends ViewModel {
    private LoginRegistrationRepository repository;

    private MutableLiveData<List<Country>> _country = new MutableLiveData<>();
    public LiveData<List<Country>> country;

    private MutableLiveData<List<State>> _state = new MutableLiveData<>();
    public LiveData<List<State>> state;

    private MutableLiveData<List<City>> _city = new MutableLiveData<>();
    public LiveData<List<City>> city;

    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error;

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading;

    private MutableLiveData<CustomerDetails> _getCustomerResponse = new MutableLiveData<>();
    public LiveData<CustomerDetails> getCustomerResponse;

    private MutableLiveData<LoginCustomerDetails> _loginGetCustomerResponse = new MutableLiveData<>();
    public LiveData<LoginCustomerDetails> loginGetCustomerResponse;


    private int branchId;
    private int companyId;
    private int userId;

    public LoginRegistrationViewModel(LoginRegistrationRepository repository, int branchId, int companyId, int userId) {
        this.repository = repository;
        this.branchId = branchId;
        this.companyId = companyId;
        this.userId = userId;
        this.error = _error;
        this.isLoading = _isLoading;
        this.country = _country;
        this.state = _state;
        this.city = _city;
        this.getCustomerResponse = _getCustomerResponse;
        this.loginGetCustomerResponse = _loginGetCustomerResponse;
    }

    public void getCountryListApiCall() {
        repository.getCountryList(new DataSource<List<Country>>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(List<Country> data) {
                _country.postValue(data);
            }
        });
    }

    public void getStateListApiCall(String countryId) {
        repository.getStateList(new DataSource<List<State>>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(List<State> data) {
                _state.postValue(data);
            }
        }, countryId);
    }

    public void getCityListApiCall(String stateId) {
        repository.getCityList(new DataSource<List<City>>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(List<City> data) {
                _city.postValue(data);
            }
        }, stateId);
    }

    /*@POST("/mpos/api/v2/createcontact")
    Call<ApiResponse<CustomerDetails>> addNewCustomer(
            @Query("branchId") int branchId,
            @Query("companyId") int companyId,
            @Query("userId") int userId,
            @Query("type") String type,
            @Body CreateCustomerBody customerBody);*/
    public void createCustomerApiCall(CreateCustomerBody customerBody) {
        String type = "customers";
        repository.customerRegistration(new DataSource<CustomerDetails>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(CustomerDetails data) {
                _getCustomerResponse.postValue(data);
            }
        }, branchId, companyId, userId, type, customerBody);
    }

    public void userLoginCompanyApiCall(String mobileNo){
        repository.userLoginInCompany(new DataSource<LoginCustomerDetails>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(LoginCustomerDetails data) {
                _loginGetCustomerResponse.postValue(data);
            }
        },branchId,companyId,mobileNo);
    }
}
