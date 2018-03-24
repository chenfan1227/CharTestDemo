package com.ccy.lnb.en.been;

/**
 * 蓝牙设备列表实体类
 * Created by MJ on 2017/4/14.
 */

public class BluetoothListBean {

    private String name;
    private boolean isConnect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
