package com.vasyerp.selfcheckout.viewmodels.login_registration;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vasyerp.selfcheckout.repositories.LoginRegistrationRepository;

public class LoginRegistrationViewModelFactory implements ViewModelProvider.Factory {

    private final LoginRegistrationRepository repository;
    private final int companyId;
    private final int branchId;
    private final int userId;

    public LoginRegistrationViewModelFactory(LoginRegistrationRepository repository, int companyId, int branchId, int userId) {
        this.repository = repository;
        this.companyId = companyId;
        this.branchId = branchId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(LoginRegistrationViewModel.class)) {
            return (T) new LoginRegistrationViewModel(repository, companyId, branchId, userId);
        }
        throw new IllegalArgumentException("Not assigned from LoginRegistrationViewModel");
    }
}
