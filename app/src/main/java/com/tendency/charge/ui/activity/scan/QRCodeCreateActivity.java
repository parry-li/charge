//package com.tdr.registration.ui.activity.scan;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.parry.zxing.activity.CodeUtils;
//import com.tdr.registration.R;
//import com.tdr.registration.ui.activity.base.NoLoadingBaseActivity;
//import com.tdr.registration.ui.activity.home.HomeActivity;
//import com.tdr.registration.utils.ActivityUtil;
//import com.tdr.registration.view.ZProgressHUD;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//import butterknife.BindView;
//
//
///**
// * 二维码生成界面
// */
////@ContentView(R.layout.activity_qrcode_create)
//public class QRCodeCreateActivity extends NoLoadingBaseActivity {
//
//
//    @BindView(R.id.com_title_back)
//    RelativeLayout comTitleBack;
//    @BindView(R.id.text_title)
//    TextView textTitle;
//    @BindView(R.id.com_title_setting_iv)
//    ImageView comTitleSettingIv;
//    @BindView(R.id.com_title_setting_tv)
//    TextView comTitleSettingTv;
//    @BindView(R.id.com_title_main_relativeLayout)
//    RelativeLayout comTitleMainRelativeLayout;
//    @BindView(R.id.tv_code_num)
//    TextView tvCodeNum;
//    @BindView(R.id.IV_QRCode)
//    ImageView IVQRCode;
//    @BindView(R.id.btn_submit)
//    Button btnSubmit;
//    private boolean IsCanBack = false;
//
//    private Activity mActivity;
//    private ZProgressHUD mProgressHUD;
//    private Gson mGson;
//    private List<DX_PreRegistrationModel> PRList;
//    private String QRCodeID;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_qrcode_create;
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initview();
//        initdate();
//    }
//
//    @Override
//    protected void initTitle() {
//
//
//        titleBackClickListener(comTitleBack);
//        textTitle.setText("预登记二维码");
//        comTitleSettingTv.setText("登记");
//        comTitleSettingTv.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//
//    }
//
//    private void initview() {
//        mActivity = this;
//        mGson = new Gson();
//        mProgressHUD = new ZProgressHUD(mActivity);
//        mProgressHUD.setMessage("");
//        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
//
//
//
//        comTitleSettingTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query();
//            }
//        });
//
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clearData();
//                ActivityUtil.goActivityAndFinish(QRCodeCreateActivity.this, HomeActivity.class);
//            }
//        });
//    }
//
//    private void initdate() {
//        Bundle bundle = getIntent().getExtras();
//        IsCanBack = bundle.getBoolean("IsCanBack", false);
//        QRCodeID = bundle.getString("QRCodeID");
//        String QRCodeID_NEW = bundle.getString("QRCodeID_NEW");
//        String UserType = ((String) SharedPreferencesUtils.get("UserType", ""));
//        mLog.e("UserType=" + UserType);
//
//        if (!TextUtils.isEmpty(QRCodeID_NEW)) {
//            tv_code_num.setText("" + QRCodeID_NEW);
//        }
//        if (IsCanBack) {
//            TV_Register.setVisibility(View.VISIBLE);
//        } else {
//            TV_Register.setVisibility(View.GONE);
//        }
//        if (!UserType.equals("1")) {
//            mLog.e("-------------UserType");
//            TV_Register.setVisibility(View.GONE);
//        }
//        mLog.e("QRCodeID=" + QRCodeID);
////        CreateQRCode createqrcode = new CreateQRCode();
////        createqrcode.execute(QRCodeID);
//
//
//        try {
//            Bitmap mBitmap = CodeUtils.createImage(QRCodeID, 400, 400, null);
//            IV_QRCode.setImageBitmap(mBitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    private class CreateQRCode extends AsyncTask<String, Void, Bitmap> {
////        @Override
////        protected Bitmap doInBackground(String... params) {
////            return QRCodeEncoder.syncEncodeQRCode(params[0], BGAQRCodeUtil.dp2px(QRCodeCreateActivity.this, 150));
////        }
////
////        @Override
////        protected void onPostExecute(Bitmap bitmap) {
////            if (bitmap != null) {
////                IV_QRCode.setImageBitmap(bitmap);
////            } else {
////                Utils.showToast("生成二维码失败");
////            }
////        }
////    }
//
//    @Override
//    public void onBackPressed() {
//        if (IsCanBack) {
//            finish();
//        } else {
//            clearData();
//            ActivityUtil.goActivityAndFinish(QRCodeCreateActivity.this, HomeActivity.class);
//        }
//    }
//
//    /**
//     * 获取电信预登记
//     */
//    private void query() {
//        mProgressHUD.show();
//        HashMap<String, String> map = new HashMap<>();
//        map.post("accessToken", (String) SharedPreferencesUtils.get("token", ""));
//        map.post("plateNumber", "");
//        map.post("cardid", "");
//        map.post("phone", "");
//        map.post("registerId", QRCodeID.substring(7));
//        mLog.e("Pan", "map=" + map);
//        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPREREGISTERLIST, map, new WebServiceUtils.WebServiceCallBack() {
//            @Override
//            public void callBack(String result) {
//                Utils.LOGE("Pan", result);
//                if (result != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        int errorCode = jsonObject.getInt("ErrorCode");
//                        String data = jsonObject.getString("Data");
//                        if (errorCode == 0) {
//                            mProgressHUD.dismiss();
//                            try {
//                                PRList = mGson.fromJson(data, new TypeToken<List<DX_PreRegistrationModel>>() {
//                                }.getType());
//                                if (PRList.get(0) != null) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("InType", "PreRegistration");
////                                    bundle.putSerializable("PreRegistrationModel", PRList.get(0));
//                                    TransferUtil.save("PreRegistrationModel", PRList.get(0));
//                                    ActivityUtil.goActivityWithBundle(QRCodeCreateActivity.this, RegisterCarActivity.class, bundle);
//                                }
//                            } catch (JsonSyntaxException e) {
//                                Utils.showToast(data);
//                            }
//                        } else {
//                            mProgressHUD.dismiss();
//                            Utils.showToast(data);
//                        }
//                    } catch (JSONException e) {
//                        mProgressHUD.dismiss();
//                        e.printStackTrace();
//                        Utils.showToast("JSON解析出错");
//                    }
//                } else {
//                    mProgressHUD.dismiss();
//                    Utils.showToast("获取数据超时，请检查网络连接");
//                }
//            }
//        });
//
//    }
//
//
//}
//
