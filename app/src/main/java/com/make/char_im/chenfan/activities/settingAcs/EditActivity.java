package com.make.char_im.chenfan.activities.settingAcs;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseActivity;
import com.make.char_im.chenfan.utils.DiyToast;
import com.make.char_im.chenfan.utils.EmoJiUtil;

import butterknife.BindView;


/**
 * 编辑我的短语界面
 * Created by make chen
 */
public class EditActivity extends BaseActivity {
    @BindView(R.id.edit_editText)
    EditText mEdit;
    @BindView(R.id.edit_button_submit)
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
