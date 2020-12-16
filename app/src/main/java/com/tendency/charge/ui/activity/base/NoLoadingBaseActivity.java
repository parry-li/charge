package com.tendency.charge.ui.activity.base;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.Unbinder;



public abstract class NoLoadingBaseActivity extends BaseActivity {


    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=   ButterKnife.bind(this);

        initTitle();
        initData(savedInstanceState);

    }

    protected abstract void initTitle();

    protected abstract void initData(Bundle savedInstanceState);
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }


    }

}
