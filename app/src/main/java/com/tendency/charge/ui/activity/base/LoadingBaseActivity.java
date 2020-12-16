package com.tendency.charge.ui.activity.base;

import android.os.Bundle;

import com.tendency.charge.http.Stateful;
import com.tendency.charge.service.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class LoadingBaseActivity<T extends BasePresenter> extends BaseActivity implements Stateful {


    protected T mPresenter;
    private Unbinder bind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ButterKnife.bind(this);
        mPresenter = setPresenter();
        mPresenter.attachView(this);
        loadData();
        initTitle();
        initData(savedInstanceState);


    }

    @Override
    public void setState(int state) {

    }

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initTitle();

    protected abstract void loadData();

    protected abstract T setPresenter();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }


}
