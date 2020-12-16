//package com.tendency.registration.ui.activity.scan;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import com.tendency.registration.R;
//import com.tendency.registration.ui.activity.base.NoLoadingBaseActivity;
//import com.tendency.registration.utils.LogUtil;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.parry.qrcode.zbar.CaptureFragment;
//import cn.parry.qrcode.zbar.CaptureUtils;
//
//
//public class CaptureActivity extends NoLoadingBaseActivity {
//
//
//    @BindView(R.id.fl_my_container)
//    FrameLayout flMyContainer;
//    @BindView(R.id.capture_crop_ts)
//    TextView captureCropTs;
//    @BindView(R.id.btn_shoudong)
//    TextView btnShoudong;
//    @BindView(R.id.capture_light)
//    ImageView captureLight;
//    @BindView(R.id.capture_containter)
//    RelativeLayout captureContainter;
//    @BindView(R.id.img_back)
//    ImageView imgBack;
//    @BindView(R.id.txt_title)
//    TextView txtTitle;
//    @BindView(R.id.txt_ok)
//    TextView txtOk;
//    @BindView(R.id.rl_title)
//    RelativeLayout rlTitle;
//    @BindView(R.id.et_code)
//    EditText etCode;
//    @BindView(R.id.ll_input)
//    LinearLayout llInput;
//    @BindView(R.id.activity_second)
//    RelativeLayout activitySecond;
//    @BindView(R.id.capture_back)
//    ImageView captureBack;
//    private CaptureFragment captureFragment;
//    private String dialogTitle;
//    private Activity mActivity;
//    private String deviceCode;
//
//    @Override
//    protected void initTitle() {
//        isAnalyzeSuccess =false;
//        String titleContent = getIntent().getStringExtra("titleContent");
//        if (titleContent != null) {
//            dialogTitle = titleContent;
//        } else {
//            dialogTitle = "设备编号";
//        }
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        captureFragment = new CaptureFragment();
//        // 为二维码扫描界面设置定制化界面
//        CaptureUtils.setFragmentArgs(captureFragment, R.layout.camera_zxing);
//        captureFragment.setAnalyzeCallback(analyzeCallback);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
//
//        initData();
//        initLight();
//    }
//
//
////    @Override
////    protected void initView(Bundle savedInstanceState) {
////
////    }
//
//    private boolean showInputButton = true;
//
//    private void initData() {
////        mActivity = CaptureActivity.this;
////        showInputButton = getIntent().getBooleanExtra("showInputButton", true);
////        final CaptureDialog captureDialog = new CaptureDialog(this);
////        captureDialog.setOnCustomDialogClickListener(new CaptureDialog.OnItemClickListener() {
////            @Override
////            public void onCustomDialogClickListener(String number) {
////                Intent intent = new Intent();
////                intent.putExtra("DeviceCode", number);
////                intent.putExtra("result", number);
////                intent.putExtra("inputtype", "shoudong");
////                setResult(RESULT_OK, intent);
////                ActivityUtil.FinishActivity(mActivity);
////            }
////        });
////
////        captureCropTs.setText("将二维码图片对准扫描框即可自动扫描\n若环境较暗请打开手电筒加快扫码速度");
////        btnShoudong.setText("输入" + dialogTitle);
////        btnShoudong.setVisibility(this.showInputButton ? View.VISIBLE : View.GONE);
////        btnShoudong.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                Show();
////                captureDialog.showCaptureDialog(dialogTitle);
////            }
////        });
////
////        imgBack.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                llInput.setVisibility(View.GONE);
////                captureContainter.setVisibility(View.VISIBLE);
////            }
////        });
////        captureBack.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////               finish();
////            }
////        });
////
////
////        etCode = (EditText) findViewById(R.id.et_code);
////        etCode.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
////
////        TextView txt_ok = (TextView) findViewById(R.id.txt_ok);
////        txt_ok.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                deviceCode = etCode.getText().toString();
////                if (!deviceCode.equals("")) {
////
////                    DialogUtil.Show(mActivity, "您输入的设备编码是\n\n" + deviceCode, new DoOk() {
////                        @Override
////                        public void goTodo() {
////                            Intent intent = new Intent();
////                            intent.putExtra("DeviceCode", deviceCode);
////                            intent.putExtra("result", deviceCode);
////                            intent.putExtra("inputtype", "shoudong");
////                            setResult(RESULT_OK, intent);
////                            ActivityUtil.FinishActivity(mActivity);
////                        }
////                    });
////
////
////                } else {
////                    ToastUtil.ErrorOrRight(mActivity, "设备编号不能为空", 1);
////
////                }
////
////            }
////        });
//
//    }
//
//    public static boolean isOpen = false;
//
//    private void initLight() {
//        captureLight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isOpen) {
//                    captureFragment.openFlashlight();
//                    isOpen = true;
//                } else {
//                    captureFragment.closeFlashlight();
//                    isOpen = false;
//                }
//
//            }
//        });
//    }
//
//    private  boolean isAnalyzeSuccess =false;
//
//    /**
//     * 二维码解析回调函数
//     */
//    CaptureUtils.AnalyzeCallback analyzeCallback = new CaptureUtils.AnalyzeCallback() {
//
//
//        @Override
//        public void onAnalyzeSuccess(String result) {
//            if(!isAnalyzeSuccess){
//                isAnalyzeSuccess =true;
//                LogUtil.i("二维码解析回调函数");
////                Intent intent = new Intent();
////                intent.putExtra("result", result);
////                intent.putExtra("inputtype", "zbar");
////                setResult(RESULT_OK, intent);
////                ActivityUtil.FinishActivity(mActivity);
//            }
//
//        }
//
//        @Override
//        public void onAnalyzeFailed() {
//            Intent intent = new Intent();
//            intent.putExtra("result", "");
//            intent.putExtra("inputtype", "zbar");
//            setResult(RESULT_OK, intent);
////            ActivityUtil.FinishActivity(mActivity);
//        }
//    };
//
//    public static void goSimpleCaptureActivity(Activity context, int requestCode) {
//        Intent intent = new Intent(context, CaptureActivity.class);
//        intent.putExtra("showInputButton", true);
//        context.startActivityForResult(intent, requestCode);
//
//    }
//
//    public static void goSimpleCaptureActivity(Activity context, int requestCode, String titleContent) {
//        Intent intent = new Intent(context, CaptureActivity.class);
//        intent.putExtra("showInputButton", true);
//        intent.putExtra("titleContent", titleContent);
//        context.startActivityForResult(intent, requestCode);
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//         return R.layout.activity_zxing;
//    }
//
//    @Override
//    protected void submitRequestData() {
//
//    }
//
//
//}