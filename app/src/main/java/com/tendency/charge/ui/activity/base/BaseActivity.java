package com.tendency.charge.ui.activity.base;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.parry.utils.code.SPUtils;
import com.tendency.charge.R;
import com.tendency.charge.constants.BaseConstants;
import com.tendency.charge.http.LifeSubscription;
import com.tendency.charge.utils.UIUtils;
import com.tendency.charge.view.CustomWindowDialog;
import com.tendency.charge.view.ZProgressHUD;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseActivity extends AppCompatActivity implements LifeSubscription {

    // 管理运行的所有的activity
    public final static List<AppCompatActivity> mActivities = new LinkedList<AppCompatActivity>();

    public static BaseActivity activity;
    //一下变量用于从左边滑动到右边关闭的变量   类似ios自带的关闭效果
    private int endX;
    private int startX;
    private int deltaX;
    private int endY;
    private int startY;
    private int deltaY;

    private View decorView;
    private VelocityTracker mVelocityTracker;
    private boolean isClose = true;
    public String userid;
    public ZProgressHUD zProgressHUD;
    public CustomWindowDialog customBaseDialog;
    private CustomWindowDialog submitRequestDialog;
    public boolean isShowBackDialog = true;//是否显示退出键
    public int systemBaseID;
    public boolean isKillDialogShow =true;// 标题点击退出是 是否显示提示框

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    protected abstract int getLayoutId();

    protected abstract void submitRequestData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        mVelocityTracker = mVelocityTracker.obtain();
        decorView = getWindow().getDecorView();
        synchronized (mActivities) {
            mActivities.add(this);
        }

        systemBaseID = SPUtils.getInstance().getInt(BaseConstants.City_systemID);
        zProgressHUD = new ZProgressHUD(this);
        zProgressHUD.setMessage("加载中");
        /*请求结果的提示*/
        customBaseDialog = new CustomWindowDialog(this);
        customBaseDialog.setOnCustomDialogClickListener(new CustomWindowDialog.OnItemClickListener() {
            @Override
            public void onCustomDialogClickListener() {
                finish();
            }
        });
        /*提交数据的提示*/
        submitRequestDialog = new CustomWindowDialog(this);
        submitRequestDialog.setOnCustomDialogClickListener(new CustomWindowDialog.OnItemClickListener() {
            @Override
            public void onCustomDialogClickListener() {

                if (UIUtils.isFastClick()) {
                    submitRequestData();
                }

            }
        });
    }



    public RequestBody getSubmitBody() {
        return submitBody;
    }

    private RequestBody submitBody;

    public RequestBody getRequestBody(Map<String, Object> stringMap) {
        String strEntity = new Gson().toJson(stringMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        return body;
    }

    /**
     * 退出登录
     */
    public void clearDataForLoginOut() {
//        SPUtils.getInstance().put(BaseConstants.BillConfig, "");
//        SPUtils.getInstance().put(BaseConstants.MapConfig, "");
//        SPUtils.getInstance().put(BaseConstants.VehicleConfig, "");
//        SPUtils.getInstance().put(BaseConstants.PhotoConfig, "");
//        SPUtils.getInstance().put(BaseConstants.NbLabelConfig, "");
//        SPUtils.getInstance().put(BaseConstants.RegisterConfig, "");
//        SPUtils.getInstance().put(BaseConstants.ManagerConfig, "");
//        SPUtils.getInstance().put(BaseConstants.AuditConfig, "");
//        SPUtils.getInstance().put(BaseConstants.token, "");
//
//        SPUtils.getInstance().put(BaseConstants.nb_detectNb, false);
//        SPUtils.getInstance().put(BaseConstants.nb_isBindRFlabel,false);
//        SPUtils.getInstance().remove(BaseConstants.nb_ble_mac);
//        SPUtils.getInstance().remove(BaseConstants.Login_user_name);
        String cityCode = SPUtils.getInstance().getString(BaseConstants.City_cityCode);
        String cityNameStr = SPUtils.getInstance().getString(BaseConstants.City_name, "");
        int systemId = SPUtils.getInstance().getInt(BaseConstants.City_systemID, -100);
        SPUtils.getInstance().clear();

        SPUtils.getInstance().put(BaseConstants.City_cityCode,cityCode);
        SPUtils.getInstance().put(BaseConstants.City_name, cityNameStr);
       SPUtils.getInstance().put(BaseConstants.City_systemID, systemId);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivities) {
            mActivities.remove(this);
        }
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }

    }

    /*按返回退出*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(isKillDialogShow){
                if (isShowBackDialog) {
                    showCustomWindowDialog();
                } else {
                    showCustomWindowDialog("确定退出当前APP");
                }
            }else {
                finish();
            }


            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void showSubmitRequestDialog(Map<String, Object> stringMap) {
        submitBody = getRequestBody(stringMap);
        showSubmitRequestDialog();

    }

    public void setSubmitBody(Map<String, Object> stringMap) {
        submitBody = getRequestBody(stringMap);
    }

    public void showSubmitRequestDialog() {
        showSubmitRequestDialog("确定提交数据");

    }

    public void showSubmitRequestDialog(String msg) {
        try{

            if(submitRequestDialog.isDialogShowing()){
                return;
            }
            submitRequestDialog.showCustomWindowDialog("温馨提示", msg, false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showCustomWindowDialog() {
        showCustomWindowDialog("温馨提示", "确定退出页面？", false);
    }

    public void showCustomWindowDialog(String content) {
        showCustomWindowDialog("温馨提示", content, false);
    }

    public void showCustomWindowDialog(String content, boolean isHidden) {
        showCustomWindowDialog("温馨提示", content, isHidden);
    }

    public void showCustomWindowDialog(String title, String content) {
        showCustomWindowDialog(title, content, false);
    }

    public void showCustomWindowDialog(String title, String content, boolean isHideCancel) {
        showCustomWindowDialog(title, content, isHideCancel, false);
    }

    public void showCustomWindowDialog(String title, String content, boolean isHideCancel, boolean isHideAffirm) {
        try{

            if(customBaseDialog.isDialogShowing()){
                return;
            }
            customBaseDialog.showCustomWindowDialog(title, content, isHideCancel, isHideAffirm);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void goToActivity(Class Activity, Bundle bundle) {
        Intent intent = new Intent(this, Activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    public void goToActivity(Class Activity) {
        goToActivity(Activity, null);
    }

    public void titleBackClickListener(RelativeLayout tvBack) {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isKillDialogShow){
                    showCustomWindowDialog();
                }else {
                    finish();
                }

            }
        });

    }

    public void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }


    /**
     * 子类可以直接用
     *
     * @param title
     */
    protected void setToolBar(Toolbar toolbar, String title) {
        setToolBar(toolbar, title, true);
    }

    /**
     * 子类可以直接用
     *
     * @param title
     */
    protected void setToolBar(Toolbar toolbar, String title, boolean enable) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);//1.显示toolbar的返回按钮左上角图标
        getSupportActionBar().setDisplayShowHomeEnabled(enable);//2.显示toolbar的返回按钮12要一起用
        getSupportActionBar().setDisplayShowTitleEnabled(enable);//显示toolbar的标题
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private CompositeSubscription mCompositeSubscription;

    //用于添加rx的监听的在onDestroy中记得关闭不然会内存泄漏。
    @Override
    public void bindSubscription(Subscription subscription) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }



    public void killAll() {
        // 复制了一份mActivities 集合Å
        List<AppCompatActivity> copy;
        synchronized (mActivities) {
            copy = new LinkedList<>(mActivities);
        }
        for (AppCompatActivity activity : copy) {
            activity.finish();
        }
//        // 杀死当前的进程
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //下面的用于侧滑关闭Activity
    public void touchFinish() {
        super.finish();
        overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
    }

    /**
     * 关闭activity时执行这个动画
     *
     * @param deltaX
     */
    public void closeAnimator(int deltaX) {
        if (isClose) {
            ValueAnimator animator = ValueAnimator.ofInt(deltaX, decorView.getWidth());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
//                decorView.layout(value, 0, value + decorView.getWidth(), decorView.getHeight());
                    decorView.scrollTo(-value, 0);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animator arg0) {

                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                    if (isClose) {
                        touchFinish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator arg0) {

                }
            });
            animator.setDuration(300);
            animator.start();
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(deltaX, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
//                decorView.layout(value, 0, value + decorView.getWidth(), decorView.getHeight());
                    decorView.scrollTo(-value, 0);
                }
            });
            animator.setDuration(300);
            animator.start();
        }
    }


    // 根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    //    需要测滑关闭时在打开这个注释
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );
            }
        }


        return super.dispatchTouchEvent(ev);
    }


    /**
     * 颜色变化过度
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24 |
                (startR + (int) (fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int) (fraction * (endB - startB)));
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
