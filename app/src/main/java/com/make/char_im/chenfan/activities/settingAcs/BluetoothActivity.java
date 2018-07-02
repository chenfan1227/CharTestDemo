package com.make.char_im.chenfan.activities.settingAcs;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.make.char_im.chenfan.BluetoothOrder.BluetoothSendMessage;
import com.make.char_im.chenfan.BluetoothOrder.BluetoothBLE;
import com.make.char_im.chenfan.BluetoothOrder.le.BluetoothLeClass;
import com.make.char_im.chenfan.BluetoothOrder.le.iBeaconClass;
import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.adapters.settingAds.BluetoothAdapter;
import com.make.char_im.chenfan.base.BaseActivity;
import com.make.char_im.chenfan.interfaces.BluetoothConnectListener;
import com.make.char_im.chenfan.utils.BluetoothUtil;
import com.make.char_im.chenfan.utils.Constants;
import com.make.char_im.chenfan.utils.DiyToast;
import com.make.char_im.chenfan.utils.LogUtil;
import com.make.char_im.chenfan.utils.Params;
import com.make.char_im.chenfan.utils.SPStrUtil;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * 蓝牙连接界面
 * Created by chen on 2017/4/14.
 */

public class BluetoothActivity extends BaseActivity implements BluetoothBLE.onBuleStatus, BluetoothConnectListener {

    @BindView(R.id.activity_bluetooth_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.activity_bluetooth_tv_scan)
    TextView mTvScan;
    @BindView(R.id.activity_bluetooth_tv_connect_status)
    TextView mTvConnectStatus;
    @BindView(R.id.activity_bluetooth_bt_search)
    Button mBtSearch;
    @BindView(R.id.activity_bluetooth_bt_auto)
    ToggleButton mBtAuto;

    private BluetoothAdapter mAdapter;
    private ArrayList<iBeaconClass.iBeacon> mBluetoothListBeen = new ArrayList<>();//搜索到的蓝牙设备集合
    private int position = 0;//点击连接的position

    // 搜索BLE终端
    public android.bluetooth.BluetoothAdapter mBluetoothAdapter;
    // 读写BLE终端
    private BluetoothLeClass mBLE;
    private BluetoothBLE ble;
    private boolean isConnectSuccess = false;//判断是否连接成功

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_bluetooth);
        pageBack();
        setTopBarTitle(mContext.getString(R.string.bluetooth));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void initViews() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(manager);
        mAdapter = new BluetoothAdapter(mContext, mBluetoothListBeen);
        mRecycler.setAdapter(mAdapter);

        initBluetooth();
    }

    @Override
    protected void setAllClick() {
        mAdapter.setBluetoothConnectListener(this);
        mBtSearch.setOnClickListener(this);
        autoBtOnClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void process() {
        //是否是自动连接
        if (SPStrUtil.getBooleanPreference(mContext, SPStrUtil.AUTO_CONNECT_BLUETOOTH, false)) {
            mBtAuto.setChecked(true);
            String address = SPStrUtil.getStringPreference(mContext, SPStrUtil.CONNECT_BLUETOOTH_ADDRESS, "");
            if (!address.equals("")) {//如果地址不为空，就自动连接
                mBLE.connect(address);
                connectTimeOut();
            }

        } else {
            mBtAuto.setChecked(false);
        }
        //是否是连接状态
        if (Constants.IS_CONNECT_BLUETOOTH) {
            String name = SPStrUtil.getStringPreference(mContext, SPStrUtil.CONNECT_BLUETOOTH_NAME, "");
            String address = SPStrUtil.getStringPreference(mContext, SPStrUtil.CONNECT_BLUETOOTH_ADDRESS, "");
            mTvConnectStatus.setText(mContext.getString(R.string.bluetooth_connected) + "-" + name);
            iBeaconClass.iBeacon iBeacon = new iBeaconClass.iBeacon();
            iBeacon.name = name;
            iBeacon.bluetoothAddress = address;
            iBeacon.isConnect = true;
            mAdapter.addItemOnly(iBeacon);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_bluetooth_bt_search:
                ble.scanLeDevice(true);//搜索蓝牙设备
                break;
        }

    }

    private void autoBtOnClick() {
        mBtAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPStrUtil.setBooleanPreference(mContext, SPStrUtil.AUTO_CONNECT_BLUETOOTH, true);
                } else {
                    SPStrUtil.setBooleanPreference(mContext, SPStrUtil.AUTO_CONNECT_BLUETOOTH, false);
                }
            }
        });
    }


    /**
     * 蓝牙初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBluetooth() {
        if (!Params.isSDK18()) {
            DiyToast.showShort(mContext, getString(R.string.bluetooth_permission_failed));
            finish();
            return;
        }

        if (!BluetoothUtil.isBluetoothLOCATION(mContext)) {
            DiyToast.showShort(mContext, getString(R.string.bluetooth_no_Connect));
            finish();
            return;
        }

        //初始化BLE
        ble = BluetoothBLE.getBluetooth();
        BluetoothBLE.setContext(this);
        mBluetoothAdapter = ble.getBluetoothAdapter();
        mBLE = ble.getBluetoothLeClass();

        if (mBluetoothAdapter == null || mBLE == null) {
            DiyToast.showShort(mContext, getString(R.string.not_phone_support));
            finish();
            return;
        }
        ble.start();

        ble.setOnBulestatusLiener(this);


    }

    private void connectTimeOut() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                if (!isConnectSuccess) {
                    //5秒钟没有连接成功就关闭连接
                    cancel();
                    mBLE.disconnect();
                    mBLE.close();
                    Constants.IS_CONNECT_BLUETOOTH = false;
                }
            }
        }, 5000);//设置连接超时时间
    }

    //点击连接
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void connectBluetoothOnClick(TextView tvConnect, int position) {
        //先断开其他连接
        mBLE.disconnect();
        mBLE.close();
        loading();
        this.position = position;
        final iBeaconClass.iBeacon device = mBluetoothListBeen.get(position);
        mBLE.connect(device.bluetoothAddress);

        connectTimeOut();
    }

    //点击断开
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void disconnectBluetoothOnClick(TextView tvConnect, int position) {
        mBLE.disconnect();
        mBLE.close();
        tvConnect.setText(mContext.getString(R.string.bluetooth_connect));

        mAdapter.changeConnectStatus(position, false);
        Constants.IS_CONNECT_BLUETOOTH = false;
        mTvConnectStatus.setText(mContext.getString(R.string.bluetooth_none));
        saveConnectInfo("", "");

    }


    @Override
    public void isConnect() {
        //连接成功
        cancel();
        isConnectSuccess = true;
        BluetoothSendMessage.sendI(Constants.SEND_I_STATUS);
        mBluetoothListBeen = (ArrayList<iBeaconClass.iBeacon>) mAdapter.getDataList();
        if (mBluetoothListBeen.size() > 0) {//连接状态和保存
            iBeaconClass.iBeacon iBeacon = mBluetoothListBeen.get(position);
            mTvConnectStatus.setText(mContext.getString(R.string.bluetooth_connected) + "-" + iBeacon.name);
            mAdapter.changeConnectStatus(position, true);
            saveConnectInfo(iBeacon.name, iBeacon.bluetoothAddress);
        }

        //ble.scanLeDevice(false);
        Constants.IS_CONNECT_BLUETOOTH = true;//表示已连接蓝牙
        DiyToast.showShort(mContext, mContext.getString(R.string.bluetooth_connect_success));

    }

    @Override
    public void isNotConnect() {
        Constants.IS_CONNECT_BLUETOOTH = false;
        isConnectSuccess = false;
        mTvConnectStatus.setText(mContext.getString(R.string.bluetooth_none));
        mAdapter.changeConnectStatus(position, false);
        DiyToast.showShort(mContext, mContext.getString(R.string.bluetooth_connect_fail));


    }

    @Override
    public void BluetoothEnable() {
        // DiyToast.showShort(mContext,"BluetoothEnable");
        Constants.IS_CONNECT_BLUETOOTH = false;
        boolean enabled = mBluetoothAdapter.isEnabled();
        if (!enabled){
            isConnectSuccess = false;
            mTvConnectStatus.setText(mContext.getString(R.string.bluetooth_none));
            mAdapter.changeConnectStatus(position, false);
            DiyToast.showShort(mContext, mContext.getString(R.string.bluetooth_connect_fail));
        }

    }

    @Override
    public void timeText(String text) {
        LogUtil.d(TAG,"----TimeText:"+text);
        if ("".equals(text)) {
            mTvScan.setVisibility(View.GONE);
        } else {
            mTvScan.setVisibility(View.VISIBLE);
            mTvScan.setText(text);
        }

    }

    @Override
    public void sendMessageResult(String result) {
//        DiyToast.showShort(mContext,"sendMessageResult:"+result);
    }

    @Override
    public void setBlueSn(String st1, String str2, String str3) {
        //DiyToast.showShort(mContext,"setBlueSn:"+st1);
    }

    @Override
    public void scanLeDeviceResult(iBeaconClass.iBeacon iBeacon) {
        if (null != iBeacon.name)
            //将路怒宝的蓝牙加入列表
            if (iBeacon.name.contains("Lunubao")) {
                mAdapter.addItemOnly(iBeacon);
                int length = mAdapter.getItemCount();
                for (int i = 0; i < length; i++) {
                    String name = mAdapter.getDataList().get(i).name.replace("\r\n", "");
                    String saveName = SPStrUtil.getStringPreference(mContext, SPStrUtil.CONNECT_BLUETOOTH_NAME, "").replace("\r\n", "");
                    if (saveName.contains(name) && Constants.IS_CONNECT_BLUETOOTH) {//连接了并和保存的路怒宝名字相同则就是连接了
                        mAdapter.changeConnectStatus(i, true);
                    }
                }
            }
    }

    /**
     * 保存连接信息
     */
    private void saveConnectInfo(String name, String address) {
        SPStrUtil.setStringPreferences(mContext, SPStrUtil.CONNECT_BLUETOOTH_NAME, name);
        SPStrUtil.setStringPreferences(mContext, SPStrUtil.CONNECT_BLUETOOTH_ADDRESS, address);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ble!=null)
        ble.unRegister();
    }
}
