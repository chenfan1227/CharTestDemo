package com.ccy.lnb.en.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccy.lnb.en.R;


/**
 * 语音dialog
 * Created by Administrator on 2016/4/8.
 */
public class YuYinDialog extends Dialog {

    public static final int SUMBITSEND = 1;//发送语音

    public static final int CANCELSEND = 0;//取消发送

    ImageView imageVoice, imageCancel;

    LinearLayout layoutSubmit;

    TextView textCancel;

    int status = SUMBITSEND;

    public YuYinDialog(Context context) {
        super(context);
    }

    public YuYinDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_speak);
        imageVoice = (ImageView) findViewById(R.id.imagevolice);
        imageCancel = (ImageView) findViewById(R.id.imagecancle);
        layoutSubmit = (LinearLayout) findViewById(R.id.layoutsumbit);
        textCancel = (TextView) findViewById(R.id.textcancle);
    }

    public void setStatus(int status) {
        this.status = status;
        if (CANCELSEND == status) {
            imageCancel.setVisibility(View.VISIBLE);
            layoutSubmit.setVisibility(View.GONE);
            textCancel.setText(R.string.Fingers_canceled);
            textCancel.setTextColor(Color.RED);
        } else {
            imageCancel.setVisibility(View.GONE);
            layoutSubmit.setVisibility(View.VISIBLE);
            textCancel.setText(R.string.Finger_canceled);
            textCancel.setTextColor(Color.WHITE);
        }

    }

    public int getStatus() {
        return status;
    }

    public void setVolice(int voice) {
        if (voice > 0 && voice <= 5) {
            imageVoice.setImageResource(R.mipmap.speak_one);
        } else if (voice > 5 && voice <= 10) {
            imageVoice.setImageResource(R.mipmap.speak_two);
        } else if (voice > 10 && voice <= 15) {
            imageVoice.setImageResource(R.mipmap.speak_three);
        } else if (voice > 15 && voice <= 20) {
            imageVoice.setImageResource(R.mipmap.speak_four);
        } else {
            imageVoice.setImageResource(R.mipmap.speak_five);
        }

    }

}
