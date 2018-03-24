package com.ccy.lnb.en.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseRecyclerAdapter;

import java.util.List;

/**
 * Created by meng on 16/12/15.
 */

public class PlaySmallAdapter extends BaseRecyclerAdapter<PlaySmallAdapter.PlaySmallItemViewHolder,String> {


    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public PlaySmallAdapter(Context context, List<String> data) {
        super(context, data);
        mContext = context;
        mLayoutInflater= LayoutInflater.from(mContext);
    }

    @Override
    public PlaySmallItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaySmallItemViewHolder(mLayoutInflater.inflate(R.layout.adapter_paly_small,parent,false) );
    }

    @Override
    public void onBindViewHolder(PlaySmallItemViewHolder holder, int position) {

    }

    public class PlaySmallItemViewHolder extends RecyclerView.ViewHolder{
        public PlaySmallItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
