package com.tendency.charge.http.utils;


import com.tendency.charge.constants.BaseConstants;
import com.tendency.charge.http.ApiException;
import com.tendency.charge.http.Stateful;
import com.tendency.charge.service.BaseView;
import com.tendency.charge.utils.LogUtil;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by parry
 */

public class Callback<T> extends Subscriber<T> {
    private Stateful target;

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public void setTarget(Stateful target) {
        this.target = target;
    }

    public void detachView() {
        if (target != null) {
            target = null;
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        try {
            if (e instanceof HttpException) {             //HTTP错误
                HttpException httpException = (HttpException) e;
                LogUtil.i("httpException.code()      "+httpException.code());
                switch (httpException.code()) {
                    case UNAUTHORIZED:
                    case FORBIDDEN:
                        onFail("网络请求权限错误，请检查网络（Http："+httpException.code()+")");  //权限错误，需要实现
                        break;
                    case NOT_FOUND:
                    case REQUEST_TIMEOUT:
                    case GATEWAY_TIMEOUT:
                        onFail("网络请求超时，请检查网络（Http："+httpException.code()+")");
                        break;
                    case INTERNAL_SERVER_ERROR:
                    case BAD_GATEWAY:
                    case SERVICE_UNAVAILABLE:
                    default:
                        onFail("网络请求失败，请检查网络（Http："+httpException.code()+")");
                        break;
                }
            } else if (e instanceof ApiException) {
                onFailApi(e.getMessage());
            } else {
                onFail("网络请求失败，请检查网络（Http-else："+e.getMessage()+")");
            }
            LogUtil.e("retrofit2_Callback_onError=     " + e.toString());

            return;
        } catch (Exception exception) {
            LogUtil.e("retrofit2_Callback_onError_exception=    " + exception);
            onFail("网络请求失败，请检查网络（exception："+exception.getMessage()+")");
        }


    }

    @Override
    public void onNext(T data) {


        DdcResult result = (DdcResult) data;
        if (result.getCode() == 0) {
            if (target != null) {
                target.setState(BaseConstants.STATE_SUCCESS);
            }
            onResponse(data);
        } else if(result.getCode() == 1007) {
            onFailApi("登录失效，请重新登录");
        }else {
            onFailApi(result.getMsg());
        }


    }

    public void onResponse(T data) {

        ((BaseView) target).loadingSuccessForData(data);

    }


    public void onFail(String msg) {
        if (target != null) {
            target.setState(BaseConstants.STATE_SUCCESS);
            ((BaseView) target).loadingFail(msg);
//            ToastUtil.showWX(msg);
        }

    }
    public void onFailApi(String msg) {
        if (target != null) {
            target.setState(BaseConstants.STATE_SUCCESS);
            ((BaseView) target).loadingApiError(msg);
//            ToastUtil.showFW(msg);
        }

    }
}
