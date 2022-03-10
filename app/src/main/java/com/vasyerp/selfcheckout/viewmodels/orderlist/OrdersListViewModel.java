package com.vasyerp.selfcheckout.viewmodels.orderlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vasyerp.selfcheckout.models.orderlist.OrdersListResponse;
import com.vasyerp.selfcheckout.repositories.DataSource;
import com.vasyerp.selfcheckout.repositories.OrdersListRepository;

public class OrdersListViewModel extends ViewModel {
    private OrdersListRepository ordersListRepository;

    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error;

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading;

    private MutableLiveData<OrdersListResponse> _ordersList = new MutableLiveData<>();
    public LiveData<OrdersListResponse> ordersList;

    private int branchId;
    private int companyId;
    private int userId;

    public OrdersListViewModel(OrdersListRepository ordersListRepository, int branchId, int companyId, int userId) {
        this.ordersListRepository = ordersListRepository;
        this.branchId = branchId;
        this.companyId = companyId;
        this.userId = userId;
        this.error = _error;
        this.isLoading = _isLoading;
        this.ordersList = _ordersList;
    }

    public void getAllCustomerOrders(int pageNo, int limit, int contactId) {
        ordersListRepository.getAllOrdersList(new DataSource<OrdersListResponse>() {
            @Override
            public void loading(boolean isLoading) {
                _isLoading.postValue(isLoading);
            }

            @Override
            public void error(String errorMessage) {
                _error.postValue(errorMessage);
            }

            @Override
            public void data(OrdersListResponse data) {
                _ordersList.postValue(data);
            }
        }, pageNo, limit, String.valueOf(this.branchId), String.valueOf(this.companyId), contactId);
    }
}
