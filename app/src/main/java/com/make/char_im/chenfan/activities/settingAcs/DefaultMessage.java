package com.make.char_im.chenfan.activities.settingAcs;


import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.make.char_im.chenfan.BluetoothOrder.BluetoothSendMessage;
import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseActivity;
import com.make.char_im.chenfan.utils.Constants;
import com.make.char_im.chenfan.utils.DiyToast;
import com.make.char_im.chenfan.utils.EmoJiUtil;
import com.make.char_im.chenfan.utils.SPStrUtil;

import butterknife.BindView;


/**
 * 编辑保存界面EditActivity
 * Created by chen on 2017/4/10.
 */
public class DefaultMessage extends BaseActivity {

    @BindView(R.id.default_button_save)
    Button mSave;
    @BindView(R.id.default_editText)
    EditText mEdit;
    @BindView(R.id.default_linearLayout)
    LinearLayout mLinearLayout;


    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_default_message);
        pageBack();
        setTopBarTitle(mContext.getString(R.string.setting_default_mess));
    }

    @Override
    protected void initViews() {

        String message = SPStrUtil.getStringPreference(mContext, SPStrUtil.DEFAULT_MESS, "");
        mEdit.setText(message);
        mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.APP_DEFAULT)});
    }

    @Override
    protected void setAllClick() {
        mSave.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mLinearLayout.setOnClickListener(this);
        EmoJiUtil.limitWord(mEdit);
    }

    @Override
    protected void process() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_button_save:
                if (mEdit.getText().toString().trim().equals("")) {
                    DiyToast.showShort(mContext, getString(R.string.cannot));
                    return;
                }
                String mMessage;
                mMessage = mEdit.getText().toString();
                //判断输入的文字是不是为空
                if (mMessage.length() == 0) {
                    DiyToast.showShort(mContext, getString(R.string.cannot));
                    return;
                }
                //保存编辑的字 并保存在数据库里面
                SPStrUtil.setStringPreferences(mContext, SPStrUtil.DEFAULT_MESS, mMessage);
                Intent intent = new Intent();
                //传一个key值并在上一个activity里面获取当前值
                intent.putExtra("defaultMess", mEdit.getText().toString());
                setResult(0, intent);
                if (Constants.IS_CONNECT_BLUETOOTH) {
                    BluetoothSendMessage.sendE(mMessage);
                } else {
                    DiyToast.showShort(mContext, getString(R.string.bluetooth_no_Connect));
                }
                finish();
                break;
            case R.id.default_editText:
//                mEdit.setFilters(new InputFilter[]{filter});
                break;
            case R.id.default_linearLayout:
                mEdit.setFilters(new InputFilter[]{filter});
                break;


            default:
        }
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) return "";
            else return null;
        }
    };


}
