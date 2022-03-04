package com.vasyerp.selfcheckout.repositories;

import androidx.annotation.Keep;

@Keep
public interface DataSource<T> {
    void loading(boolean isLoading);
    void error(String errorMessage);
    void data(T data);
}
