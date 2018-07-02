package com.make.char_im.chenfan.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseRecyclerAdapter;
import com.make.char_im.chenfan.been.SettingBean;
import com.make.char_im.chenfan.interfaces.ItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Administrator on 2018/7/2 0002 10:22
 * 功能： 设置列表的适配器
 * 作者：chenfan
 */


public class SettingAdapter extends BaseRecyclerAdapter<SettingAdapter.ItemViewHolder,SettingBean>{

    private ItemClickListener mItemClickListener;//点击接口

    public SettingAdapter(Context context, List<SettingBean> data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mLayoutInflater.inflate(R.layout.adapter_setting,parent,false) );
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        SettingBean settingBean= mDataList.get(position);
        holder.mImageView.setImageResource(settingBean.getImageId());
        holder.mTvName.setText(settingBean.getName());
        holder.mLinearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view,position);
            }
        });

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.adapter_setting_img)
        ImageView mImageView;
        @BindView(R.id.adapter_setting_tv_name)
        TextView mTvName;
        @BindView(R.id.adapter_setting_linear)
        LinearLayout mLinearItem;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //设置点击回调
    public void setOnItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
