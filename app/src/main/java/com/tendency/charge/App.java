package com.tendency.charge;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;


import com.clj.fastble.BleManager;
import com.parry.utils.code.CrashUtils;
import com.parry.utils.code.LogUtils;
import com.parry.utils.code.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;
import com.tendency.charge.constants.UrlConstants;
import com.tendency.charge.utils.LogUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class App extends MultiDexApplication {

    private static App INSTANCE;
    public static Context context;



    public static synchronized App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        context = this.getApplicationContext();
        Utils.init(this);//一个utils库的初始化
        LogUtils.getConfig().setGlobalTag("parry");

        initBle();

        initCrash();

        checkBuglyUpdate();

    }

    /**
     * 初始化蓝牙
     */
    private void initBle() {
        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(30000)
                .setOperateTimeout(5000);
    }

    /**
     * 检测更新
     */
    private void checkBuglyUpdate() {
        Beta.upgradeDialogLayoutId = R.layout.layout_update_popup;
        Beta.tipsDialogLayoutId = R.layout.dialog_tips;
        Beta.strNetworkTipsCancelBtn = "";
        Beta.strUpgradeDialogCancelBtn = "     ";
        Beta.initDelay = 4 * 1000;
        Beta.largeIconId = R.mipmap.ico_register_launcher;



        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, final UpgradeInfo upgradeInfo) {
                LogUtils.i("upgradeType:" + upgradeInfo.upgradeType);
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:

                // 通过id方式获取控件
                ImageView ivCancel =(ImageView) view.findViewById(R.id.iv_cancel_update);

                // 更多的操作：比如设置控件的点击事件
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (upgradeInfo.upgradeType == 2) {
                            LogUtils.i("退出应用");
                            System.exit(0);
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                });
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {


            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {

            }

        };

        Bugly.init(App.getInstance(), UrlConstants.bugly_id, false);
    }





    public static boolean isApkInDebug() {
        try {
            ApplicationInfo info =  App.getContext().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }



    public static Context getContext() {
        return App.context;
    }




    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
    private void initCrash() {

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            CrashUtils.init(new CrashUtils.OnCrashListener() {


                @Override
                public void onCrash(String crashInfo, Throwable e) {
                    LogUtil.e("crashInfo" + crashInfo);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
