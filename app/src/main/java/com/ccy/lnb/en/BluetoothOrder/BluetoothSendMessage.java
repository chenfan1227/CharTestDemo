package com.ccy.lnb.en.BluetoothOrder;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.ccy.lnb.en.BluetoothOrder.le.Utils;

import java.io.FileInputStream;

/**
 * 蓝牙发送消息
 */
public class BluetoothSendMessage {

    //发送L指令
    public static void sendMessage(String message, int status, String playstatus, int time) {

        if ("0".equals(playstatus)) {//如果是循环播放 时间传0
            time = 1;
        }
        String s1;
        if (0 == status) {//49十进制为提醒双闪
            s1 = L.get_L(message, 49, time);
        } else {//48十进制为不提醒双闪
            s1 = L.get_L(message, 48, time);
        }
        sendOrder(s1);
    }


    //发送R指令  index为发送编号  length为固件内容的长度  b为固件内容
    public static void sendR(int index, int length, byte[] b) {
        String s1 = R.getRByte(index, length, b);
        Log.e("index", "index=" + index + ",length=" + length + ",s1=" + s1);
            Log.e("FaiLFaiLFaiL1",s1);
        sendOrder(s1);
    }

    //发送I指令
    public static void sendI(int l) {
        String s1 = I.getI(l);
        Log.e("I1", s1);
        sendOrder(s1);
    }

    //发送E指令
    public static void sendE(String l) {
        sendOrder(E.getE(l));
    }

    //发送F指令
    public static void sendF(int l) {
        sendOrder(F.getF(l));
    }

    public static void sendBC(String tixing, String ganxie) {
        sendOrder(B.getB(tixing));
        sendOrder(B.getC(ganxie));
    }

    //发送G指令
    public static void sendG(int fonts, int scroll, int speed) {
        String sG = G.getG(fonts, scroll, speed);
        sendOrder(sG);
    }

    //发送命令
    public static void sendOrder(String orderStr) {
        boolean hex_flag = true;
        for (int i = 0; i < orderStr.length(); i++) {
            char charV = orderStr.charAt(i);
            if ((charV >= '0' && charV <= '9')
                    || (charV >= 'a' && charV <= 'f')
                    || (charV >= 'A' && charV <= 'F')) {
            } else {
                hex_flag = false;
            }
        }
        if (hex_flag) {
            if (0 == orderStr.length() % 2) {
                byte bytes[] = Utils.hexStringToBytes(orderStr);

                // 分包发送 每包最大18个字节
                sendBytes(bytes);
            }
        }
    }


    public static void sendBytes(byte bytes[]) {
        // 分包发送 每包最大18个字节
        int total_len = bytes.length;

        int cur_pos = 0;
        int i = 0;
        while (cur_pos < total_len) {
            int lenSub = 0;
            if (total_len - cur_pos > 18)
                lenSub = 18;
            else
                lenSub = total_len - cur_pos;

            byte[] bytes_sub = new byte[lenSub];

            for (i = 0; i < lenSub; i++) {
                bytes_sub[i] = bytes[cur_pos + i];
            }
            cur_pos += lenSub;
            writeChar6_in_bytes(bytes_sub);

            // 延时 一会
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
    }

    // 字节发送
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static public void writeChar6_in_bytes(byte bytes[]) {
        // byte[] writeValue = new byte[1];
        if (BluetoothBLE.gattCharacteristic_char6 != null) {
            boolean bRet = BluetoothBLE.gattCharacteristic_char6.setValue(bytes);
            if (null != BluetoothBLE.mBLE)
                BluetoothBLE.mBLE.writeCharacteristic(BluetoothBLE.gattCharacteristic_char6);
        }
    }

    public static byte[] getFile(String fileName) {
        try {
            FileInputStream fin = new FileInputStream(fileName);


            int length = fin.available();

            byte[] buffer = new byte[length];

            fin.read(buffer);
            Log.e("length2", "" + buffer + "," + buffer.length + "," + buffer[0]);
            fin.close();
            return buffer;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }


    //转为16进制
    protected static String getHexString(int[] b) {
        String hexs = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i]);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexs = hexs + hex;
        }
        return hexs;
    }

    protected static int CRC8(int[] buffer) {
        int crc = 0;
        for (int j = 1; j < buffer.length - 1; j++) {
            crc ^= buffer[j];
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x01) != 0) {
                    crc >>= 1;// 鍙樹负3
                    crc ^= 0x8c;
                } else {
                    crc >>= 1;// 鍙樹负3
                }
            }
        }
        return crc;
    }
}
