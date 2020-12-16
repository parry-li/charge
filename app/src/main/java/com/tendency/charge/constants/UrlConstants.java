package com.tendency.charge.constants;


public class UrlConstants {


    /**
     * 线上正式服务器
     */
//
//    public static final String main_host_service = "https://ddc.iotone.cn/";//正式
//    public static final String bugly_id = "bffb30d76d";//正式


    /**
     * 内网测试服务器
     * */

    public static final String main_host_service = "http://10.140.0.75:8443/";//测试内网
    public static final String bugly_id = "43baf9a0b3";//测试


    /**
     * 外网测试服务器
     */
//    public static final String main_host_service = "http://122.228.188.212:8443/";
//    public static final String bugly_id = "43baf9a0b3";//测试









    /*=====================================================*/

    public static final String cityConfigure_getCityList = main_host_service + "api/ddc-user/cityConfigure/getCityList";
    public static final String user_login = main_host_service + "api/ddc-user/user/login";
    public static final String cityConfigure_getCityConfigureBySubsystemId = main_host_service + "api/ddc-user/cityConfigure/getCityConfigureBySubsystemId";
    public static final String electriccarsChange_check = main_host_service + "api/ddc-electriccar/electriccarsChange/check";
    /*车牌补办登记*/
    public static final String electriccarsChange_register = main_host_service + "api/ddc-electriccar/electriccarsChange/register";
    /*照片上传*/
    public static final String zimgCommon_uploadMultFile = main_host_service + "api/ddc-user/zimgCommon/uploadMultFile";
    /*字典数据*/
    public static final String codeTable_contentList = main_host_service + "api/ddc-user/codeTable/contentList";
    /*报废*/
    public static final String electriccarsScrap_add = main_host_service + "api/ddc-electriccar/electriccarsScrap/add";
    /*校验车牌*/
    public static final String electriccars_checkOnlyOnePlateNumber = main_host_service + "api/ddc-electriccar/electriccars/checkOnlyOnePlateNumber";
    /*获取保险*/
    public static final String configure_getInsuranceConfigs = main_host_service + "api/ddc-insurance/insurance/configure/getInsuranceConfigs";
    /*备案登记*/
    public static final String electriccars_registration = main_host_service + "api/ddc-electriccar/electriccars/registration";
    /*查询车辆*/
    public static final String electriccars_editInfo = main_host_service + "api/ddc-electriccar/electriccars/editInfoList";
    /*信息变更*/
    public static final String electriccars_edit = main_host_service + "api/ddc-electriccar/electriccars/edit";
    /*获取保险变更*/
    public static final String policy_edit = main_host_service + "api/ddc-electriccar/policy/edit";
    /*获取保险新保*/
    public static final String policy_getNewInsuranceConfigs = main_host_service + "api/ddc-electriccar/policy/getNewInsuranceConfigs";
    /*获取保险续保*/
    public static final String policy_getRenewInsuranceConfigs = main_host_service + "api/ddc-electriccar/policy/getRenewInsuranceConfigs";
    /*保险变更*/
    public static final String policy_insured = main_host_service + "api/ddc-electriccar/policy/insured";
    /*保险变更*/
    public static final String user_updatePwd = main_host_service + "api/ddc-user/user/updatePwd";
    /*待投保查询*/
    public static final String policy_failurePage = main_host_service + "api/ddc-electriccar/policy/failurePage";
    /*重新投保*/
    public static final String policy_reinsure = main_host_service + "api/ddc-electriccar/policy/reinsure";
    /*个人统计*/
    public static final String installSituation_query2User = main_host_service + "api/ddc-statistical-report/installSituation/query2User";
    /*备案统计*/
    public static final String installSituation_query2Unit = main_host_service + "api/ddc-statistical-report/installSituation/query2Unit";
    /*XI辖区列表*/
    public static final String unit_unitTreeByUnitNo = main_host_service + "api/ddc-user/unit/unitTreeByUnitNo";
    /*车辆信息*/
    public static final String electriccars_info = main_host_service + "api/ddc-electriccar/electriccars/info";
    /*黑车校验*/
    public static final String electriccars_checkBlackCar = main_host_service + "api/ddc-electriccar/electriccars/checkBlackCar";
    /*校验标签*/
    public static final String electriccars_checkOnlyOneLabel = main_host_service + "api/ddc-electriccar/electriccars/checkOnlyOneLabel";
    /*版本下载*/
    public static final String minio_download = main_host_service + "api/ddc-file/minio/download";
    /*校验版本*/
    public static final String version_checkDevice = main_host_service + "api/ddc-deploycontrol/version/checkDevice";
    /*新国标品牌*/
    public static final String brandModel_allBrandList = main_host_service + "api/ddc-user/brandModel/allBrandList";
    /*查询车架编码*/
    public static final String electriccars_getCarInfoByVehicleCode = main_host_service + "api/ddc-electriccar/electriccars/getCarInfoByVehicleCode";
    /*查询审查*/
    public static final String checker_allCheckers = main_host_service + "api/ddc-user/checker/allCheckers";
    /*新国标备案登记*/
    public static final String electriccars_registrationMeetStandard = main_host_service + "api/ddc-electriccar/electriccars/registrationMeetStandard";
    /*发证机关*/
    public static final String electriccars_getLicensePlateList = main_host_service + "api/ddc-electriccar/electriccars/getLicensePlateList";
    /*登记上牌*/
    public static final String electriccars_registrationMeetStandardNoLabel = main_host_service + "api/ddc-electriccar/electriccars/registrationMeetStandardNoLabel";
    /*信息变更*/
    public static final String electriccars_editMeetStandard = main_host_service + "api/ddc-electriccar/electriccars/editMeetStandard";
    /*老国标登记上牌*/
    public static final String electriccars_registrationNoLabel = main_host_service + "api/ddc-electriccar/electriccars/registrationNoLabel";
    /*车牌补办查询*/
    public static final String electriccarReissue_getReissue = main_host_service + "api/ddc-electriccar/electriccarReissue/getReissue";
    /*车牌补办领取*/
    public static final String electriccarReissue_receive = main_host_service + "api/ddc-electriccar/electriccarReissue/receive";
    /*车牌补办登记*/
    public static final String electriccarReissue_add = main_host_service + "api/ddc-electriccar/electriccarReissue/add";
    /*蓄电池型号*/
    public static final String batteryBrandModel_query = main_host_service + "api/ddc-electriccar/batteryBrandModel/query";
    /*蓄电池添加*/
    public static final String battery_add = main_host_service + "api/ddc-electriccar/battery/add";
    /*蓄电池查询*/
    public static final String battery_appQuery = main_host_service + "api/ddc-electriccar/battery/appQuery";
    /*获取所有省市区街道*/
    public static final String administrativeCode_getAllAdministrativeCode = main_host_service + "api/ddc-user/administrativeCode/getAllAdministrativeCode";
    /*车牌补办-车牌发货*/
    public static final String electriccarReissue_ship = main_host_service + "api/ddc-electriccar/electriccarReissue/ship";
    /*车牌更换-更换业务*/
    public static final String electriccarsPlateChange_change = main_host_service + "api/ddc-electriccar/electriccarsPlateChange/change";
    /*转移登记*/
    public static final String electriccarsTransfer_add = main_host_service + "api/ddc-electriccar/electriccarsTransfer/add";
    /*车辆注销*/
    public static final String electriccarsCancel_add = main_host_service + "api/ddc-electriccar/electriccarsCancel/add";
    /*车辆延期*/
    public static final String electriccarsDelay_add = main_host_service + "api/ddc-electriccar/electriccarsDelay/add";
    /*标签变更*/
    public static final String electriccarsLabelChange_add = main_host_service + "api/ddc-electriccar/electriccarsLabelChange/add";
    /*行驶证补办/变更-补办/变更业务*/
    public static final String electriccarsLicense_add = main_host_service + "api/ddc-electriccar/electriccarsLicense/add";
    /*变更登记-变更登记业务*/
    public static final String electriccarsModify_add = main_host_service + "api/ddc-electriccar/electriccarsModify/add";
    /*获取照片*/
    public static final String zimgCommon_getImg = main_host_service + "api/ddc-user/zimgCommon/getImg?photoId=";
    /*根据车牌获取标签*/
    public static final String electriccarsCode_getCarLabelByPlate = main_host_service + "api/ddc-electriccar/electriccarsCode/getCarLabelByPlate";

}
