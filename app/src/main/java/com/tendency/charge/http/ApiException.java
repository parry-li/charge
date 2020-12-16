package com.tendency.charge.http;

import java.io.IOException;

public class ApiException extends IOException {
    /*无参构造函数*/
    public ApiException() {
        super();
    }

    //用详细信息指定一个异常
    public ApiException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public ApiException(Throwable cause) {
        super(cause);
    }
}