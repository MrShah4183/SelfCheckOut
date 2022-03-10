package com.vasyerp.selfcheckout.viewmodels.orderlist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vasyerp.selfcheckout.repositories.OrdersListRepository;

public class OrdersListViewModelFactory implements ViewModelProvider.Factory {

    private final OrdersListRepository ordersListRepository;
    private final int companyId;
    private final int branchId;
    private final int userId;

    public OrdersListViewModelFactory(OrdersListRepository ordersListRepository, int companyId, int branchId, int userId) {
        this.ordersListRepository = ordersListRepository;
        this.companyId = companyId;
        this.branchId = branchId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(OrdersListViewModel.class)) {
            return (T) new OrdersListViewModel(ordersListRepository, companyId, branchId, userId);
        }
        throw new IllegalArgumentException("Not assigned from OrdersListViewModel");
        //return null;
    }
}
