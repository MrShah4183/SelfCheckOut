package com.vasyerp.selfcheckout.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vasyerp.selfcheckout.models.login.LoginBody;
import com.vasyerp.selfcheckout.repositories.CompanyLoginRepository;

public class CompanyLoginViewModelFactory implements ViewModelProvider.Factory {

    private final CompanyLoginRepository companyLoginRepository;
    private final LoginBody loginBody;

    public CompanyLoginViewModelFactory(CompanyLoginRepository companyLoginRepository, LoginBody loginBody) {
        this.companyLoginRepository = companyLoginRepository;
        this.loginBody = loginBody;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(CompanyLoginViewModel.class)) {
            return (T) new CompanyLoginViewModel(companyLoginRepository, loginBody);
        }
        throw new IllegalArgumentException("Not assigned from MainViewModel");
        //return null;
    }
}
