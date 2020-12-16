package com.tendency.charge.data;

import android.text.TextUtils;

import com.parry.utils.code.SPUtils;

import com.tendency.charge.constants.BaseConstants;

import com.tendency.charge.utils.CRC16MP;
import com.tendency.charge.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙数据包处理类
 * Created by Linus_Xie on 2016/10/25.
 */

public class PacketData {

    public PacketData() {
    }

    public byte[] frameFlag = {(byte) 0xAA};
    public static byte[] frameFlagStatic = {(byte) 0xAA};
    //命令字
    public byte[] queryKeyCommand = {(byte) 0x01};//查询设备配置key
    public byte[] sendKeyCommand = {(byte) 0x02};//下发配置key
    public byte[] deleteOnePlateNumberCommand = {(byte) 0x03};//删除一个车牌
    public byte[] deleteAll = {(byte) 0x04};//清空所有车牌
    public byte[] controlCommand = {(byte) 0x05};//控制报警
    public byte[] quitLocked = {(byte) 0x06};//退出锁定状态
    public byte[] queryKeyVersion = {(byte) 0x07};//查询设备版本号

    public byte[] getDeviceTime = {(byte) 0x3f};//打包开始下发
    public byte[] setDeviceTime = {(byte) 0x40};//打包开始下发
    public byte[] queryCar = {(byte) 0x41};//打包开始下发
    public byte[] getGatewayDate = {(byte) 0x42};//打包开始下发
    public byte[] setGatewayTime = {(byte) 0x43};//打包开始下发
    public byte[] GatewayDate = {(byte) 0x44};//打包开始下发
    public byte[] GatewayLowPower = {(byte) 0x45};//打包开始下发

    public byte[] packagingStart = {(byte) 0x51};//打包开始下发
    public byte[] packagingComplete = {(byte) 0x52};//打包下发完成
    public byte[] packagingSend = {(byte) 0x53};//打包下发内容

    public byte[] updateStart = {(byte) 0x61};//更新包开始下发
    public byte[] updateComplete = {(byte) 0x62};//更新包下发完成
    public byte[] updateSend = {(byte) 0x63};//更新包下发内容

    public byte[] aerialSignal = {(byte) 0x71};//空中信号开关

    public byte[] uploadState = {(byte) 0x80};//上传搜索枪状态
    public byte[] uploadHeartBeat = {(byte) 0x81};//透传标签心跳
    public byte[] uploadStateSecond = {(byte) 0x82};//上传搜索枪状态2

    public byte[] uploadaerialSignal = {(byte) 0x83};//上传空中信号值

    public byte[] noneData = {(byte) 0x00};

    public byte[] Update840PD2 = {(byte) 0x24};//更新频点


    //================================打包蓝牙数据===============================================

    public static String detectNB(String nbNum) {
        String pcCode = SPUtils.getInstance().getString(BaseConstants.ble_pcCode, "");
        String numType = nbNum.substring(0, 4);
        String numContent = nbNum.substring(4, nbNum.length());

        numType = UIUtils.highAndLowChange(numType);

        numContent = UIUtils.void10to16(numContent);
        numContent = UIUtils.highAndLowChange(numContent);


        String dataContent = "1f" + "18" + "00" + pcCode + "00000000000000000000" + numType + numContent;
//        dataContent = UIUtils.autoGenericCodeBehind(dataContent,34);
//
//        byte[] lastByte = CRC16MP.getSendBuf(dataContent);
//
//        requestData =UIUtils.ByteArrayCopy(requestData, frameFlag);
//        requestData =UIUtils.ByteArrayCopy(requestData, lastByte);

        return dataContent;
    }

    /*Frame Flag	SN号/中继编号	设备类型	设备ID	命令字	内容	CRC校验
          1 Byte	低5bit/高3bit	2Byte	4 Byte	1 Byte	5Byte	2Byte
            0xAA	0x00	设备类型（0x8021）	电池标签	0x02	设备ID（00 00 00 01）NB标签	低位	高位
*/
    public static byte[] cellLabel(String cellbNum, String nbNum, String type) {
        try {
            if (!TextUtils.isEmpty(cellbNum) && !TextUtils.isEmpty(nbNum)) {
                String labelType = cellbNum.substring(0, 4);
                String labelContent = cellbNum.substring(4, cellbNum.length());
                labelContent = UIUtils.void10to16(labelContent);
                labelContent = UIUtils.autoGenericCode(labelContent, 8);


                String nbContent = nbNum.substring(4, nbNum.length());
                nbContent = UIUtils.setEvenNum(UIUtils.void10to16(nbContent));
                nbContent = UIUtils.autoGenericCode(nbContent, 8);
                nbContent = UIUtils.autoGenericCodeBehind(nbContent, 10);

                String cellAndNb = "00" + labelType + labelContent + type + nbContent;

                byte[] sbuf = CRC16MP.getSendBuf(cellAndNb);
                String allCrcStr = CRC16MP.getBufHexStr(sbuf);
                allCrcStr = "c3" + "aa" + allCrcStr;
                byte[] sbufLast = CRC16MP.getSendBuf(allCrcStr);


                byte[] childContent = null;
                childContent = UIUtils.ByteArrayCopy(childContent, frameFlagStatic);
                childContent = UIUtils.ByteArrayCopy(childContent, sbufLast);
                return childContent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * @return
     */
    public static byte[] writeBlackCarDataToBle51or52(String nbNum, String sendOrder) {
        String writeData = detectNB(nbNum);
        String allLength;

        allLength = "24000000";

        byte[] sbuf = CRC16MP.getSendBuf(UIUtils.autoGenericCodeBehind(writeData, 72));
        String allCrcStr = CRC16MP.getBufHexStr(sbuf);
        allCrcStr = allCrcStr.substring(allCrcStr.length() - 4, allCrcStr.length());//得到总的crc
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sendOrder);//命令字
        stringBuffer.append(allLength);//内容大小
        stringBuffer.append("03000000");//总包
        stringBuffer.append(allCrcStr);
        byte[] lastByte = CRC16MP.getSendBuf(UIUtils.autoGenericCodeBehind(stringBuffer.toString(), 34));
        byte[] childContent = null;
        childContent = UIUtils.ByteArrayCopy(childContent, frameFlagStatic);
        childContent = UIUtils.ByteArrayCopy(childContent, lastByte);

        return childContent;
    }

    public static List<byte[]> writeBlackCarDataToBle53(String nbNum) {
        String allData = detectNB(nbNum);
        allData = UIUtils.autoGenericCodeBehind(allData, 72);
        List<String> listAllData = new ArrayList<>();
        for (int i = 0; i < 72; i += 24) {
            listAllData.add("530" + (i / 24 + 1) + "000000" + allData.substring(i, i + 24));
        }


        List<byte[]> listCrc = new ArrayList<>();
        for (int i = 0; i < listAllData.size(); i++) {
            byte[] sbuf = CRC16MP.getSendBuf(listAllData.get(i));
            byte[] childContent = null;
            childContent = UIUtils.ByteArrayCopy(childContent, frameFlagStatic);
            childContent = UIUtils.ByteArrayCopy(childContent, sbuf);
            listCrc.add(childContent);
        }

        return listCrc;
    }

    /**
     * 下发09数据
     *
     * @param nbData nb内容
     * @return
     */
    public static byte[] writeDataToBle09(String nbData) {
        /*1、	AA    01+900000000000+09+4个字节（NB标签的ID）+01    +CRC校验*/
        String nbContent = nbData.substring(4, nbData.length());
        nbContent = UIUtils.setEvenNum(UIUtils.void10to16(nbContent));
        nbContent = UIUtils.autoGenericCode(nbContent, 8);
        String firstCrcData = "01" + "900000000000" + "09" + nbContent + "01";
        byte[] firstSbuf = CRC16MP.getSendBuf(firstCrcData);
        String firstCrcStr = CRC16MP.getBufHexStr(firstSbuf);
        firstCrcStr = firstCrcStr.substring(firstCrcStr.length() - 4, firstCrcStr.length());//第一次crc
        firstCrcData = "aa" + firstCrcData + firstCrcStr;
        /*2、	在上述数据之前加上C3,得到C3+ AA01+900000000000+09+4个字节（NB标签的ID）+01+CRC校验*/
        String allData = UIUtils.autoGenericCodeBehind("c3" + firstCrcData, 34);
        byte[] allsbuf = CRC16MP.getSendBuf(allData);
        byte[] childContent = null;
        childContent = UIUtils.ByteArrayCopy(childContent, frameFlagStatic);
        childContent = UIUtils.ByteArrayCopy(childContent, allsbuf);
        String ss = com.clj.fastble.utils.HexUtil.formatHexString(childContent, false);
        return childContent;
    }

    /**
     * 下发07数据 获取版本
     *
     * @param
     * @return
     */
    public static byte[] writeDataToBle07() {

        String allData = UIUtils.autoGenericCodeBehind("07", 34);
        byte[] allsbuf = CRC16MP.getSendBuf(allData);
        byte[] childContent = null;
        childContent = UIUtils.ByteArrayCopy(childContent, frameFlagStatic);
        childContent = UIUtils.ByteArrayCopy(childContent, allsbuf);
        String ss = com.clj.fastble.utils.HexUtil.formatHexString(childContent, false);
        return childContent;
    }


}
