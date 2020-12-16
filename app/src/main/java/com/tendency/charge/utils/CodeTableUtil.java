//package com.tdr.registration.utils;
//
//import android.graphics.Bitmap;
//
//import com.google.gson.Gson;
//import com.parry.utils.code.SPUtils;
//import com.tdr.registration.bean.CodeTableBean;
//import com.tdr.registration.bean.PhotoBean;
//import com.tdr.registration.constants.BaseConstants;
//import com.tdr.registration.constants.UrlConstants;
//import com.tdr.registration.http.utils.DdcResult;
//import com.tdr.registration.listener.CustomSendLister;
//import com.tdr.registration.listener.CustomSendOperater;
//import com.tdr.registration.service.BaseService;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class CodeTableUtil {
//
//    /**
//     * 0字典类型说明，1电动车品牌,2电动车类型,3 登记点类型,4颜色,5基站类型,6证件类型,7来历凭证,8基站运维状态,
//     * 9业主单位,10运维单位,11基站用途,12保险公司,13车辆类型,15警车品牌,16 蓄电池品牌,17 蓄电池型号,18 经营范围,
//     * 19 NB标签报警类型 20 电动车注销原因 21 所属部门
//     */
//    public static void getCodeTable(final int type) {
//        CustomSendLister imageSendLister = new CustomSendLister() {
//            @Override
//            public void sendResult(Boolean isSuccess, int position, String photoId) {
//
//            }
//        };
//        getCodeTable(type, imageSendLister);
//    }
//
//    public static void getCodeTable(final int type, final CustomSendLister imageSendLister) {
//
//        final CustomSendOperater operater = new CustomSendOperater();
//        operater.setListener(imageSendLister);
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(UrlConstants.main_host_service)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        BaseService service = retrofit.create(BaseService.class);
//        Map<String, Object> map = new HashMap<>();
//        map.post("type", type);
//        map.post("page", 1);
//        map.post("size", 0);
//
//        String strEntity = new Gson().toJson(map);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
//
//
//        Call<DdcResult<CodeTableBean>> call = service.codeTable(UrlConstants.codeTable_contentList, body);
//        call.enqueue(new Callback<DdcResult<CodeTableBean>>() {
//            @Override
//            public void onResponse(Call<DdcResult<CodeTableBean>> call, Response<DdcResult<CodeTableBean>> response) {
//
//                String json = new Gson().toJson(response.body().getData());
//                SPUtils.getInstance().post(BaseConstants.code_table + type, json);
//                operater.sendResult(true,0,"");
//            }
//
//            @Override
//            public void onFailure(Call<DdcResult<CodeTableBean>> call, Throwable t) {
//
//
//            }
//        });
//
//    }
//}
