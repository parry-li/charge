package com.tendency.charge.http.utils;


import com.tendency.charge.http.LifeSubscription;
import com.tendency.charge.http.Stateful;
import com.tendency.charge.utils.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by parry
 */

public class HttpUtils {

    public static ExecutorService singleThreadPool =  Executors.newFixedThreadPool(5);
    private static Stateful target = null;


    public static <T> void invoke(final LifeSubscription lifecycle, final Observable<T> observable, final Callback<T> callback) {

        if (lifecycle instanceof Stateful) {
            target = (Stateful) lifecycle;
            callback.setTarget(target);
        }
        /**
         * 先判断网络连接状态和网络是否可用，放在回调那里好呢，还是放这里每次请求都去判断下网络是否可用好呢？
         * 如果放在请求前面太耗时了，如果放回掉提示的速度慢，要10秒钟请求超时后才提示。
         * 最后采取的方法是判断网络是否连接放在外面，网络是否可用放在回掉。
         */


/*
        判断网络是否连接
        if (!NetworkUtils.isConnected()) {

            if (target != null) {
                target.setState(BaseConstants.STATE_ERROR);
            }
            return;
        }
*/


        singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    LogUtil.i("线程："+threadName+",正在执行任务");

                    if(lifecycle!=null){
                        Subscription subscription = observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(callback);
                        lifecycle.bindSubscription(subscription);
                    }

                }
            });
    }
}
