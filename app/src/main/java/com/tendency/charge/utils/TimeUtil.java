package com.tendency.charge.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {


    /** 选择的时间和当前时间对比
     * @param endTime
     * @return  true 结束时间小于开始时间
     */
    public static boolean timeCompare(String endTime) {

        int timeIndex = timeCompare(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"), endTime);
        if (timeIndex < 3) {
            return true;
        } else {
            ToastUtil.showWX("选择时间不能超过当前时间");
            return false;
        }

    }

    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     *
     * @param startTime
     * @param endTime
     * @return 1 结束时间小于开始时间
     * 2 开始时间与结束时间相同
     * 3 结束时间大于开始时间
     */
    public static int timeCompare(String startTime, String endTime) {
        int i = 0;
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //结束时间小于开始时间
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                //开始时间与结束时间相同
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //结束时间大于开始时间
                i = 3;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return i;
    }


    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @return
     */
    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DATE);
    }

    /**
     * 获取时
     *
     * @return
     */
    public static int getHour() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.HOUR);
    }

    /**
     * 获取分
     *
     * @return
     */
    public static int getMinute() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     *
     * @return
     */
    public static int getSecond() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.SECOND);
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     */

    public static String getCurrentTime(String formatType) {
        return getFormatedDateTime(System.currentTimeMillis(), formatType);
    }

    /**
     * 将long转换为日期（yyyy-MM-dd HH:mm）
     *
     * @param dateTime
     * @return 到分
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDateTime(long dateTime, String formatType) {
        String time = "";
        if (TextUtils.isEmpty(formatType)) {
            formatType = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(formatType);
            time = sDateFormat.format(new Date(dateTime + 0));
        } catch (Exception e) {

        }
        return time;
    }


}
