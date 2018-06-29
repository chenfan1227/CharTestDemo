package com.make.char_im.chenfan.activities.settingAcs;


/**
 * 亮度调节界面
 * Created by make chen
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.make.char_im.chenfan.BluetoothOrder.BluetoothSendMessage;
import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseActivity;
import com.make.char_im.chenfan.utils.Constants;
import com.make.char_im.chenfan.utils.DiyToast;
import com.make.char_im.chenfan.utils.SPStrUtil;

import butterknife.BindView;


public class BrightnessActivity extends BaseActivity {
    @BindView(R.id.brightness_check_one)
    ImageView mCheckOne;
    @BindView(R.id.brightness_check_two)
    ImageView mCheckTwo;
    @BindView(R.id.brightness_check_there)
    ImageView mCheckThere;
    @BindView(R.id.brightness_check_for)
    ImageView mCheckFor;
    @BindView(R.id.bluetooth_Number)
    TextView mBluetoothNumber;
    @BindView(R.id.image_button)
    ImageView mButton;
    @BindView(R.id.brightness_LinearLayout_one)
    LinearLayout mLinearLayoutOne;
    @BindView(R.id.brightness_LinearLayout_two)
    LinearLayout mLinearLayoutTwo;
    @BindView(R.id.brightness_LinearLayout_there)
    LinearLayout mLinearLayoutThere;
    @BindView(R.id.brightness_LinearLayout_for)
    LinearLayout mLinearLayoutFor;
    @BindView(R.id.brightness_LinearLayout)
    LinearLayout mLinearLayout;
    //默认开关开启状态
    boolean mImageCheck = false;
    //4种亮度并根据亮度找到相应的图片切换
    int mProgress;
    int mLightPost;
    //ImageView数组
    ImageView[] imageViews;
    //4种显示的亮度
    public int[] progress = {25, 50, 75, 100};
    //默认状态
    int[] munCheck = {R.mipmap.brightness_25_check, R.mipmap.brightness_50_check, R.mipmap.brightness_75_check, R.mipmap.brightness_100_check};
    //选中状态
    int[] mTheSelected = {R.mipmap.theselected_25, R.mipmap.theselected_50, R.mipmap.theselected_75, R.mipmap.theselected_100};


    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_brightness);
        pageBack();
        setTopBarTitle(mContext.getString(R.string.setting_bright));
    }

    /**
     * imageViews图片数组
     */
    @Override
    protected void initViews() {
        imageViews = new ImageView[4];
        imageViews[0] = mCheckOne;
        imageViews[1] = mCheckTwo;
        imageViews[2] = mCheckThere;
        imageViews[3] = mCheckFor;

    }

    @Override
    protected void setAllClick() {
        mBluetoothNumber.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mLinearLayoutOne.setOnClickListener(this);
        mLinearLayoutTwo.setOnClickListener(this);
        mLinearLayoutFor.setOnClickListener(this);
        mLinearLayoutThere.setOnClickListener(this);
    }

    @Override
    protected void process() {
        /**
         * 从数据库拿到按钮并进行处理
         * */
        String percent = SPStrUtil.getStringPreference(mContext, SPStrUtil.DISPLAY_BRIGHT, "25%");
        boolean isAuto = SPStrUtil.getBooleanPreference(mContext, SPStrUtil.SWITCH_AUTO_BRIGHT, false);
        if (isAuto == false) {
            mButton.setImageResource(R.mipmap.open);
            mImageCheck = true;
            mLinearLayout.setVisibility(View.GONE);

        } else {
            mButton.setImageResource(R.mipmap.off);
            mImageCheck = false;
            mLinearLayout.setVisibility(View.VISIBLE);
            setImageCheck(1);
        }
        /**
         * 判断是那种显示亮度并在列表进行匹配并切换点击后的图片
         * */
        if (percent.equals("25%")) {
            setImageCheck(0);
        } else if (percent.equals("50%")) {
            setImageCheck(1);
        } else if (percent.equals("75%")) {
            setImageCheck(2);
        } else if (percent.equals("100%")) {
            setImageCheck(3);
        }
        if (Constants.IS_CONNECT_BLUETOOTH) {
            String name = SPStrUtil.getStringPreference(mContext, SPStrUtil.CONNECT_BLUETOOTH_NAME, "");
            mBluetoothNumber.setText(name);
        } else {
            mBluetoothNumber.setVisibility(View.GONE);
        }
    }


    /**
     * 根据id点击事件判断那个图标进行变化
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_button:
                //判断点击开关按钮的状态并显示或关闭下面的选择框
                if (mImageCheck) {
                    mProgress = progress[mLightPost];
                    mImageCheck = false;
                    SPStrUtil.setBooleanPreference(mContext, SPStrUtil.SWITCH_AUTO_BRIGHT, true);
                    mButton.setImageResource(R.mipmap.off);
                    mLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    mProgress = progress[mLightPost];
                    mImageCheck = true;
                    mButton.setImageResource(R.mipmap.open);
                    mLinearLayout.setVisibility(View.GONE);
                    SPStrUtil.setBooleanPreference(mContext, SPStrUtil.SWITCH_AUTO_BRIGHT, false);
                    if (Constants.IS_CONNECT_BLUETOOTH) {
                        BluetoothSendMessage.sendF(50);
                        setImageCheck(1);
                    } else {
                        DiyToast.showShort(mContext, getString(R.string.bluetooth_no_Connect));
                    }
                    setImageCheck(1);
                    SPStrUtil.setStringPreferences(mContext, SPStrUtil.DISPLAY_BRIGHT, "50%");
                }

                break;
//            25%
            case R.id.brightness_LinearLayout_one:
                SPStrUtil.setStringPreferences(mContext, SPStrUtil.DISPLAY_BRIGHT, "25%");

                if (!mImageCheck)
                    setImageCheck(0);
                mBluetooth();
                break;
            //50%
            case R.id.brightness_LinearLayout_two:
                SPStrUtil.setStringPreferences(mContext, SPStrUtil.DISPLAY_BRIGHT, "50%");
                if (!mImageCheck)
                    setImageCheck(1);
                mBluetooth();
                break;
            //75%
            case R.id.brightness_LinearLayout_there:
                SPStrUtil.setStringPreferences(mContext, SPStrUtil.DISPLAY_BRIGHT, "75%");
                if (!mImageCheck)
                    setImageCheck(2);
                mBluetooth();
                break;
            //100%
            case R.id.brightness_LinearLayout_for:
                SPStrUtil.setStringPreferences(mContext, SPStrUtil.DISPLAY_BRIGHT, "100%");
                if (!mImageCheck)
                    setImageCheck(3);
                mBluetooth();
                break;
            default:
                break;

        }
    }

    public void mBluetooth() {
        if (Constants.IS_CONNECT_BLUETOOTH) {
            BluetoothSendMessage.sendF(mProgress);
        } else {
            DiyToast.showShort(mContext, getString(R.string.bluetooth_no_Connect));
        }
    }

    /**
     * 根据数组的个数并显示相应的图
     */
    public void setImageCheck(int p) {
        mLightPost = p;
        mProgress = progress[p];
        for (int i = 0; i < imageViews.length; i++) {
            if (p == i) {
                imageViews[i].setImageResource(mTheSelected[i]);
            } else {
                imageViews[i].setImageResource(munCheck[i]);
            }
        }
    }


}
