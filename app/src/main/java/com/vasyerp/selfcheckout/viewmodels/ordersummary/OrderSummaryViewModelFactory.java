package com.vasyerp.selfcheckout.viewmodels.ordersummary;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vasyerp.selfcheckout.repositories.OrderSummaryRepository;

public class OrderSummaryViewModelFactory implements ViewModelProvider.Factory {

    private final OrderSummaryRepository orderSummaryRepository;
    private final int companyId;
    private final int branchId;
    private final int userId;

    public OrderSummaryViewModelFactory(OrderSummaryRepository orderSummaryRepository, int companyId, int branchId, int userId) {
        this.orderSummaryRepository = orderSummaryRepository;
        this.companyId = companyId;
        this.branchId = branchId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(OrderSummaryViewModel.class)) {
            return (T) new OrderSummaryViewModel(orderSummaryRepository, companyId, branchId, userId);
        }
        throw new IllegalArgumentException("Not assigned from OrderSummaryViewModel");
    }
}
