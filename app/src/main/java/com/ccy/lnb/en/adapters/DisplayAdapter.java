package com.ccy.lnb.en.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseRecyclerAdapter;
import com.ccy.lnb.en.been.DisplayBean;
import com.ccy.lnb.en.interfaces.ItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页点发显示adapter
 * Created by MJ on 2017/4/13.
 */

public class DisplayAdapter extends BaseRecyclerAdapter<DisplayAdapter.ItemViewHolder,DisplayBean>{

    private ItemClickListener itemClickListener;//点击item的监听
    private ItemOnClickInterface itemOnClickInterface;

    public void setItemOnClickInterface(ItemOnClickInterface itemOnClickInterface) {
        this.itemOnClickInterface = itemOnClickInterface;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DisplayAdapter(Context context, List<DisplayBean> data) {
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DisplayAdapter.ItemViewHolder(mLayoutInflater.inflate(R.layout.adapter_send_mess,parent,false) );
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        DisplayBean displayBean = mDataList.get(position);
        holder.mTvSendTime.setText(displayBean.getSendTime());
        holder.mTvTimeLong.setText(displayBean.getShowTimeLong());
        holder.mTvInfo.setText(displayBean.getMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view,position);
            }
        });

        holder.mTvAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickInterface.sendAgainClick(view,position);
            }
        });

        holder.mTvInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemOnClickInterface.messLongClick(view,position);
                return false;
            }
        });

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.adapter_send_mess_tv_time_long)
        TextView mTvTimeLong;
        @Bind(R.id.adapter_send_mess_tv_send_time)
        TextView mTvSendTime;
        @Bind(R.id.adapter_send_mess_tv_info)
        TextView mTvInfo;
        @Bind(R.id.adapter_send_mess_tv_is_again)
        TextView mTvAgain;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemOnClickInterface{
        void sendAgainClick(View view,int position);
        void messLongClick(View view,int position);
    }
}
