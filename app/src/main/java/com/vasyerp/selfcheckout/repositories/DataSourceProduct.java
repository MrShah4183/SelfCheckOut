package com.vasyerp.selfcheckout.repositories;

import androidx.annotation.Keep;

@Keep
public interface DataSourceProduct<T, T1> {
    void loading(boolean isLoading);

    void error(String errorMessage);

    void data(T data);

    void data2(T1 data2);
}
