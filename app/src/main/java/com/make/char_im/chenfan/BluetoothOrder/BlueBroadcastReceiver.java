package com.make.char_im.chenfan.BluetoothOrder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.make.char_im.chenfan.utils.Constants;
import com.make.char_im.chenfan.utils.LogUtil;

/**
 * 蓝牙广播接收
 */
public class BlueBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            LogUtil.e("BlueBroadcastReceiver", "连接");
            Intent in = new Intent(Constants.CONNECTED_BLUE_TOOTH);
            context.sendBroadcast(in);

        }
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            LogUtil.e("BlueBroadcastReceiver", "断开");
            Intent in = new Intent(Constants.DISCONNECTED_BLUE_TOOTH);
            context.sendBroadcast(in);
        }

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            LogUtil.e("BlueBroadcastReceiver", "开关状态改变");
            Intent in = new Intent(Constants.SWITCH_STATUS_BLUE_TOOTH);
            context.sendBroadcast(in);
        }
    }
}
