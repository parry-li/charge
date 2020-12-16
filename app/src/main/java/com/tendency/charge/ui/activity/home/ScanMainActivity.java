package com.tendency.charge.ui.activity.home;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.parry.utils.code.SPUtils;
import com.tendency.charge.R;
import com.tendency.charge.adapter.FragmentPageAdapter;
import com.tendency.charge.constants.BaseConstants;
import com.tendency.charge.ui.activity.base.NoLoadingBaseActivity;
import com.tendency.charge.ui.activity.scan.QRCodeScanFragment;
import com.tendency.charge.ui.activity.scan.ScanBleFragment;
import com.tendency.charge.utils.CRC16MP;
import com.tendency.charge.utils.LogUtil;
import com.tendency.charge.utils.ToastUtil;
import com.tendency.charge.utils.UIUtils;
import com.tendency.charge.view.CustomWindowDialog;
import com.tendency.charge.view.NoScrollViewPager;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

public class ScanMainActivity extends NoLoadingBaseActivity {
    @BindView(R.id.register_vp)
    NoScrollViewPager registerVp;

    private BluetoothGattCharacteristic blueCharacteristics;
    public BleDevice connectBleDevice;
    private boolean isDestroy = false;//页面销毁

    private ScanBleFragment scanBleFragment;
    private QRCodeScanFragment qrCodeScanFragment;
    private CustomWindowDialog bleWindowDialog;
    private String labelLastContent;


    @Override
    protected void initTitle() {
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        initVp();
        initDialog();
    }


    private void initVp() {
        List<Fragment> mFragmentList = new ArrayList<>();
        qrCodeScanFragment = new QRCodeScanFragment();
        scanBleFragment = new ScanBleFragment();
        mFragmentList.add(qrCodeScanFragment);
        mFragmentList.add(scanBleFragment);
        registerVp.setNoScroll(true);
        registerVp.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(), mFragmentList));
        setVpCurrentItem(0);
    }


    public void setVpCurrentItem(int page) {
        registerVp.setCurrentItem(page);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_main;
    }

    @Override
    protected void submitRequestData() {

    }


    /*======================================下发数据==========================================*/
    public ExecutorService singleThreadPool = Executors.newFixedThreadPool(1);
    public void scanWrite(String labelContent){
        doWriteDataToBle(labelContent);
        handlerTime =10;
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(1,1500);
    }

    public  void  doWriteDataToBle(final String labelContent) {
        labelLastContent = labelContent;
//        singleThreadPool.execute(new Runnable() {
//            @Override
//            public void run() {
                writeToBle(writeDataToBleC3(labelContent));
//            }
//        });

    }


    public  byte[] frameFlagStatic = {(byte) 0xAA};


    public  byte[] writeDataToBleC3(String label) {
        String contentFor81 = "810201000000";
        String contentForType = "00" + label;
        byte[] firstSbuf = CRC16MP.getSendBuf(contentForType+contentFor81);
        String firstCrcStr = CRC16MP.getBufHexStr(firstSbuf);
        firstCrcStr = firstCrcStr.substring(firstCrcStr.length() - 4);
        String all = "C3AA" + contentForType + contentFor81+firstCrcStr;
        String allData = UIUtils.autoGenericCodeBehind(all, 34);
        byte[] allsbuf = CRC16MP.getSendBuf(allData);
        byte[] childContent = null;
        childContent = UIUtils.ByteArrayCopy(childContent, frameFlagStatic);
        childContent = UIUtils.ByteArrayCopy(childContent, allsbuf);

        return childContent;
    }


    private void writeToBle(byte[] bytes) {
        if (blueCharacteristics != null) {
            BleManager.getInstance().write(
                    connectBleDevice,
                    blueCharacteristics.getService().getUuid().toString(),
                    blueCharacteristics.getUuid().toString(),
                    bytes,
                    bleWriteCallback);
        }

    }

    private BleWriteCallback bleWriteCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    byte[] aa = justWrite;
                    LogUtil.d("write success, current: " + current
                            + " total: " + total
                            + " justWrite: " + HexUtil.formatHexString(justWrite, false));
                }
            });
        }

        @Override
        public void onWriteFailure(final BleException exception) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.d("onWriteFailure" + exception.toString());
                    if (exception.getCode() == 102) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (exception.getDescription().equals("This device not connect!")) {
                        }
                    }
                }
            });
        }
    };


    /*======================================连接蓝牙======================================*/
    public boolean isBleConnectSuccess = false;//蓝牙是否连接成功

    public boolean getBleConnectStatus() {
        return isBleConnectSuccess;
    }


    public void doConnectBleByMac() {
        String bleMac = SPUtils.getInstance().getString(BaseConstants.nb_ble_mac);
        if (!TextUtils.isEmpty(bleMac)) {
            if (connectNum > 0) {
                BleManager.getInstance().connect(bleMac, connectCallback);
            }

        } else {
            if (!bleWindowDialog.isDialogShowing()) {
                showDialog("备案登记NB标签报装，为提高效率请先连接设备");
            }

        }

    }

    private void initDialog() {

        bleWindowDialog = new CustomWindowDialog(this);
        bleWindowDialog.setOnCustomDialogClickListener(new CustomWindowDialog.OnItemClickListener() {
            @Override
            public void onCustomDialogClickListener() {
                setVpCurrentItem(1);
            }
        });
    }

    private void showDialog(String msg) {
        bleWindowDialog.showCustomWindowDialog("温馨提示",
                msg, false);
    }

    /**
     * 连接设备
     */

    private int connectNum = 5;//连接设备次数

    private boolean isConnecting = false;//是否正在连接中

    public void doConnectBle() {
        zProgressHUD.show();
        BleManager.getInstance().connect(connectBleDevice, connectCallback);
    }

    private BleGattCallback connectCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            LogUtil.i("onStartConnect==========");
            isConnecting = true;
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            if (isDestroy) {
                return;
            }
            isConnecting = false;
            LogUtil.i("onConnectFail==========" + exception.getDescription());
            ToastUtil.showWX("设备连接失败，请重新连接");

            zProgressHUD.dismiss();
            isBleConnectSuccess = false;
            scanBleFragment.initAdapterCheckList();
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            zProgressHUD.dismiss();
            for (BluetoothGattService service : gatt.getServices()) {
                if (service.getUuid().toString().equals(BaseConstants.ServiceUUID)) {
                    blueCharacteristics = service.getCharacteristics().get(0);
                }
            }

            if (blueCharacteristics != null) {
                isBleConnectSuccess = true;
                ToastUtil.showWX("设备连接成功");

                connectBleDevice = bleDevice;
                openNot();
                connectNum = 5;
                isConnecting = false;
                SPUtils.getInstance().put(BaseConstants.nb_ble_mac, connectBleDevice.getMac());
                qrCodeScanFragment.setBleName(connectBleDevice.getName());
                if (!TextUtils.isEmpty(labelLastContent)) {
                    doWriteDataToBle(labelLastContent);
                }

            } else {
                ToastUtil.showWX("设备连接失败，请重新连接");
            }


        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
            isBleConnectSuccess = false;
            zProgressHUD.dismiss();
            if (isDestroy) {
                return;
            }
            isConnecting = false;
            qrCodeScanFragment.setBleName("连接蓝牙");

            ToastUtil.showWX("设备已断开，请重新连接");
            scanBleFragment.initAdapterCheckList();
        }
    };

    private int handlerTime =10;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(handlerTime>0){
                handlerTime--;
                doWriteDataToBle(labelLastContent);
                handler.sendEmptyMessageDelayed(1,1000);
            }else {
                handlerTime =10;
            }
            return true;
        }
    });

    private void openNot() {
        if (connectBleDevice != null && blueCharacteristics != null) {
            BleManager.getInstance().notify(
                    connectBleDevice,
                    blueCharacteristics.getService().getUuid().toString(),
                    blueCharacteristics.getUuid().toString(),
                    new BleNotifyCallback() {

                        @Override
                        public void onNotifySuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtil.e("notify success===");
                                }
                            });
                        }

                        @Override
                        public void onNotifyFailure(final BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtil.e("onNotifyFailure===" + exception.toString());
                                }
                            });
                        }

                        @Override
                        public void onCharacteristicChanged(final byte[] data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        final String hexString = HexUtil.formatHexString(blueCharacteristics.getValue(), false);
                                        LogUtil.v("read= ble data:   " + hexString);

                                        //aa81 06 870e 00000226 01 0014271a0a 5d00007545
                                        if (!TextUtils.isEmpty(hexString)) {

                                            if(hexString.length()>18){
                                                if(hexString.substring(0,4).equals("AA5")){
//                                                    ToastUtils.showLong(hexString.substring(0,4));
                                                                                                  }
                                            }
                                        } else {
                                            if (!isConnecting) {
                                                connectNum--;
                                                doConnectBleByMac();
                                            }

                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            });
                        }
                    });
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            isDestroy = true;
            BleManager.getInstance().cancelScan();
            BleManager.getInstance().destroy();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
