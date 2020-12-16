package com.tendency.charge.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tendency.charge.App;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 专门提供为处理一些UI相关的问题而创建的工具类，
 * 提供资源获取的通用方法，避免每次都写重复的代码获取结果。
 */
public class UIUtils {

    public static Context getContext() {
        return App.context;
    }

    /**
     * @param s
     * @return
     */
    public final static boolean isNumber(String s) {
        if (s != null && !"".equals(s.trim())) {
            return s.matches("^[0-9]*$");
        }else {
            return false;
        }
    }
    /**
     * 设置输入框大写
     *
     * @param editText
     */
    public static void setEditTextUpperCase(EditText editText) {

        editText.setTransformationMethod(new AllCapTransformationMethod(true));
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**判断内容是否为空
     * @param value
     * @return
     */
    public static String isEmpty(String value){
        if(!TextUtils.isEmpty(value)){
            if(!"null".equals(value.toLowerCase())){
                return value;
            }else {
                return "";
            }

        }else {
            return "";
        }
    }
    /**
     * @return true 大于
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
            LogUtil.i("curClickTime===" + curClickTime + "===lastClickTime====" + lastClickTime);
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isFastClick(int setTime) {

        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= setTime) {
            flag = true;

        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 禁止输入空格
     *
     * @param editText
     * @return
     */
    public static TextWatcher prohibitEditSpace(final EditText editText) {
        TextWatcher textChanged = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 禁止EditText输入空格
                if (charSequence.toString().contains(" ")) {
                    String[] str = charSequence.toString().split(" ");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < str.length; i++) {
                        sb.append(str[i]);
                    }
                    editText.setText(sb.toString());
                    editText.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        return textChanged;
    }

    /**是否输入特殊字符
     * @return true 不包含
     */
    public static boolean judgeText(String source) {
        if(source.contains(" ")){
            return false;
        }
        String speChat = "[`— - _~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(speChat);
        Matcher matcher = pattern.matcher(source.toString());
        if (matcher.find()) {
            return false;
        } else {
            return true;
        }

    }
    static List<Activity> sActivityList = new LinkedList<>();



    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * byte转String
     *
     * @param bt
     * @return
     */
    public static String Byte2Str(byte[] bt) {
        return Base64.encode(bt);
    }

    /**
     * 若都不为空，将arrSrc2添加到arrSrc1的末尾组合成新的byte数组
     *
     * @param arrSrc1
     * @param arrSrc2
     * @return
     */
    public static final byte[] ByteArrayCopy(byte[] arrSrc1, byte[] arrSrc2) {
        byte[] arrDes = null;
        if (arrSrc2 == null) {
            arrDes = arrSrc1;
            return arrDes;
        }

        if (arrSrc1 != null) {
            arrDes = new byte[arrSrc1.length + arrSrc2.length];
            System.arraycopy(arrSrc1, 0, arrDes, 0, arrSrc1.length);
            System.arraycopy(arrSrc2, 0, arrDes, arrSrc1.length, arrSrc2.length);
        } else {
            arrDes = new byte[arrSrc2.length];
            System.arraycopy(arrSrc2, 0, arrDes, 0, arrSrc2.length);
        }

        return arrDes;
    }

    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * 10转16
     *
     * @param num
     * @return
     */
    public static String void10to16(String num) {
//        int intNum = Integer.parseInt(num);

        BigInteger b = new BigInteger(num);
        String intNum = b.toString(16);
        return intNum;
    }

    /**
     * 判断位数是否是偶数，否在前面加0
     *
     * @param num
     * @return
     */
    public static String setEvenNum(String num) {
        int nlength = num.length();
        if (nlength % 2 != 0) {
            num = "0" + num;
        }

        return num;
    }

    // 高低位转换
    public static String highAndLowChange(String value) {
        int valueLen = value.length();
        if (valueLen % 2 != 0) {
            value = autoGenericCode(value, valueLen + 1);
            valueLen = value.length();
        }
        StringBuilder lastData = new StringBuilder();
        for (int i = 0; i <= valueLen - 2; i += 2) {

            lastData.append(value.substring((valueLen - i - 2), valueLen - i));
        }
        return lastData.toString();
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {

        String res = String.format("%" + num + "s", code);
        res = res.replaceAll("\\s", "0");
        return res;
    }

    /**
     * 不够位数的在后面补0，保留num的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCodeBehind(String code, int num) {

        for (int i = code.length(); i < num; i++) {
            code += "0";
        }
        return code;
    }

    /**
     * byte——>String
     *
     * @param src
     * @return
     */
    public static final String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 数组截取：从arrData的offset开始获取len个长度的byte生成的len的byte[]
     *
     * @param arrData
     * @param offset
     * @param len
     * @return
     */
    public static final byte[] GetByteArrayByLength(byte[] arrData, int offset, int len) {
        byte[] newData = new byte[len];
        try {
//            LOG.e("GetByteArrayByLength    newData="+newData.length+"  offset="+offset+"   len"+len);
            for (int i = offset; i < offset + len; i++) {
                newData[i - offset] = arrData[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newData;
    }

    /**
     * short转换成byte数组
     *
     * @param s
     * @return
     */
    public static final byte[] shortToBytes(short s) {//01e2
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 1 - i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 0xff);
        }
        return shortBuf;
    }

    //返回指定colorId对应的颜色值
    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

//    private double doSetDoubleFormat(double formatMoney) {
//        DecimalFormat df = new DecimalFormat("#.##");
//        formatMoney = Double.valueOf(df.format(formatMoney));
//
//
//        return formatMoney;
//    }

    //加载指定viewId的视图对象，并返回
    public static View getView(int viewId) {
        View view = View.inflate(getContext(), viewId, null);
        return view;
    }

    public static String[] getStringArr(int strArrId) {
        String[] stringArray = getContext().getResources().getStringArray(strArrId);
        return stringArray;
    }

    //将dp转化为px
    public static int dp2px(int dp) {
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);//实现四舍五入
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(int px) {
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);//实现四舍五入
    }

    // 合成新的颜色值
    public static int getNewColorByStartEndColor(Context context, float fraction, int startValue, int endValue) {
        return evaluate(fraction, context.getResources().getColor(startValue), context.getResources().getColor(endValue));
    }

    /**
     * 合成新的颜色值
     *
     * @param fraction   颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue   结束显示的颜色
     * @return 返回生成新的颜色值
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

//    //保证runnable中的操作在主线程中执行
//    public static void runOnUiThread(Runnable runnable) {
//        if(isInMainThread()){
//            runnable.run();
//        }else{
//            UIUtils.getHandler().post(runnable);
//        }
//    }
//    //判断当前线程是否是主线程
//    private static boolean isInMainThread() {
//        int currentThreadId = android.os.Process.myTid();
//        return MyApplication.mainThreadId == currentThreadId;
//
//    }

//    public static void toast(String message,boolean isLengthLong){
//        Toast.makeText(UIUtils.getContext(), message,isLengthLong? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
//    }

//    // 合成新的颜色值
//    public static int getNewColorByStartEndColor(Context context, float fraction, int startValue, int endValue) {
//        return evaluate(fraction, context.getResources().getColor(startValue), context.getResources().getColor(endValue));
//    }
//    /**
//     * 合成新的颜色值
//     * @param fraction 颜色取值的级别 (0.0f ~ 1.0f)
//     * @param startValue 开始显示的颜色
//     * @param endValue 结束显示的颜色
//     * @return 返回生成新的颜色值
//     */
//    public static int evaluate(float fraction, int startValue, int endValue) {
//        int startA = (startValue >> 24) & 0xff;
//        int startR = (startValue >> 16) & 0xff;
//        int startG = (startValue >> 8) & 0xff;
//        int startB = startValue & 0xff;
//
//        int endA = (endValue >> 24) & 0xff;
//        int endR = (endValue >> 16) & 0xff;
//        int endG = (endValue >> 8) & 0xff;
//        int endB = endValue & 0xff;
//
//        return ((startA + (int) (fraction * (endA - startA))) << 24) |
//                ((startR + (int) (fraction * (endR - startR))) << 16) |
//                ((startG + (int) (fraction * (endG - startG))) << 8) |
//                ((startB + (int) (fraction * (endB - startB))));
//    }

    @TargetApi(19)
    public static String formatUri(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     *                      [url=home.php?mod=space&uid=7300]@return[/url] The value of
     *                      the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String HL_Transposition(String s) {
        Log.e("Pan", "换位前=" + s);
        String ret = "";
        String sA = s.substring(0, 2);
        String sB = s.substring(2);
        int A = Integer.parseInt(sA, 16);
        int B = Integer.parseInt(sB, 16);
        if (A > B) {
            ret = sB + sA;
        }
        Log.e("Pan", "换位后=" + ret);
        return ret;
    }


    /**
     * bytes字符串转换为Byte值
     *
     * @param src src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     * <p>
     * 89860070111257004158--->0x89 0x86.... 0
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            System.out.println("0x" + src.substring(i * 2, m) + src.substring(m, n));
            ret[i] = (byte) Integer.parseInt((src.substring(i * 2, m) + src.substring(m, n)).trim(), 16);
        }
        return ret;
    }


    /**
     * 获取两数相除结果
     *
     * @param i 分子
     * @param j 分母
     * @return
     */
    public static String getDivide(int i, int j) {
        NumberFormat f = NumberFormat.getNumberInstance();
        f.setMaximumFractionDigits(1);
        f.setMinimumFractionDigits(1);
        double d = (i * 1.0 / j) * 100;
        return f.format(d);
    }

    public static final byte[] intTobyte2(int s) {
        byte[] shortBuf = new byte[1];
        shortBuf[0] = (byte) (s & 0xff);
        return shortBuf;
    }

    //long类型转成byte数组
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[6];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * short 转 byte 高低位互换
     *
     * @param s
     * @return byte[]
     */
    public static final byte[] shortToByte(short s) {//e201
        byte[] shortBuf = new byte[2];
        shortBuf[0] = (byte) (s & 0xff);
        shortBuf[1] = (byte) ((s >>> 8) & 0xff);
        return shortBuf;
    }


    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }
}
