package com.make.char_im.chenfan.fragments;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.activities.AboutActivity;
import com.make.char_im.chenfan.activities.MoreActivity;
import com.make.char_im.chenfan.activities.settingAcs.BluetoothActivity;
import com.make.char_im.chenfan.activities.settingAcs.BrightnessActivity;
import com.make.char_im.chenfan.activities.settingAcs.DefaultMessageActivity;
import com.make.char_im.chenfan.activities.settingAcs.FontModeActivity;
import com.make.char_im.chenfan.adapters.SettingAdapter;
import com.make.char_im.chenfan.base.BaseFragment;
import com.make.char_im.chenfan.been.SettingBean;
import com.make.char_im.chenfan.interfaces.ItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 设置的fragment
 * Created by MJ on 2017/4/10.
 */

public class SettingFrag extends BaseFragment implements ItemClickListener {

    @BindView(R.id.frag_setting_recycler)
    RecyclerView mRecycler;

    private SettingAdapter mAdapter;//设置适配器

    private ArrayList<SettingBean> settingBeen = new ArrayList<>();//设置数据集合

    //设置选项
    private String[] names;
    //点击跳转的class
    private Class<?>[] listClass = {DefaultMessageActivity.class, BrightnessActivity.class, FontModeActivity.class, BluetoothActivity.class, AboutActivity.class, MoreActivity.class};
    //图片
    private int[] imageIds = {R.mipmap.icon_default_mess, R.mipmap.icon_set_bright, R.mipmap.icon_set_model, R.mipmap.icon_set_bluetooth, R.mipmap.more, R.mipmap.about};
    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_setting, null);
        pageBack(view);

        goneBack(view);
        setTopBarTitle(view, mContext.getString(R.string.setting));
        return view;
    }

    @Override
    protected void initViews(View view) {
        names = new String[]{mContext.getString(R.string.setting_default_mess)
                , mContext.getString(R.string.setting_bright)
                , mContext.getString(R.string.setting_model)
                , mContext.getString(R.string.setting_bluetooth),
                mContext.getString(R.string.my_more),
                mContext.getString(R.string.activity_about)

        };
        RecyclerView.LayoutManager manager = new GridLayoutManager(mContext, 2);
        mRecycler.setLayoutManager(manager);
        int length = listClass.length;
        SettingBean settingBean;
        for (int i = 0; i < length; i++) {
            settingBean = new SettingBean();
            settingBean.setName(names[i]);
            settingBean.setaClass(listClass[i]);
            settingBean.setImageId(imageIds[i]);
            settingBeen.add(settingBean);
        }
        mAdapter = new SettingAdapter(mContext, settingBeen);
        mRecycler.setAdapter(mAdapter);//设置adapter
    }

    @Override
    protected void setAllClick() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void process() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(View view, int position) {
        intentAc(listClass[position]);
    }


}
