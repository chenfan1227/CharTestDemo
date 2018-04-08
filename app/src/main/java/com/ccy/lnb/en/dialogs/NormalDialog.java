package com.ccy.lnb.en.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ccy.lnb.en.R;


public class NormalDialog extends Dialog {

    Context mContext;
    OnSureClick on;
    private TextView cancel;
    private TextView sure;
    private TextView Title;
    private TextView content;

    public NormalDialog(Context context, int theme, OnSureClick on) {
        super(context, theme);
        mContext = context;
        this.on = on;
    }

    public TextView getCancel() {
        return cancel;
    }

    public NormalDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_dialog);


        Title = (TextView) findViewById(R.id.normal_dialog_tv_title);
        content = (TextView) findViewById(R.id.normal_dialog_tv_content);
        cancel = (TextView) findViewById(R.id.normal_dialog_tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();

            }
        });
        sure = (TextView) findViewById(R.id.normal_dialog_tv_sure);
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
                on.setOnClick();
            }
        });
    }

    // 设置标题
    public void setTitle(String title) {
        Title.setText(title);
    }

    // 设置内容
    public void setContent(String c) {
        content.setText(c);
    }

    // 确认
    public void setSure(String c) {
        sure.setText(c);
    }

    public interface OnSureClick {
        public void setOnClick();
    }
}
