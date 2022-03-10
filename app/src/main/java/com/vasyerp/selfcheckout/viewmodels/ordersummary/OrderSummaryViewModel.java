package com.vasyerp.selfcheckout.viewmodels.ordersummary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vasyerp.selfcheckout.models.ordersummary.OrderSummary;
import com.vasyerp.selfcheckout.repositories.DataSource;
import com.vasyerp.selfcheckout.repositories.OrderSummaryRepository;

public class OrderSummaryViewModel extends ViewModel {
    private OrderSummaryRepository orderSummaryRepository;

    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error;

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading;

    private MutableLiveData<OrderSummary> _orderSummary = new MutableLiveData<>();
    public LiveData<OrderSummary> orderSummary;

    private int branchId;
    private int companyId;
    private int userId;

    public OrderSummaryViewModel(OrderSummaryRepository orderSummaryRepository, int branchId, int companyId, int userId) {
        this.orderSummaryRepository = orderSummaryRepository;
        this.branchId = branchId;
        this.companyId = companyId;
        this.userId = userId;
        this.error = _error;
        this.isLoading = _isLoading;
        this.orderSummary = _orderSummary;
    }

    public void getOrderSummaryDetails(int salesId) {
        orderSummaryRepository.getOrderSummary(new DataSource<OrderSummary>() {
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(OrderSummary data) {
                _orderSummary.postValue(data);
            }
        }, this.branchId, this.companyId, salesId);
    }
}
