package com.vasyerp.selfcheckout.viewmodels.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vasyerp.selfcheckout.repositories.MainRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final MainRepository mainRepository;
    private final int companyId;
    private final int branchId;
    private final int userId;
    //private final MainRepository mainRepository;


    public MainViewModelFactory(MainRepository mainRepository, int companyId, int branchId, int userId) {
        this.mainRepository = mainRepository;
        this.companyId = companyId;
        this.branchId = branchId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mainRepository, companyId, branchId, userId);
        }
        throw new IllegalArgumentException("Not assigned from MainViewModel");
    }
}
