package com.ccy.lnb.en.BluetoothOrder;

import com.ccy.lnb.en.utils.StringUtil;

import java.io.UnsupportedEncodingException;

/**
 * 蓝牙L指令
 */
public class L {
    protected static final char startCommand = '@';
    private static final char ADCommand = 'L';

    public static String get_L(String message, int status, int time) {

        String command = "";
        int len;
        try {

            len = StringUtil.gbkStr(message);

            int dataLength = len + 7;
            // byte[] cmdByteArray = new byte[dataLength]; // @ Length L 00 24
            // 浣�
            // 濂� 1 CRC
            int[] cmdByteArray = new int[dataLength];

            cmdByteArray[0] = (int) startCommand;
            cmdByteArray[1] = 3 + len;
            cmdByteArray[2] = (int) ADCommand;

            // 杞崲鏃堕暱
            if (time > 256) {
                int byteFirst = time / 256;
                int byteSecond = time % 256;
                cmdByteArray[3] = ((byte) byteFirst) & 0xff;
                cmdByteArray[4] = ((byte) byteSecond) & 0xff;
            } else {
                cmdByteArray[3] = (byte) 0;
                cmdByteArray[4] = ((byte) time) & 0xff;
            }
            cmdByteArray[5] = ((byte) status) & 0xff;

            // 杞崲骞垮憡鍐呭

            byte[] tempContent = message.getBytes("GBK");

            int[] intContent = transfer(tempContent);

            int contentLength = tempContent.length;
            System.arraycopy(intContent, 0, cmdByteArray, 6, contentLength);

            cmdByteArray[contentLength + 6] = BluetoothSendMessage.CRC8(cmdByteArray);

            command = BluetoothSendMessage.getHexString(cmdByteArray);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return command;
    }

    private static int[] transfer(byte[] tempContent) {
        // TODO Auto-generated method stub
        int[] result = new int[tempContent.length];
        for (int i = 0; i < tempContent.length; i++) {
            result[i] = tempContent[i] & 0xff;
        }
        return result;
    }
}
