package com.ccy.lnb.en.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseRecyclerAdapter;
import com.ccy.lnb.en.been.SettingBean;
import com.ccy.lnb.en.interfaces.ItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置列表的适配器
 * Created by Mj on 2017/4/12.
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
        @Bind(R.id.adapter_setting_img)
        ImageView mImageView;
        @Bind(R.id.adapter_setting_tv_name)
        TextView mTvName;
        @Bind(R.id.adapter_setting_linear)
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
