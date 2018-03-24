package com.ccy.lnb.en.BluetoothOrder;

/**
 * 蓝牙F指令
 */
public class F {
    private static final char ADCommandR = 'F';
    private static final char startCommand = '@';

    public static String getF(int progress) {
        String command = "";
        int length = 5;
        int[] cmdByteArray = new int[length];

        cmdByteArray[0] = (int) startCommand;
        cmdByteArray[1] = 1;
        cmdByteArray[2] = (int) ADCommandR;

        cmdByteArray[3] = progress;

        cmdByteArray[4] = BluetoothSendMessage.CRC8(cmdByteArray);
        command = BluetoothSendMessage.getHexString(cmdByteArray);
        return command;
    }
}
