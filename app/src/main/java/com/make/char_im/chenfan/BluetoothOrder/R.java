package com.make.char_im.chenfan.BluetoothOrder;

/**
 * 蓝牙R指令
 */
public class R {
    private static final char ADCommandR = 'R';
    protected static final char startCommand = '@';

    public static String getRByte(int o, int length, byte[] b) {
        int len = length + 6;

        int[] cmdByteArray = new int[len];

        cmdByteArray[0] = (int) startCommand;
        cmdByteArray[1] = length + 2;

        cmdByteArray[2] = (int) ADCommandR;

        if (o > 255) {
            int byteFirst = o / 256;
            int byteSecond = o % 256;
            cmdByteArray[3] = ((byte) byteFirst) & 0xff;
            cmdByteArray[4] = ((byte) byteSecond) & 0xff;
        } else {
            cmdByteArray[3] = (byte) 0;
            cmdByteArray[4] = ((byte) o) & 0xff;
        }

        for (int i = 0; i < b.length; i++) {
            cmdByteArray[i + 5] = b[i] & 0xff;
        }
        cmdByteArray[length + 5] = BluetoothSendMessage.CRC8(cmdByteArray);

        return BluetoothSendMessage.getHexString(cmdByteArray);
    }
}

