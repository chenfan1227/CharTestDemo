package com.ccy.lnb.en.activities.settingAcs;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseActivity;
import com.ccy.lnb.en.utils.DiyToast;
import com.ccy.lnb.en.utils.EmoJiUtil;

import butterknife.Bind;


/**
 * 编辑我的短语界面
 * Created by make chen
 */
public class EditActivity extends BaseActivity {
    @Bind(R.id.edit_editText)
    EditText mEdit;
    @Bind(R.id.edit_button_submit)
    Button mButton;
    private String textContent;


    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_edit);
        pageBack();
        setTopBarTitle(mContext.getString(R.string.add));
    }

    @Override
    protected void initViews() {
        mButton.setOnClickListener(this);
    }

    @Override
    protected void setAllClick() {
        EmoJiUtil.limitWord(mEdit);
    }

    @Override
    protected void process() {
        Intent intent = getIntent();
        textContent = intent.getStringExtra("content");
        mEdit.setText(textContent);
        if (textContent.equals("")){
            setTopBarTitle(mContext.getString(R.string.add_phrase));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button_submit:
                if (mEdit.getText().toString().trim().equals("")){
                    DiyToast.showShort(mContext, getString(R.string.cannot));
                    return;
                }
                Intent intent = new Intent();
                if (textContent.equals("")){
                    intent.putExtra("phrase",mEdit.getText().toString());
                    setResult(0,intent);
                }else {
                    intent.putExtra("phrase",mEdit.getText().toString());
                    setResult(1,intent);
                }

                finish();
                break;
            default:
        }
    }
}
