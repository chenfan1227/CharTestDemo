package com.ccy.lnb.en.activities;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ccy.lnb.en.BluetoothOrder.BluetoothBLE;
import com.ccy.lnb.en.BluetoothOrder.BluetoothSendMessage;
import com.ccy.lnb.en.BluetoothOrder.le.iBeaconClass;
import com.ccy.lnb.en.R;
import com.ccy.lnb.en.adapters.FragTabAdapter;
import com.ccy.lnb.en.base.BaseActivity;
import com.ccy.lnb.en.fragments.PointSendFrag;
import com.ccy.lnb.en.fragments.SettingFrag;
import com.ccy.lnb.en.utils.BluetoothUtil;
import com.ccy.lnb.en.utils.Constants;
import com.ccy.lnb.en.utils.DiyToast;
import com.ccy.lnb.en.utils.Params;
import com.ccy.lnb.en.utils.SPStrUtil;
import com.ccy.lnb.en.utils.SoftKeyBoardListener;
import com.ccy.lnb.en.views.DiyRadioButton;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * 主页mainActivity
 * Created by MJ on 2017/4/10.
 */
public class MainActivity extends BaseActivity implements BluetoothBLE.onBuleStatus {

    @Bind(R.id.activity_main_rb_display)
    DiyRadioButton mTabDisplay;
    @Bind(R.id.activity_main_rg)
    RadioGroup mTabRg;
    @Bind(R.id.activity_main)
    RelativeLayout mRelativeMain;

    private PointSendFrag pointSendFrag;
    private SettingFrag settingFrag;


    FragTabAdapter mFragTabAdapter;//fragmentTab的适配器

    private ArrayList<Fragment> mTabFrags = new ArrayList<>();//tabFragment的集合

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void initViews() {
        swipeBackLayout.setEnablePullToBack(false);
        pointSendFrag = new PointSendFrag();

        settingFrag = new SettingFrag();
        mTabFrags.add(pointSendFrag);
        mTabFrags.add(settingFrag);
        mTabDisplay.setChecked(true);
        mFragTabAdapter = new FragTabAdapter(this, mTabFrags, R.id.activity_main_frame,
                mTabRg, false);
        initAutoConBluetooth();
    }

    @Override
    protected void setAllClick() {
        softIsHide();
    }


    @Override
    protected void process() {
        reqPermission();
    }

    @Override
    public void onClick(View view) {

    }

    private void softIsHide() {
        SoftKeyBoardListener.setListener((Activity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mTabRg.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                mTabRg.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (pointSendFrag == null && fragment instanceof PointSendFrag) {
            pointSendFrag = (PointSendFrag) fragment;
        } else if (settingFrag == null && fragment instanceof SettingFrag) {
            settingFrag = (SettingFrag) fragment;
        }
    }


    /**
     * 申请权限
     */
    private void reqPermission() {
        if (SPStrUtil.getBooleanPreference(mContext, Constants.IS_PERMISSION, true) && Params.isSDK23()) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Constants.CAMERA}, 1);
        } else {
            SPStrUtil.setBooleanPreference(mContext, Constants.IS_PERMISSION, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Constants.LOCATION1, Constants.LOCATION2}, 2);
                break;
            case 2:
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Constants.SAY}, 3);
                break;
            case 3:
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Constants.SAVE1, Constants.SAVE2, Constants.SAVE3}, 7);
                break;
            case 7:
                SPStrUtil.setBooleanPreference(mContext, Constants.IS_PERMISSION, false);
                break;
        }
    }

    /**
     * 初始化自动连接蓝牙
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initAutoConBluetooth() {
        if (Params.isSDK18()) {
            final BluetoothBLE b = BluetoothBLE.getBluetooth();
            BluetoothBLE.setContext(mContext);
            if (BluetoothUtil.isBluetoothLOCATION(mContext)) {
                if (SPStrUtil.getBooleanPreference(mContext, SPStrUtil.AUTO_CONNECT_BLUETOOTH, false)) {
                    String address = SPStrUtil.getStringPreference(mContext, SPStrUtil.CONNECT_BLUETOOTH_ADDRESS, "");
                    if (!"".equals(address)) {
                        if (null != b.getBluetoothAdapter() && null != b.getBluetoothLeClass()) {
                            b.setBuletoothAddress(address);
                            b.start();
                            b.setOnBulestatusLiener(this);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void isConnect() {
        Constants.IS_CONNECT_BLUETOOTH = true;//表示已连接蓝牙
        BluetoothSendMessage.sendI(Constants.SEND_I_STATUS);
    }

    @Override
    public void isNotConnect() {
        Constants.IS_CONNECT_BLUETOOTH = false;
        BluetoothSendMessage.sendI(Constants.SEND_I_STATUS);
        DiyToast.showShort(mContext, mContext.getString(R.string.bluetooth_connect_fail));
    }

    @Override
    public void BluetoothEnable() {
    }

    @Override
    public void timeText(String text) {
    }

    @Override
    public void sendMessageResult(String result) {
    }

    @Override
    public void setBlueSn(String st1, String str2, String str3) {
    }

    @Override
    public void scanLeDeviceResult(iBeaconClass.iBeacon iBeacon) {
    }


    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                DiyToast.showShort(mContext, "Press again to exit the program");
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                //取消注册蓝牙广播
                /*if (BluetoothUtil.isBluetoothLOCATION(mContext)) {
                    if (SPStrUtil.getBooleanPreference(mContext, SPStrUtil.AUTO_CONNECT_BLUETOOTH, false)) {
                        String address = SPStrUtil.getStringPreference(mContext, SPStrUtil.CONNECT_BLUETOOTH_ADDRESS, "");
                        if (!"".equals(address)) {
                            BluetoothBLE.getBluetooth().unRegister();
                        }
                    }
                }*/
                BluetoothBLE.getBluetooth().close();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
