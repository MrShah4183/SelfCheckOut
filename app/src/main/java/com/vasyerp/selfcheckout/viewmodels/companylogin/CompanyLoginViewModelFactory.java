package com.vasyerp.selfcheckout.viewmodels.companylogin;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vasyerp.selfcheckout.repositories.CompanyLoginRepository;

public class CompanyLoginViewModelFactory implements ViewModelProvider.Factory {

    private final CompanyLoginRepository companyLoginRepository;

    public CompanyLoginViewModelFactory(CompanyLoginRepository companyLoginRepository) {
        this.companyLoginRepository = companyLoginRepository;
        //this.loginBody = loginBody;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(CompanyLoginViewModel.class)) {
            return (T) new CompanyLoginViewModel(companyLoginRepository);
        }
        throw new IllegalArgumentException("Not assigned from MainViewModel");
        //return null;
    }
}
