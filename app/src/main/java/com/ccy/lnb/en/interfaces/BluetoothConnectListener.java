package com.ccy.lnb.en.interfaces;

import android.widget.TextView;

/**
 * 蓝牙连接断开的接口
 * Created by MJ on 2017/4/17.
 */

public interface BluetoothConnectListener {

    void connectBluetoothOnClick(TextView tvConnect,int position);//连接蓝牙
    void disconnectBluetoothOnClick(TextView tvConnect,int position);//断开蓝牙

}
