//package com.tendency.registration.ui.activity.scan;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.Point;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Vibrator;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.parry.utils.code.ScreenUtils;
//import com.tendency.registration.R;
//import com.tendency.registration.constants.BaseConstants;
//import com.tendency.registration.ui.activity.base.NoLoadingBaseActivity;
//import com.tendency.registration.utils.LogUtil;
//import com.tendency.registration.utils.ScanUtil;
//import com.tendency.registration.utils.ToastUtil;
//import com.tendency.registration.view.ScanDialog;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import cn.bertsir.zbar.CameraConfiguration;
//import cn.bertsir.zbar.CameraManager;
//import cn.bertsir.zbar.CameraPreview;
//import cn.bertsir.zbar.Qr.ScanResult;
//import cn.bertsir.zbar.Qr.Symbol;
//import cn.bertsir.zbar.QrConfig;
//import cn.bertsir.zbar.ScanCallback;
//import cn.bertsir.zbar.view.ScanView;
//
//
//public class ScanQRCodeActivity extends NoLoadingBaseActivity {
//    @BindView(R.id.cp_scan)
//    CameraPreview cpScan;
//    @BindView(R.id.scan_view)
//    ScanView scanView;
//    @BindView(R.id.iv_back)
//    ImageView ivBack;
//    @BindView(R.id.scan_input)
//    LinearLayout scanInput;
//    @BindView(R.id.scan_light_iv)
//    ImageView scanLightIv;
//    @BindView(R.id.scan_light)
//    LinearLayout scanLight;
//    @BindView(R.id.bottom)
//    LinearLayout bottom;
//
//
//    private String mCallBack;
//    private String scanResult;
//    private ScanDialog scanDialog;
//    private String ButtonName = "二维码";
//    private boolean isOpen = false;
//
//
//
//    private void setInputDialog() {
//        scanDialog = new ScanDialog(QRCodeScanActivity.this);
//        scanDialog.setOnCustomDialogClickListener(new ScanDialog.OnItemConfirmClickListener() {
//            @Override
//            public void onCustomDialogClickListener(String content) {
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra(BaseConstants.SCAN_RESULT, content);
//                resultIntent.putExtra("isPlateNumber", "0");
//                resultIntent.putExtra(BaseConstants.SCAN_HAND_INPUT, true);//true 手动
//                setResult(RESULT_OK, resultIntent);
//                finish();
//            }
//        });
//    }
//
//    //
//    @Override
//    protected void initTitle() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        Symbol.scanType = QrConfig.TYPE_CUSTOM;
//        Symbol.doubleEngine = true;
//        setInputDialog();
//    }
//
//    @Override
//    protected void initData(Bundle savedInstanceState) {
//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null) {
//            ButtonName = bundle.getString("ButtonName");
//
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (cpScan != null) {
//            cpScan.setScanCallback(resultCallback);
//            cpScan.start();
//
//            try {
//                //CameraPreview设置宽高
//                CameraManager cameraManager = cpScan.getmCameraManager();
//                Camera camera = cameraManager.getmCamera();
//                Camera.Parameters parameters = camera.getParameters();
//                Point screenResolutionForCamera = new Point();
//                screenResolutionForCamera.x = ScreenUtils.getScreenHeight();
//                screenResolutionForCamera.y = ScreenUtils.getScreenWidth();
//                Point bestPreviewSizeValue = CameraConfiguration.findBestPreviewSizeValue(parameters, screenResolutionForCamera);
//                ViewGroup.LayoutParams layoutParams = cpScan.getLayoutParams();
//                layoutParams.width = ScreenUtils.getScreenWidth();
//                layoutParams.height = ScreenUtils.getScreenWidth() * bestPreviewSizeValue.x / bestPreviewSizeValue.y;
////                LogUtils.log("sss" + layoutParams.width + "," + layoutParams.height);
//                cpScan.setLayoutParams(layoutParams);
//            } catch (Exception e) {
//
//            }
//
//        }
//    }
//
//
//    private ScanCallback resultCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(ScanResult result) {
//            scanResult(result.getContent());
//        }
//
//    };
//
//
//    private void scanResult(String result) {
//        vibrate();
//        LogUtil.i("CaptureUtils_result:" + result);
//        result = ScanUtil.decode(ButtonName, result);
//        Intent intent = new Intent();
//        intent.putExtra(BaseConstants.SCAN_RESULT, result);
//        intent.putExtra("isPlateNumber", "1");
//        intent.putExtra(BaseConstants.SCAN_HAND_INPUT, false);
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (cpScan != null) {
//            cpScan.stop();
//        }
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_scan_qrcode;
//    }
//
//    @Override
//    protected void submitRequestData() {
//
//    }
//
//    @Override
//    protected void onStop() {
//        if (cpScan != null) {
//            cpScan.setFlash(false);
//            cpScan.stop();
//        }
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//        if (cpScan != null) {
//            cpScan.setFlash(false);
//            cpScan.stop();
//        }
//
////        PictureFileUtils.deleteCacheDirFile(this);
//
//    }
//
//    private void vibrate() {
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(200);
//    }
//
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    scanResult(scanResult);
//                    break;
//                case 2:
//                    ToastUtil.showWX("识别失败！");
//                    finish();
//                    break;
//            }
//
//
//        }
//    };
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
////        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
////            String imagePath = PictureSelector.obtainMultipleResult(data).get(0).getPath();
////
////            if (TextUtils.isEmpty(imagePath)) {
////                ToastUtil.showWX("识别失败！");
////                return;
////            }
////
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        //优先使用zbar识别一次二维码
////                        String qrContent = QRUtils.getInstance().decodeQRcode(imagePath);
////                        if (!TextUtils.isEmpty(qrContent)) {
////                            scanResult = qrContent;
////                            handler.sendEmptyMessage(1);
////                        } else {
////                            //尝试用zxing再试一次识别二维码
////                            String qrCode = QRUtils.getInstance().decodeQRcodeByZxing(imagePath);
////                            if (!TextUtils.isEmpty(qrCode)) {
////                                scanResult = qrCode;
////                                handler.sendEmptyMessage(1);
////                            } else {
////                                //再试试是不是条形码
////                                try {
////                                    String barCode = QRUtils.getInstance().decodeBarcode(imagePath);
////                                    if (!TextUtils.isEmpty(barCode)) {
////                                        scanResult = barCode;
////                                        handler.sendEmptyMessage(1);
////                                    } else {
////                                        handler.sendEmptyMessage(2);
////                                    }
////                                } catch (Exception e) {
////                                    handler.sendEmptyMessage(2);
////                                    e.printStackTrace();
////                                }
////                            }
////
////                        }
////                    } catch (Exception e) {
////                        handler.sendEmptyMessage(2);
////                    }
////                }
////            }).start();
////        }
//    }
//
//    @OnClick({R.id.iv_back, R.id.scan_input, R.id.scan_light})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.scan_input:
//                dialogShow();
//                break;
//            case R.id.scan_light:
//                if (!isOpen) {
//                    isOpen = true;
//                } else {
//                    isOpen = false;
//                }
//                cpScan.setFlash(isOpen);
//                scanLightIv.setImageResource(isOpen ? R.mipmap.light_open : R.mipmap.light_close);
//                break;
//        }
//    }
//
//    private void dialogShow() {
//        if (scanDialog == null || scanDialog.isShowing()) {
//            return;
//        }
//        scanDialog.showCustomWindowDialog(ButtonName);
//    }
//}
