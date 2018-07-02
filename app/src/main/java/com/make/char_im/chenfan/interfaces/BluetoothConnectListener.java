package com.make.char_im.chenfan.interfaces;

import android.widget.TextView;
/**
 * 作者：Administrator on 2018/7/2 0002 10:22
 * 功能： 蓝牙连接断开的接口
 * 作者：chenfan
 */

public interface BluetoothConnectListener {

    void connectBluetoothOnClick(TextView tvConnect,int position);//连接蓝牙
    void disconnectBluetoothOnClick(TextView tvConnect,int position);//断开蓝牙

}
