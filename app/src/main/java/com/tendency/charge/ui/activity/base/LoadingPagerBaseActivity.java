package com.tendency.charge.ui.activity.base;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.tendency.charge.http.Stateful;
import com.tendency.charge.service.BasePresenter;
import com.tendency.charge.view.LoadingPage;

import butterknife.ButterKnife;
import butterknife.Unbinder;



public abstract class LoadingPagerBaseActivity<T extends BasePresenter> extends BaseActivity implements Stateful {

    protected LoadingPage mLoadingPage;
    protected T mPresenter;
    private Unbinder bind;
    protected FrameLayout flBaseContent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        mPresenter = setAdapter();
        mPresenter.attachView(this);
        flBaseContent = (FrameLayout) findViewById(setFrameLayoutId());
        if (mLoadingPage == null) {
            mLoadingPage = new LoadingPage(this) {
                @Override
                protected void initView() {
                    bind = ButterKnife.bind(LoadingPagerBaseActivity.this, contentView);
                    LoadingPagerBaseActivity.this.initData(savedInstanceState);
                }

                @Override
                protected void loadData() {
                    LoadingPagerBaseActivity.this.loadData();
                }

                @Override
                protected int getLayoutId() {
                    return LoadingPagerBaseActivity.this.getContentLayoutId();
                }
            };
        }
        flBaseContent.addView(mLoadingPage);
        loadData();
    }

    protected abstract T setAdapter();

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

    @Override
    public void setState(int state) {
        mLoadingPage.state = state;
        mLoadingPage.showPage();
    }


    public void setCommomTitle(String title) {
//        RelativeLayout comTitleBack = findViewById(R.id.com_title_back);
//        TextView comTitleTv = findViewById(R.id.com_title_tv);
//
//
//        comTitleTv.setText(title);
//        titleSetBackClickListener(comTitleBack);
    }

    /**
     * 1
     * 根据网络获取的数据返回状态，每一个子类的获取网络返回的都不一样，所以要交给子类去完成
     * * 如果是静态页面不需要网络请求的在子类的loadData方法中添加以下2行即可
     * mLoadingPage.state = STATE_SUCCESS;
     * mLoadingPage.showPage();
     * 或者调用setState(BaseConstants.STATE_SUCCESS)
     */
    protected abstract void loadData();

    /**
     * 2
     * 网络请求成功在去加载布局
     *
     * @return
     */
    public abstract int getContentLayoutId();

    /**
     * 3
     * 子类关于View的操作(如setAdapter)都必须在这里面，会因为页面状态不为成功，而binding还没创建就引用而导致空指针。
     * loadData()和initData只执行一次，如果有一些请求需要二次的不要放到loadData()里面。
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);


    //子类只需要实现下面两个方法 孙子类需要实现所有。子类还需要实现BaseActivity的getLayoutId();

    /**
     * 获取子类FrameLayout的Id是孙子类的容器
     *
     * @return
     */
    public abstract int setFrameLayoutId();

    /**
     * 用于子类初始化findviewbyid的。
     * 这里初始化的Id是为了子类能公用的。
     */
    protected abstract void initUI();

}
