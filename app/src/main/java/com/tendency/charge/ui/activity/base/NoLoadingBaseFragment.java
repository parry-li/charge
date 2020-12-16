package com.tendency.charge.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tendency.charge.http.LifeSubscription;
import com.tendency.charge.http.Stateful;
import com.tendency.charge.service.BasePresenter;
import com.tendency.charge.view.LoadingPage;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by parry
 */

public abstract class NoLoadingBaseFragment<P extends BasePresenter> extends BaseFragment implements LifeSubscription, Stateful {

    protected P mPresenter;


    public boolean isRefresh = true;

    public LoadingPage mLoadingPage;

    public boolean mIsVisible = false;     // fragment是否显示了

    private boolean isPrepared = false;

    private boolean isFirst = true; //只加载一次界面



    private Unbinder bind;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        bind = ButterKnife.bind(NoLoadingBaseFragment.this, view);
        loadBaseData();
        initView();


        return view;


    }


    protected abstract P setPresenter();

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//fragment可见
            mIsVisible = true;
            onVisible();
        } else {//fragment不可见
            mIsVisible = false;
            onInvisible();
        }
    }

    @Override
    public void setState(int state) {
        mLoadingPage.state = state;
        if (isRefresh) {
            mLoadingPage.showPage();
        }

    }



    protected void onInvisible() {
    }

    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected void onVisible() {
        if (isFirst) {
            //            initInject();
            mPresenter = setPresenter();
            if (mPresenter != null) {
                mPresenter.attachView(this);
            }
        }
        loadBaseData();//根据获取的数据来调用showView()切换界面
    }


    public void loadBaseData() {
        if (!mIsVisible || !isPrepared || !isFirst) {
            return;
        }

    }



    /**
     * 2
     * 网络请求成功在去加载布局
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 3
     * 子类关于View的操作(如setAdapter)都必须在这里面，会因为页面状态不为成功，而binding还没创建就引用而导致空指针。
     * loadData()和initView只执行一次，如果有一些请求需要二次的不要放到loadData()里面。
     */
    protected abstract void initView();




    private CompositeSubscription mCompositeSubscription;

    //用于添加rx的监听的在onDestroy中记得关闭不然会内存泄漏。
    @Override
    public void bindSubscription(Subscription subscription) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }




    @Override
    public void onDetach() {
        super.onDetach();
        if (bind != null) {
            bind.unbind();
        }
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

}
