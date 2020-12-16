package com.tendency.charge.constants;

import android.Manifest;
import android.os.Environment;

/**
 * Created by quantan.liu on 2017/3/21.
 */

public class BaseConstants {


    /*布局*/
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;

    public static final int BUX_SEND_CODE = 2001;
    public static final int STATE_SUCCESS12 = 4;
    /*蓝牙id  目前在用*/
    public final static String ServiceUUID = "0000ffe0-0000-1000-8000-00805f9b34fb";

    /*备案登记权限码，当前和其他选项相匹配*/
    public final static String[] funJurisdiction = {
            "0", //车辆报废0   废除
            "licensePlate:register",//车牌补办1
            "2", //车辆过户2  废除
            "policy:renewal_renew",//服务延期3
            "policy:renewal",//服务购买4
            "8",//备案统计5
            "electriccarsMeetStandard:registrationNoLabel",//登记上牌6
            "battery:add",//蓄电池备案登记7
            "battery:query",//蓄电池查询8
            "plateChange:change",//车牌更换9
            "changeRegistration:change",//变更登记10
            "vehicleCancellation:cancel",//车辆注销11
            "vehicleDelay:delay",//车辆延期12
            "labelChange:change",//标签更换13
            "changeOfDrivingLicense:change",//行驶证更换14
            "reissueOfDrivingLicense:change",//行驶证补办15
            "registrationOfTransfer:transfer",//转移登记16
    };

    /*备案登记权限码，用于排序，根据当前先后排序*/
    /**
     * 登记上牌
     * 备案统计
     * 服务购买
     * 服务延期
     * 蓄电池登记
     * 蓄电池查询
     * 变更登记
     * 转移登记
     * 车牌补办
     * 车辆更换
     * 行驶证补办
     * 行驶证更换
     * 车辆注销
     * 车辆延期
     * 标签更换
     */
    public final static String[] funSort = {
            "electriccarsMeetStandard:registrationNoLabel",//登记上牌6
            "8",//备案统计5
            "policy:renewal",//服务购买4
            "battery:add",//蓄电池备案登记7
            "battery:query",//蓄电池查询8
            "changeRegistration:change",//变更登记10
            "registrationOfTransfer:transfer",//转移登记16
            "licensePlate:register",//车牌补办1
            "plateChange:change",//车牌更换9
            "reissueOfDrivingLicense:change",//行驶证补办15
            "changeOfDrivingLicense:change",//行驶证更换14
            "vehicleCancellation:cancel",//车辆注销11
            "vehicleDelay:delay",//车辆延期12
            "labelChange:change",//标签更换13

    };
    public static final String FILE_PROVIDER_AUTHORITY = "com.tdr.registration";

    public static final String[] PERMISSION_CONTENT =
            {Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN};

    public static String APP_NAME = "com.tdr.charge.";
    /*配置缓存*/
    public static final String ERROR_MSG = APP_NAME + "Exception";
    public static final String token = APP_NAME + "token";
    public static final String uType = APP_NAME + "uType";
    public static final String data = APP_NAME + "register_data";
    public static final String data2 = APP_NAME + "register_data2";
    public static final String rolePower = APP_NAME + "role_power";
    public static final String BillConfig = APP_NAME + "BillConfig";
    public static final String MapConfig = APP_NAME + "MapConfig";
    public static final String VehicleConfig = APP_NAME + "VehicleConfig";
    public static final String PhotoConfig = APP_NAME + "PhotoConfig";
    public static final String NbLabelConfig = APP_NAME + "NbLabelConfig";
    public static final String RegisterConfig = APP_NAME + "RegisterConfig";
    public static final String ManagerConfig = APP_NAME + "ManagerConfig";
    public static final String AuditConfig = APP_NAME + "AuditConfig";
    public static final String PlateReissueConfig = APP_NAME + "PlateReissueConfig";
    public static final String BatteryConfig = APP_NAME + "BatteryConfig";
    public static final String PlateChangeConfig = APP_NAME + "PlateChangeConfig";//车牌更换
    public static final String RegisterTransferConfig = APP_NAME + "RegisterTransferConfig";//转移登记
    public static final String RegisterModifyConfig = APP_NAME + "RegisterModifyConfig";//变更登记
    public static final String VehicleCancelConfig = APP_NAME + "VehicleCancelConfig";//车辆注销
    public static final String VehicleDelayConfig = APP_NAME + "VehicleDelayConfig";//车辆延期
    public static final String LabelChangeConfig = APP_NAME + "LabelChangeConfig";//标签更换
    public static final String LicenseChangeConfig = APP_NAME + "LicenseChangeConfig";//标签更换
    public static final String LicenseReissueConfig = APP_NAME + "LicenseReissueConfig";//标签更换


    public static final String IsEnableAlbum = APP_NAME + "isEnableAlbum";///*默认相册 1不开启，2开启*/


    /* Nb标签配置 detectNb 默认1不检测 2表示检测
     *             isBindRFLable   标签是否绑定RF标签  默认1表示不绑定  2表示绑定*/
    public static final String nb_detectNb = APP_NAME + "detectNb";//Boolean  true校验
    public static final String nb_isBindRFlabel = APP_NAME + "detectNb";//Boolean  true绑定
    public static final String nb_ble_mac = APP_NAME + "bleMac";//缓存的蓝牙mac
    public static final String nb_hard_version = APP_NAME + "hand_version";//硬件版本号
    public static final String nb_soft_version = APP_NAME + "soft_version";//软件版本号
    public static final String nb_satellites = APP_NAME + "satellites";//卫星数
    public static final String nb_signal = APP_NAME + "signal";//信号强度


    public static final String menuList = APP_NAME + "menuList";//权限码

    public static final String TYPE = APP_NAME + "key_type";//类型
    public static final String KEY_NAME = APP_NAME + "picked_name";
    public static final String KEY_VALUE = APP_NAME + "picked_value";
    public static final String KEY_VALUE2 = APP_NAME + "picked_value2";

    public static final String City_name = APP_NAME + "City_name";
    public static final String City_systemID = APP_NAME + "City_systemID";// int类型
    public static final String Login_unitName = APP_NAME + "Login_unitName";
    public static final String City_cityCode = APP_NAME + "City_cityCode";
    public static final String Login_unitNo = APP_NAME + "Login_unitNo";
    public static final String Login_unitType = APP_NAME + "Login_unitType";// int类型
    public static final String Login_name = APP_NAME + "Login_name";
    public static final String Login_user_name = APP_NAME + "Login_user_name";
    public static final String code_table = APP_NAME + "code_table";


    /*蓝牙相关缓存*/
    public static final String ble_key = APP_NAME + "blue_key";
    public static final String ble_pcCode = APP_NAME + "pcCode";
    public static final String ble_isEnableBuzzer = APP_NAME + "isEnableBuzzer";
    public static final String ble_deviceType = APP_NAME + "deviceType";
    public static final String ble_content = APP_NAME + "content";
    public static final String ble_XQCode = APP_NAME + "XQCode";
    public static final String ble_cityAbbr = APP_NAME + "cityAbbr";
    public static final String ble_provinceAbbr = APP_NAME + "provinceAbbr";

    /*身份证扫码*/
    public static final String id_card = "id_card";
    public static final int GUO_CODE_BRAND = 500;//品牌
    public static final int GUO_CODE_EXAMINE = 501;//审查员
    public static final int GUO_CODE_CHECK = 502;//查验员
    public static final int GUO_CODE_LICENSE = 503;//发证机关
    public static final int GUO_CODE_BATTERY = 504;//发证机关
    //APP文件路径
    public static final String tdrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Registration";
    //APP图片缓存路径
    public static final String imagePath = tdrPath + "/imgCache";

    public static final int CHANGE_REGISTER_PLATE = 601;//登记上牌
    public static final int CHANGE_REGISTER_INFO = 602;//信息变更

    /*二维码*/
    public static final String SCAN_HAND_INPUT = APP_NAME + "hand_input";//是否手动输入 true为手动输入
    public static final String SCAN_RESULT = APP_NAME + "result";

    public static final String BUGLY_UPDATE = APP_NAME + "BUGLY_UPDATE";//强制升级 是否退出
}
