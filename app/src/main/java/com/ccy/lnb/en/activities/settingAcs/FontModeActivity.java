package com.ccy.lnb.en.activities.settingAcs;


/**
 * 显示设置（滚动方式,滚动速度）
 * Created by make chen
 */

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ccy.lnb.en.BluetoothOrder.BluetoothSendMessage;
import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseActivity;
import com.ccy.lnb.en.utils.Constants;
import com.ccy.lnb.en.utils.DiyToast;
import com.ccy.lnb.en.utils.SPStrUtil;

import butterknife.Bind;

public class FontModeActivity extends BaseActivity {
    @Bind(R.id.radioButton_up)
    RadioButton mUp;
    @Bind(R.id.radioButton_fast)
    RadioButton mFast;
    @Bind(R.id.radioButton_left)
    RadioButton mLeft;
    @Bind(R.id.radioButton_mid)
    RadioButton mMid;
    @Bind(R.id.radioButton_slow)
    RadioButton mSlow;
    @Bind(R.id.radioButton_Direction)
    RadioGroup mDirection;
    @Bind(R.id.radioButton_speed)
    RadioGroup mSpeed;
    @Bind(R.id.FontMode_button_save)
    Button mSave;
    String mName = "0", scroll = "", speed = "";
    String scrollOne = "", speedone = "", scrollTwo, speedTwo, speedThere;

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_font_mode);
        pageBack();
        setTopBarTitle(mContext.getString(R.string.setting_model));
    }

    @Override
    protected void initViews() {
/**
 * 从数据库拿到值进来根据数据的值并显示出来
 * */
        String direction = SPStrUtil.getStringPreference(mContext, SPStrUtil.DISPLAY_DIRECTION, mLeft.getText().toString());
        //判断拉到值并给按钮赋值并设置是否是之前选中状态
        if (direction.equals(mLeft.getText().toString())) {
            mLeft.setChecked(true);
        } else {
            mUp.setChecked(true);
        }
        //判断拉到值并给按钮赋值并设置是否是之前选中状态
        String speed = SPStrUtil.getStringPreference(mContext, SPStrUtil.SWITCH_SPEED, mFast.getText().toString());
        if (speed.equals(mFast.getText().toString())) {
            mFast.setChecked(true);

        } else {
            if (speed.equals(mMid.getText().toString())) {
                mMid.setChecked(true);
            } else {
                mSlow.setChecked(true);
            }
        }

    }

    @Override
    protected void setAllClick() {
        mSave.setOnClickListener(this);


    }

    @Override
    protected void process() {

    }

    private void setFontSuccess() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                BluetoothSendMessage.sendMessage("Setting is OK", 1, "1", 3);
            }
        }, 100);//设置连接超时时间
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 传点击事件的值到数据库
             * */
            case R.id.FontMode_button_save:
                //速度跟滚动方式
                if (mUp.isChecked()) {
                    speed = mUp.getText().toString();
                } else {
                    speed = mLeft.getText().toString();
                }
                SPStrUtil.setStringPreferences(mContext, SPStrUtil.DISPLAY_DIRECTION, speed);
                if (mFast.isChecked()) {
                    scroll = mFast.getText().toString();
                } else {
                    if (mSlow.isChecked()) {
                        scroll = mSlow.getText().toString();
                    } else {
                        scroll = mMid.getText().toString();
                    }

                }
                //把选中的状态传递到数据库
                SPStrUtil.setStringPreferences(mContext, SPStrUtil.SWITCH_SPEED, scroll);
                if (Constants.IS_CONNECT_BLUETOOTH) {
                    BluetoothSendMessage.sendG(0, getDirection(), getSpeed());
                    setFontSuccess();
                } else {
                    DiyToast.showShort(mContext, getString(R.string.bluetooth_no_Connect));
                }
                finish();

                break;
            default:
                break;
        }
    }

    /**
     * 判断RadioGroup的RadioButton点击时的int型类型并传给蓝牙
     */
    private int getSpeed() {
        if (mFast.isChecked()) {
            return 1;
        } else if (mMid.isChecked()) {
            return 3;
        } else {
            return 6;
        }
    }

    /**
     * 判断RadioGroup的RadioButton点击时的int型类型并传给蓝牙
     */
    private int getDirection() {
        if (mLeft.isChecked()) {
            return 1;

        } else {
            return 0;
        }
    }


}
