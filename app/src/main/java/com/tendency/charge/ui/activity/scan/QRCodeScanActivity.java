package com.tendency.charge.ui.activity.scan;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parry.utils.code.ScreenUtils;
import com.tendency.charge.R;
import com.tendency.charge.constants.BaseConstants;
import com.tendency.charge.ui.activity.base.NoLoadingBaseActivity;
import com.tendency.charge.utils.Base64;
import com.tendency.charge.utils.LogUtil;
import com.tendency.charge.utils.ToastUtil;
import com.tendency.charge.utils.UIUtils;
import com.tendency.charge.view.ScanDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.CameraConfiguration;
import cn.bertsir.zbar.CameraManager;
import cn.bertsir.zbar.CameraPreview;
import cn.bertsir.zbar.Qr.ScanResult;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.ScanCallback;
import cn.bertsir.zbar.view.ScanView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 二维码扫描
 */

public class QRCodeScanActivity extends NoLoadingBaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.cp_scan)
    CameraPreview cpScan;
    @BindView(R.id.scan_view)
    ScanView scanView;
    @BindView(R.id.scan_input)
    LinearLayout scanInput;
    @BindView(R.id.scan_light_iv)
    ImageView scanLightIv;
    @BindView(R.id.scan_light)
    LinearLayout scanLight;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.label)
    TextView label;


    private String mCallBack;
    private String scanResult;
    private ScanDialog scanDialog;
    private String ButtonName = "二维码";
    private boolean isOpen = false;
    private static final int PERMISSION_CODE = 124;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(PERMISSION_CODE)
    public void getPermission() {
        if (EasyPermissions.hasPermissions(this, BaseConstants.PERMISSION_CONTENT)) {
            LogUtil.i("hasPermissions");

        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location_contacts),
                    PERMISSION_CODE,
                    BaseConstants.PERMISSION_CONTENT);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogUtil.i("onPermissionsGranted");
//        updateManager.check(false);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        LogUtil.i("onPermissionsDenied");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setNegativeButton("取消")
                    .setRationale(R.string.permisson_dialog_content)
                    .setPositiveButton("确定")
                    .build().show();

        }
    }

    private void setInputDialog() {
        scanDialog = new ScanDialog(QRCodeScanActivity.this);
        scanDialog.setOnCustomDialogClickListener(new ScanDialog.OnItemConfirmClickListener() {
            @Override
            public void onCustomDialogClickListener(String content) {
                scanResult(content);
            }
        });
    }

    //
    @Override
    protected void initTitle() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Symbol.scanType = QrConfig.TYPE_CUSTOM;
        Symbol.doubleEngine = true;
        getPermission();
        setInputDialog();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            ButtonName = bundle.getString("ButtonName");

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (cpScan != null) {
            cpScan.setScanCallback(resultCallback);
            cpScan.start();

            try {
                //CameraPreview设置宽高
                CameraManager cameraManager = cpScan.getmCameraManager();
                Camera camera = cameraManager.getmCamera();
                Camera.Parameters parameters = camera.getParameters();
                Point screenResolutionForCamera = new Point();
                screenResolutionForCamera.x = ScreenUtils.getScreenHeight();
                screenResolutionForCamera.y = ScreenUtils.getScreenWidth();
                Point bestPreviewSizeValue = CameraConfiguration.findBestPreviewSizeValue(parameters, screenResolutionForCamera);
                ViewGroup.LayoutParams layoutParams = cpScan.getLayoutParams();
                layoutParams.width = ScreenUtils.getScreenWidth();
                layoutParams.height = ScreenUtils.getScreenWidth() * bestPreviewSizeValue.x / bestPreviewSizeValue.y;
//                LogUtils.log("sss" + layoutParams.width + "," + layoutParams.height);
                cpScan.setLayoutParams(layoutParams);
            } catch (Exception e) {

            }

        }
    }


    private ScanCallback resultCallback = new ScanCallback() {
        @Override
        public void onScanResult(ScanResult result) {
            scanResult(result.getContent());
        }

    };


    private void scanResult(String result) {
        vibrate();
        LogUtil.i("CaptureUtils_result:" + result);
        result = decodeResult(result);

    }

    private String decodeResult(String result) {
        try {
            if (result.toUpperCase().contains("?C1")) {

                int i = result.toUpperCase().indexOf("?C1");
                if (i > 0) {//数据加密过
                    String re = result.substring(i + 3);

                    String reType = re.substring(0, 4);
                    String contentFor16 = re.substring(4,12);
                    Integer content10 = Integer.parseInt(contentFor16, 16);//转10进制
                    String reContent = UIUtils.autoGenericCode(content10 + "", 10);
                    result = reType + contentFor16;
                    label.setText(reType+reContent);
                }
            } else if (result.toUpperCase().contains("?ID")) {
                /*标签*/
                /*http://ga.iotone.cn/?IDgCIAAAfR*/
                int i = result.toUpperCase().indexOf("?ID");
                if (i > 0) {//数据加密过
                    String re = result.substring(i + 3);
                    String baseContent = Base64.base64Decode16(re);//解析数据
                    String reType = baseContent.substring(0, 4);
                    String contentFor16 = baseContent.substring(4);
                    Integer content10 = Integer.parseInt(contentFor16, 16);//转10进制
                    String reContent = UIUtils.autoGenericCode(content10 + "", 10);
                    result = reType + contentFor16;
                    label.setText(reType+reContent);
                }

            } else {
                label.setText(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (cpScan != null) {
            cpScan.stop();
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_qrcode;
    }

    @Override
    protected void submitRequestData() {

    }

    @Override
    protected void onStop() {
        if (cpScan != null) {
            cpScan.setFlash(false);
            cpScan.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (cpScan != null) {
            cpScan.setFlash(false);
            cpScan.stop();
        }

//        PictureFileUtils.deleteCacheDirFile(this);

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        if (cpScan != null) {
            cpScan.setFlash(false);
            cpScan.stop();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    scanResult(scanResult);
                    break;
                case 2:
                    ToastUtil.showWX("识别失败！");
                    finish();
                    break;
            }


        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick({R.id.scan_input, R.id.scan_light})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scan_input:
                dialogShow();
                break;
            case R.id.scan_light:
                if (!isOpen) {
                    isOpen = true;
                } else {
                    isOpen = false;
                }
                cpScan.setFlash(isOpen);
                scanLightIv.setImageResource(isOpen ? R.mipmap.light_open : R.mipmap.light_close);
                break;
        }
    }

    private void dialogShow() {
        if (scanDialog == null || scanDialog.isShowing()) {
            return;
        }
        scanDialog.showCustomWindowDialog(ButtonName);
    }

}