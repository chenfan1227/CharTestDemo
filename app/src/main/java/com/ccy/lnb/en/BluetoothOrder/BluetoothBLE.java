package com.ccy.lnb.en.BluetoothOrder;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;


import com.ccy.lnb.en.BluetoothOrder.le.BluetoothLeClass;
import com.ccy.lnb.en.BluetoothOrder.le.iBeaconClass;
import com.ccy.lnb.en.utils.Constants;
import com.ccy.lnb.en.utils.DiyToast;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class BluetoothBLE {
    public static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR3 = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR4 = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR5 = "0000fff5-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static String UUID_CHAR7 = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static String UUID_HERATRATE = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String UUID_TEMPERATURE = "00002a1c-0000-1000-8000-00805f9b34fb";
    public static String UUID_0XFFA6 = "0000ffa6-0000-1000-8000-00805f9b34fb";

    public static BluetoothGattCharacteristic gattCharacteristic_char1 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char5 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_char6 = null;
    public static BluetoothGattCharacteristic gattCharacteristic_heartrate = null;
    public static BluetoothGattCharacteristic gattCharacteristic_keydata = null;
    public static BluetoothGattCharacteristic gattCharacteristic_temperature = null;
    public static BluetoothGattCharacteristic gattCharacteristic_0xffa6 = null;


    public static BluetoothBLE mBuletooth;

    static Context mContext;

    public static BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    public static BluetoothLeClass mBLE;
    private String Address;
    onBuleStatus monBuleStatus;
    Handler TimeHandle;
    boolean isTimeout = true;
    String versionname, versioncode, versionsn;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String m = (String) msg.obj;
            switch (msg.what) {
                case 1:
                    if ("OKF".equals(m)) {
                        DiyToast.showShort(mContext, "ok");
                    }
                    if ("OKE".equals(m)) {
                        DiyToast.showShort(mContext, "ok");
                    }
                    if ("OKG".equals(m)) {
                        DiyToast.showShort(mContext, "ok");
                    }
                    if ("OKL".equals(m)) {
                        Constants.RESULT = Constants.RESULT_OKL;
                        DiyToast.showShort(mContext, "ok");
                        return;
                    } else if (m.contains("OKI")) {// OKI,V1.0.0,2,280000
                        String[] str = m.split(",");

                        if (str.length == 3) {
                            versionname = str[1];
                            versioncode = str[2];
                            if (!isNumeric(versioncode))
                                versioncode = "1";
                            getbuletoothsn();
                        }

                        if (str.length == 2) {
                            versionsn = str[1];
                            Constants.BluetoothSendSN = versionsn;
                            if (monBuleStatus != null)
                                monBuleStatus.setBlueSn(versionname, versionsn, versioncode);
                        }

                    } else {
                        if (monBuleStatus != null)
                            monBuleStatus.sendMessageResult(m);
                    }
                    break;
            }
        }
    };

    public static BluetoothBLE getBluetooth() {
        if (mBuletooth == null)
            mBuletooth = new BluetoothBLE();
        return mBuletooth;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        if (!mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE))
            return null;

        if (mBluetoothAdapter == null)
            mBluetoothAdapter = ((BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (mBluetoothAdapter == null)
            return null;
        return mBluetoothAdapter;
    }

    public void setBuletoothAddress(String address) {
        Address = address;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void start() {
        registerBoradcastReceiver2();
        Intent bindIntent = new Intent(mContext, BlueService.class);
        mContext.startService(bindIntent);
        mBluetoothAdapter.enable();
        // 发现BLE终端的Service时回�?
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);

        // 收到BLE终端数据交互的事�?
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        scanLeDevice(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BluetoothLeClass getBluetoothLeClass() {
        if (mBLE == null)
            mBLE = new BluetoothLeClass(mContext);
        if (!mBLE.initialize()) {
            return null;
        }
        return mBLE;
    }


    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    isTimeout = false;
                    getTimeText("");
                    ((Activity) mContext).invalidateOptionsMenu();
                }
            }, 6000);//搜索6秒
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            isTimeout = true;
            setTime();
            MyThread myThread = new MyThread();
            myThread.start();
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        ((Activity) mContext).invalidateOptionsMenu();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            final iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi,
                    scanRecord);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {

                    if (monBuleStatus != null)
                        monBuleStatus.scanLeDeviceResult(ibeacon);

//
//                    // 发现小米3必须加以下的这3个语句，否则不更新数据，而三星的机子s3则没有这个问题
//                    if (mScanning == true) {
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                        mBluetoothAdapter.startLeScan(mLeScanCallback);
//                    }
                    // 发现小米3必须加以下的这3个语句，否则不更新数据，而三星的机子s3则没有这个问题
                    if (device.getAddress().equals(Address)) {
                        boolean b = mBLE.connect(Address);
                        if (b) {
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            Constants.isBluetoothSendMessage = "1";
//                            if (Constants.IS_CONNECT_BLUETOOTH)
                            DiyToast.showShort(mContext, "Bluetooth is automatically connected successfully");
                            Address = "";
                        }
                    }
                }
            });
        }
    };

    private void registerBoradcastReceiver2() {
        IntentFilter connectedFilter = new IntentFilter();
        connectedFilter.addAction(Constants.CONNECTED_BLUE_TOOTH);
        connectedFilter.addAction(Constants.DISCONNECTED_BLUE_TOOTH);
        connectedFilter.addAction(Constants.SWITCH_STATUS_BLUE_TOOTH);
        mContext.registerReceiver(stateChangeReceiver, connectedFilter);
    }

    private boolean isRegister =false;
    private BroadcastReceiver stateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isRegister=true;
            String action = intent.getAction();
            if (Constants.CONNECTED_BLUE_TOOTH.equals(action)) {
                if (monBuleStatus != null)
                    monBuleStatus.isConnect();
            }
            if (Constants.DISCONNECTED_BLUE_TOOTH.equals(action)) {
                if (monBuleStatus != null)
                    monBuleStatus.isNotConnect();
            }

            if (Constants.SWITCH_STATUS_BLUE_TOOTH.equals(action)) {
                if (monBuleStatus != null)
                    monBuleStatus.BluetoothEnable();
            }
        }
    };

    public void unRegister(){
        if (isRegister)
        mContext.unregisterReceiver(stateChangeReceiver);
    }


    /**
     * 搜索到BLE终端服务的事�?
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }

    };
    /**
     * 收到BLE终端数据交互的事�?
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
        /**
         * BLE终端数据被读的事�?
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            // 执行 mBLE.readCharacteristic(gattCharacteristic); 后就会收到数�? if
            // (status == BluetoothGatt.GATT_SUCCESS)
//
//            char6_display(Utils.bytesToString(characteristic
//                    .getValue()), characteristic.getValue(), characteristic
//                    .getUuid().toString());
        }

        /**
         * 收到BLE终端写入数据回调
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic) {
            Log.e("发送消息回调", "onCharWrite " + gatt.getDevice().getName() + " write "
                    + characteristic.getUuid().toString() + " -> "
                    + new String(characteristic.getValue()));

            Log.e("resutl-BuletoothBle:", new String(characteristic.getValue()));
            String aa = new String(characteristic.getValue());
            Message msg = new Message();
            msg.obj = new String(characteristic.getValue());
            msg.what = 1;
            mHandler.sendMessage(msg);


        }
    };

    public class MyThread extends Thread {
        public void run() {
            while (isTimeout) {

                Message msg = new Message();
                msg.what = 1;
                TimeHandle.sendMessage(msg);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setTime() {
        TimeHandle = new Handler() {
            int count = 0;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {

                    count++;
                    if (count == 0 && isTimeout)
                        getTimeText("Scanning your device.");
                    else if (count == 1 && isTimeout)
                        getTimeText("Scanning your device..");
                    else if (count == 2 && isTimeout)
                        getTimeText("Scanning your device...");
                    else if (count == 3 && isTimeout)
                        getTimeText("Scanning your device.....");
                    else if (count == 4 && isTimeout)
                        getTimeText("Scanning your device.....");
                    else if (count == 5 && isTimeout)
                        getTimeText("Scanning your device.......");
                    else if (count == 6 && isTimeout)
                        getTimeText("Scanning your device.......");
                    else if (isTimeout) {
                        count = 0;
                        getTimeText("Scanning your device........");
                    }
                }
                super.handleMessage(msg);
            }
        };
    }

    public void getTimeText(String ex) {
        if (monBuleStatus != null)
            monBuleStatus.timeText(ex);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        BluetoothGattCharacteristic Characteristic_cur = null;

        for (BluetoothGattService gattService : gattServices) {
            // -----Service的字段信�?----//
            int type = gattService.getType();

            // -----Characteristics的字段信�?----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.e("sssssssssss", "---->char uuid:" + gattCharacteristic.getUuid());
//                getbuletoothversion();
                int permission = gattCharacteristic.getPermissions();

                int property = gattCharacteristic.getProperties();

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                }

                if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR5)) {
                    gattCharacteristic_char5 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR6)) {
                    // 把char1 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_char6 = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                }

                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_HERATRATE)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_heartrate = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的�?�?收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                }

                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_KEY_DATA)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_keydata = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的�?�?收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                }

                if (gattCharacteristic.getUuid().toString()
                        .equals(UUID_TEMPERATURE)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_temperature = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的�?�?收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                }

                if (gattCharacteristic.getUuid().toString().equals(UUID_0XFFA6)) {
                    // 把heartrate 保存起来�?以方便后面读写数据时使用
                    gattCharacteristic_0xffa6 = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                }

                // -----Descriptors的字段信�?----//
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic
                        .getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    int descPermission = gattDescriptor.getPermissions();

                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                    }
                }
            }
        }
//        BluetoothSendMessage.sendI(1);
    }

    private void getbuletoothsn() {
        BluetoothSendMessage.sendI(2);
    }

    public interface onBuleStatus {
        public void isConnect();

        public void isNotConnect();

        public void BluetoothEnable();

        public void timeText(String text);

        public void sendMessageResult(String result);

        public void setBlueSn(String st1, String str2, String str3);

        public void scanLeDeviceResult(iBeaconClass.iBeacon ibeacon);

    }

    public void setOnBulestatusLiener(onBuleStatus on) {
        monBuleStatus = on;
    }

    /**
     * 判断是否为数字
     */
    public boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void close() {
        Intent in = new Intent(mContext, BlueService.class);
        mContext.stopService(in);
        if (mBLE != null)
            mBLE.close();
    }
}
