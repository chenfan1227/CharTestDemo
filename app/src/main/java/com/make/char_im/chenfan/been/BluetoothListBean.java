package com.make.char_im.chenfan.been;

/**
 * 蓝牙设备列表实体类
 * Created by chen on 2017/4/14.
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
