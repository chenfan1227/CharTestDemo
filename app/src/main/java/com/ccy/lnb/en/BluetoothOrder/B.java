package com.ccy.lnb.en.BluetoothOrder;

import com.ccy.lnb.en.utils.StringUtil;

import java.io.UnsupportedEncodingException;

/**
 * 蓝牙B指令
 */
public class B {
    private static final char ADCommandB = 'B';
    private static final char ADCommandC = 'C';
    private static final char startCommand = '@';

    public static String getB(String content) {
        String command = "";
        try {
            int length = StringUtil.gbkStr(content) + 4;
            int[] cmdByteArray = new int[length];

            cmdByteArray[0] = (int) startCommand;
            cmdByteArray[1] = StringUtil.gbkStr(content);
            cmdByteArray[2] = (int) ADCommandB;

            byte[] tempContent = content.getBytes("GBK");
            int[] intContent = transfer(tempContent);
            int contentLength = tempContent.length;
            System.arraycopy(intContent, 0, cmdByteArray, 3, contentLength);

            cmdByteArray[length - 1] = BluetoothSendMessage.CRC8(cmdByteArray);
            command = BluetoothSendMessage.getHexString(cmdByteArray);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return command;
    }

    public static String getC(String content) {
        String command = "";
        try {
            int length = StringUtil.gbkStr(content) + 4;
            int[] cmdByteArray = new int[length];

            cmdByteArray[0] = (int) startCommand;
            cmdByteArray[1] = StringUtil.gbkStr(content);
            cmdByteArray[2] = (int) ADCommandC;

            byte[] tempContent = content.getBytes("GBK");
            int[] intContent = transfer(tempContent);
            int contentLength = tempContent.length;
            System.arraycopy(intContent, 0, cmdByteArray, 3, contentLength);

            cmdByteArray[length - 1] = BluetoothSendMessage.CRC8(cmdByteArray);
            command = BluetoothSendMessage.getHexString(cmdByteArray);
        } catch (UnsupportedEncodingException e) {
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
