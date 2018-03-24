package com.ccy.lnb.en.BluetoothOrder;

/**
 * 蓝牙G指令
 */
public class G {
    private static final char ADCommandR = 'G';
    protected static final char startCommand = '@';


    public static String getG(int fonts, int scroll, int speed) {

        int length = 8;


        int[] cmdByteArray = new int[length];

        cmdByteArray[0] = (int) startCommand;

        cmdByteArray[1] = 04;

        cmdByteArray[2] = (int) ADCommandR;
        //字体
        cmdByteArray[3] = fonts;
        //滚动方式
        cmdByteArray[4] = scroll;
        //滚动速度
        cmdByteArray[5] = speed;
        cmdByteArray[6] = 0;

        cmdByteArray[7] = BluetoothSendMessage.CRC8(cmdByteArray);

        return BluetoothSendMessage.getHexString(cmdByteArray);
    }

}
