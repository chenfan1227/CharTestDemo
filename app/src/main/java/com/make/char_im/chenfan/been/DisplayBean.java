package com.make.char_im.chenfan.been;

/**
 * 点发显示实体类
 * Created by MJ on 2017/4/13.
 */

public class DisplayBean {

    private String sendTime;//发送的时间
    private String showTimeLong;//显示的时长（12s或者循环）
    private String message;

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getShowTimeLong() {
        return showTimeLong;
    }

    public void setShowTimeLong(String showTimeLong) {
        this.showTimeLong = showTimeLong;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
