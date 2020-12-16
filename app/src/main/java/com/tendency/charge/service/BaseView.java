package com.tendency.charge.service;



public interface BaseView<T> {
    void loadingSuccessForData(T mData);
    void loadingFail(String msg);
    void loadingApiError(String msg);
}
