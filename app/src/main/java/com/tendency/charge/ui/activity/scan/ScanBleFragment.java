package com.tendency.charge.ui.activity.scan;


import android.bluetooth.BluetoothAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.tendency.charge.R;
import com.tendency.charge.adapter.InspectBleAdapter;
import com.tendency.charge.bean.CheckBean;
import com.tendency.charge.service.BasePresenter;
import com.tendency.charge.ui.activity.base.NoLoadingBaseFragment;
import com.tendency.charge.ui.activity.home.ScanMainActivity;
import com.tendency.charge.utils.LogUtil;
import com.tendency.charge.utils.ToastUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ScanBleFragment extends NoLoadingBaseFragment {

    @BindView(R.id.com_title_back)
    RelativeLayout comTitleBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.com_title_setting_iv)
    ImageView comTitleSettingIv;
    @BindView(R.id.com_title_setting_tv)
    TextView comTitleSettingTv;
    @BindView(R.id.ble_update)
    TextView bleUpdate;
    @BindView(R.id.ble_update_iv)
    ImageView bleUpdateIv;
    @BindView(R.id.button_next)
    TextView buttonNext;
    @BindView(R.id.ble_rv)
    RecyclerView bleRv;


    private ScanMainActivity scanMainActivity;
    private InspectBleAdapter inspectBleAdapter;

    @Override
    protected BasePresenter setPresenter() {
        return null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_inspect_ble;
    }

    @Override
    protected void initView() {
        scanMainActivity = ((ScanMainActivity) ScanBleFragment.this.getActivity());
        initRv();
        setScanRule();
        initTitle();
    }

    private void initTitle() {
        textTitle.setText("请选择设备");
        comTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanMainActivity.setVpCurrentItem(0);
            }
        });
    }

    private List<BleDevice> bleList = new ArrayList<>();

    private void initRv() {
        List<BleDevice> bleDeviceList = new ArrayList<>();
        inspectBleAdapter = new InspectBleAdapter(bleDeviceList);
        bleRv.setLayoutManager(new LinearLayoutManager(ScanBleFragment.this.getContext()));
        bleRv.setAdapter(inspectBleAdapter);
        inspectBleAdapter.setOnItemClickListener(new InspectBleAdapter.OnItemConfirmClickListener() {
            @Override
            public void onItemClickListener(BleDevice device) {
                scanMainActivity.connectBleDevice = device;
                scanMainActivity.doConnectBle();
            }
        });
    }


    public void  initAdapterCheckList(){
        if(inspectBleAdapter!=null){
            inspectBleAdapter.initCheckList();
        }

    }
    @Override
    protected void submitRequestData() {

    }


    @OnClick({R.id.ble_update, R.id.ble_update_iv, R.id.button_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ble_update:
            case R.id.ble_update_iv:
                checkBle();
                break;
            case R.id.button_next:
                connectToBle();
                break;
        }
    }

    /**
     * 连接蓝牙
     */
    private void connectToBle() {
        List<CheckBean> checkList = inspectBleAdapter.getCheckList();
        int mSize = checkList.size();
        boolean isHaveCheck = false;//是否选择
        for (int i = 0; i < mSize; i++) {
            if (checkList.get(i).isCheck()) {
                isHaveCheck = true;
            }
        }

        if (!isHaveCheck) {
            ToastUtil.showWX("请选择设备");
        } else {
            /*蓝牙连接成功跳转到车辆*/
            if (scanMainActivity.getBleConnectStatus()) {
                scanMainActivity.setVpCurrentItem(0);

            }
        }

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        checkBle();
    }

    /**
     * 判断蓝牙并开始扫描
     */
    private void checkBle() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            ToastUtil.showWX("请打开蓝牙");
            return;
        }

        startScan();
    }


    /**
     * 设置蓝牙规则
     */
    private void setScanRule() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                // 只扫描指定mac的设备，可选
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                bleUpdate.setText("正在搜索");
                bleList.clear();
                inspectBleAdapter.setNewDataForSearch(null);
                LogUtil.i("onScanStarted");
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                LogUtil.i("onScanning");
                if (!bleList.contains(bleDevice)) {
                    if (!TextUtils.isEmpty(bleDevice.getName())) {
                        bleList.add(bleDevice);
                        inspectBleAdapter.addNewBle(bleDevice);
                    }
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                LogUtil.i("onScanFinished");
                if(bleUpdate!=null){
                    bleUpdate.setText("刷新列表");
                }

            }
        });
    }


}