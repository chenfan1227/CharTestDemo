package com.make.char_im.chenfan.BluetoothOrder;

/**
 * 蓝牙I指令
 */
public class I {
    private static final char ADCommandR = 'I';
    private static final char startCommand = '@';

    public static String getI(int order) {
        int length = 5;

        int[] cmdByteArray = new int[length];

        cmdByteArray[0] = (int) startCommand;
        cmdByteArray[1] = 1;

        cmdByteArray[2] = (int) ADCommandR;

        cmdByteArray[3] = order;
        cmdByteArray[4] = BluetoothSendMessage.CRC8(cmdByteArray);
        return BluetoothSendMessage.getHexString(cmdByteArray);
    }
}
