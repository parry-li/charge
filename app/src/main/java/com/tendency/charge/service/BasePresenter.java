package com.tendency.charge.service;


import com.parry.utils.code.LogUtils;
import com.parry.utils.code.SPUtils;
import com.tendency.charge.constants.BaseConstants;
import com.tendency.charge.constants.UrlConstants;
import com.tendency.charge.http.LifeSubscription;
import com.tendency.charge.http.Stateful;
import com.tendency.charge.http.utils.Callback;
import com.tendency.charge.http.utils.HttpUtils;
import com.tendency.charge.http.utils.JsonConverterFactory;
import com.tendency.charge.utils.LogUtil;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Created by parry
 */

public class BasePresenter<T extends BaseView> {

    protected Reference<T> mReferenceView;
    private final Charset UTF8 = Charset.forName("UTF-8");
    protected T mView;
    private Callback callback;

    public void attachView(LifeSubscription mLifeSubscription) {
        this.mReferenceView = new WeakReference<>((T) mLifeSubscription);
        mView = mReferenceView.get();
    }

    protected <T> void invoke(Observable<T> observable, Callback<T> callback) {
        this.callback = callback;
        HttpUtils.invoke((LifeSubscription) mView, observable, callback);


    }

    public Object setRetrofitService(Class A) {

        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //自定义OkHttpClient
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //添加拦截器
        okHttpClient.addInterceptor(httpLoggingInterceptor);

        okHttpClient.readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES);
//
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                String token = SPUtils.getInstance().getString(BaseConstants.token);
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("token", token)
                        .header("content-type", "application/json;charset:utf-8");
                Request request = requestBuilder.build();


                //获得请求信息，此处如有需要可以添加headers信息
                try {
                    printLog(request,token);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //记录请求耗时
                long startNs = System.nanoTime();
                okhttp3.Response response;
                try {
                    //发送请求，获得相应，
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }

                //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，原因在于这个方法会消耗返回结果的数据(buffer)
                ResponseBody responseBody = response.body();
                //为了不消耗buffer，我们这里使用source先获得buffer对象，然后clone()后使用
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                //获得返回的数据
                Buffer buffer = source.buffer();
                //使用前clone()下，避免直接消耗
                LogUtil.d("response:" + buffer.clone().readString(Charset.forName("UTF-8")));
                return response;
            }
        });
        //创建并指定自定义的OkHttpClient
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConstants.main_host_service)
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient.build()).build();


        return retrofit.create(A);
    }



    private void printLog(Request request,String token) {
        //打印请求信息
        try {
//            if (BuildConfig.DEBUG) {
                if ("POST".equals(request.method())) {
                    StringBuilder sb = new StringBuilder();
                    if (request.body() instanceof FormBody) {
                        FormBody body = (FormBody) request.body();
                        for (int i = 0; i < body.size(); i++) {
                            sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                        }
                        sb.delete(sb.length() - 1, sb.length());
                        LogUtils.w("RequestToken: " + token,"RequestUrl:" + request.url(), "RequestMethod:" + request.method(), "RequestParams:{" + sb.toString() + "}");
                    } else {
                        RequestBody requestBody = request.body();
                        String body = null;
                        if (requestBody != null) {
                            Buffer buffer = new Buffer();
                            requestBody.writeTo(buffer);
                            Charset charset = UTF8;
                            MediaType contentType = requestBody.contentType();
                            if (contentType != null) {
                                charset = contentType.charset(UTF8);
                            }
                            body = buffer.readString(charset);
                            LogUtils.w("RequestToken: " + token,"RequestUrl: " + request.url(), "RequestMethod: " + request.method(), "RequestParams:   " + body);
                        }


                    }
                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给子类检查返回集合是否为空
     * 这样子做虽然耦合度高，但是接口都不是统一定的，我们没有什么更好的办法
     *
     * @param list
     */
    public void checkState(List list) {
        if (list.size() == 0) {
            if (mView instanceof Stateful)
                ((Stateful) mView).setState(BaseConstants.STATE_EMPTY);
            return;
        }
    }

    public void detachView() {
        if (mReferenceView != null)
            mReferenceView.clear();
        mReferenceView = null;
        if (mView != null)
            mView = null;
        if (callback != null) {
            callback.detachView();
        }
    }
}
