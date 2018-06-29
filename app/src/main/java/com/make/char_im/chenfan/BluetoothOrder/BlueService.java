package com.make.char_im.chenfan.BluetoothOrder;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


/**
 * 蓝牙服务
 */
public class BlueService extends Service {

    public BlueBroadcastReceiver mReceiver = new BlueBroadcastReceiver();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter connectedFilter = new IntentFilter();
        connectedFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        connectedFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        connectedFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, connectedFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
