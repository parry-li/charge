package com.tendency.charge.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.parry.utils.code.SPUtils;
import com.tendency.charge.constants.BaseConstants;
import com.tendency.charge.utils.UIUtils;
import com.tendency.charge.view.CustomWindowDialog;
import com.tendency.charge.view.ZProgressHUD;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public abstract class BaseFragment extends Fragment {
    public ZProgressHUD zProgressHUD;
    private CustomWindowDialog customBaseDialog;
    private CustomWindowDialog submitRequestDialog;
    public int systemBaseID;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemBaseID =  SPUtils.getInstance().getInt(BaseConstants.City_systemID);
        zProgressHUD = new ZProgressHUD(this.getActivity());
        zProgressHUD.setMessage("加载中");
        customBaseDialog = new CustomWindowDialog(this.getActivity());
        customBaseDialog.setOnCustomDialogClickListener(new CustomWindowDialog.OnItemClickListener() {
            @Override
            public void onCustomDialogClickListener() {
//                finish();
            }
        });
//        /*请求结果的提示*/
//        customBaseDialog = new CustomWindowDialog(this.getActivity());
//        customBaseDialog.setOnCustomDialogClickListener(new CustomWindowDialog.OnItemClickListener() {
//            @Override
//            public void onCustomDialogClickListener() {
//
//            }
//        });
        /*提交数据的提示*/
        submitRequestDialog = new CustomWindowDialog(this.getActivity());
        submitRequestDialog.setOnCustomDialogClickListener(new CustomWindowDialog.OnItemClickListener() {
            @Override
            public void onCustomDialogClickListener() {
                if(UIUtils.isFastClick()){
                    submitRequestData();
                }

            }
        });


    }

    protected abstract void submitRequestData();

    public RequestBody getRequestBody(Map<String, Object> stringMap) {
        String strEntity = new Gson().toJson(stringMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        return body;
    }

    private RequestBody submitBody;
    public RequestBody getSubmitBoby() {
        return submitBody;
    }
    public void showSubmitRequestDialog(Map<String, Object> stringMap) {
        submitBody = getRequestBody(stringMap);
        showSubmitRequestDialog();

    }

    public void showSubmitRequestDialog() {
        submitRequestDialog. showCustomWindowDialog("温馨提示", "确定提交数据", false);
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

        ((BaseActivity) this.getActivity()).showCustomWindowDialog(title, content, isHideCancel, isHideAffirm);
    }
}
