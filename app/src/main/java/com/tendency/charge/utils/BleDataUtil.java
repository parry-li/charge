package com.tendency.charge.utils;

import android.app.Activity;


/**
 * 稽查枪更新包
 */
public class BleDataUtil {

    private String File_PATH;
    private Activity mActivity;
    private String File_Name = "gunUpdateFile.zip";
    public byte[] frameFlag = {(byte) 0xAA};
    public static byte[] frameFlagStatic = {(byte) 0xAA};


    public byte[] noneData = {(byte) 0x00};
    public byte[] frame01 = {(byte) 0x01};
    public byte[] frame10 = {(byte) 0x10};

    public byte[] packagingStart = {(byte) 0x61};//打包开始下发
    public byte[] packagingComplete = {(byte) 0x62};//打包下发完成
    public byte[] packagingSend = {(byte) 0x63};//打包下发内容

    public BleDataUtil() {


    }





    private byte[] writeContent;




    /**
     * 下发07数据 获取版本
     *aa81 01043838000000010000000000000000 bedb
     * @param
     * @return
     */
    public  byte[] writeDataToBle10() {
        byte[] requestData = null;//全部内容
//        requestData = UIUtils.ByteArrayCopy(requestData, frameFlag);
        requestData = UIUtils.ByteArrayCopy(requestData, frame10);
        requestData = UIUtils.ByteArrayCopy(requestData, frame01);
        for (int i = 0; i < 17; i++) {
            requestData = UIUtils.ByteArrayCopy(requestData, noneData);
        }
        LogUtil.i("===requestData=1=="+UIUtils.bytesToHexString(requestData));
        requestData = CRC16MP.getSendBuf(requestData);
        requestData =  UIUtils.ByteArrayCopy(frameFlag, requestData);
        LogUtil.i("===requestData==="+UIUtils.bytesToHexString(requestData));
        return requestData;
    }

    /**
     * 打包开始下发
     * 0x61 ,0x62
     *
     * @return
     */
    public byte[] writeUpteDataToBle61or62(int flag) {
        byte[] data = writeContent;
        byte[] requestData = null;//全部内容
        byte[] content = null;
        byte[] contentLength = null;
        while (data.length % 12 != 0) {
            data = UIUtils.ByteArrayCopy(data, noneData);
        }

        contentLength = UIUtils.intToByte(data.length);
        //byte[] totalPackage = Utils.intToByte((int) Math.ceil((short) data.length / 12));
        short length = (short) Math.ceil((short) data.length / 12);//总共需要发送的包数
        if (data.length % 12 != 0) {
            length += 1;
        }
        byte[] totalPackage = UIUtils.intToByte(length);
        byte[] allContentVerifyCode = UIUtils.shortToBytes(CRC16M.CalculateCrc16(data));
        content = UIUtils.ByteArrayCopy(content, contentLength);
        content = UIUtils.ByteArrayCopy(content, totalPackage);
        content = UIUtils.ByteArrayCopy(content, allContentVerifyCode);
        for (int i = content.length; i < 16; i++) {
            content = UIUtils.ByteArrayCopy(content, noneData);
        }
        requestData = UIUtils.ByteArrayCopy(requestData, frameFlag);
        if (flag == 61) {
            requestData = UIUtils.ByteArrayCopy(requestData, packagingStart);
        } else if (flag == 62) {
            requestData = UIUtils.ByteArrayCopy(requestData, packagingComplete);
        }
        requestData = UIUtils.ByteArrayCopy(requestData, content);
        byte[] verifyCode = UIUtils.shortToBytes(CRC16M.CalculateCrc16(UIUtils.GetByteArrayByLength(requestData, 1, 17)));
        requestData = UIUtils.ByteArrayCopy(requestData, verifyCode);
        String bb = UIUtils.bytesToHexString(requestData);
        LogUtil.e("update1requestData:" + UIUtils.bytesToHexString(requestData));
        LogUtil.e("update2requestData:" + bb);
        return requestData;
    }


//    /**下发63
//     * @return
//     */
//    public List<byte[]> writeUpteDataToBle63() {
//        byte[] data = writeContent;
//        String SS = Utils.bytesToHexString(data);
//        Log.e("Pan", "53:data=" + SS);
//
//        List<byte[]> allDataList= new ArrayList<>();
//        int length =  data.length / 12;//总共需要发送的包数
//        if (data.length % 12 != 0) {
//            length += 1;
//        }
//        for (short i = 0; i < length; i++) {
//
//            byte[] requestData = null;//全部内容
//            byte[] packageNum = new byte[4];
//            byte[] packageContent = new byte[12];
//            byte[] childContent = null;
//            packageNum = Utils.intToByte(i + 1);
//            packageContent = Utils.GetByteArrayByLength(data, 12 * i, 12);
//            childContent = Utils.ByteArrayCopy(childContent, packageNum);
//            childContent = Utils.ByteArrayCopy(childContent, packageContent);
//
//            requestData = Utils.ByteArrayCopy(requestData, frameFlag);
//            requestData = Utils.ByteArrayCopy(requestData, packagingSend);
//            requestData = Utils.ByteArrayCopy(requestData, childContent);
//
//            byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
//            requestData = Utils.ByteArrayCopy(requestData, verifyCode);
//            allDataList.add(requestData);
//        }
//
//        return allDataList;
//    }


//    public List<byte[]> writeUpteDataToBle63(int hardVersion, int softVersion) {
//
//        int modSize = getModSize(writeContent);
//        while (writeContent.length % 12 != 0) {
//            writeContent = Utils.ByteArrayCopy(writeContent, noneData);
//        }
//
////        writeContent = UIUtils.autoGenericCodeBehind(writeContent, modSize * 24);
//        List<String> listAllData = new ArrayList<>();
//        int allLength = modSize * 24;
//        for (int i = 0; i < allLength; i += 24) {
//            String bagNum = (i / 24 + 1) + "";//包号
//            if ((i / 24 + 1) < 10) {//前10个包 补0
//                bagNum = "0" + bagNum;
//            }
//            bagNum = UIUtils.autoGenericCodeBehind(bagNum, 8);
//            listAllData.add("63" + bagNum + writeContent.substring(i, i + 24));
//        }
//
//
//        List<byte[]> listCrc = new ArrayList<>();
//        for (int i = 0; i < listAllData.size(); i++) {
//            byte[] sbuf = CRC16MP.getSendBuf(listAllData.get(i));
//            byte[] childContent = null;
//            childContent = Utils.ByteArrayCopy(childContent, frameFlag);
//            childContent = Utils.ByteArrayCopy(childContent, sbuf);
//            listCrc.add(childContent);
//        }
//
//        return listCrc;
//    }


    /**
     * 得到分包下发的数量
     *
     * @param mContent
     * @return
     */
    private int getModSize(byte[] mContent) {
        int contentLength = mContent.length;
        int modInt = contentLength % 12;
        int modSize = contentLength /12;
        if (modInt > 0) {
            modSize++;
        }

        return modSize;
    }

}
