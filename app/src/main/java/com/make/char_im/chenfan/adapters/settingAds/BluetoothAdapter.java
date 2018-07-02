package com.make.char_im.chenfan.adapters.settingAds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.make.char_im.chenfan.BluetoothOrder.le.iBeaconClass;
import com.make.char_im.chenfan.R;
import com.make.char_im.chenfan.base.BaseRecyclerAdapter;
import com.make.char_im.chenfan.interfaces.BluetoothConnectListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 蓝牙设备列表adapter
 * Created by chen on 2017/4/14.
 */

public class BluetoothAdapter extends BaseRecyclerAdapter<BluetoothAdapter.ItemViewHolder, iBeaconClass.iBeacon> {

    private int currentPosition = 0;

    public BluetoothAdapter(Context context, List<iBeaconClass.iBeacon> data) {
        super(context, data);
    }

    private BluetoothConnectListener bluetoothConnectListener;//连接，断开蓝牙的监听

    public void setBluetoothConnectListener(BluetoothConnectListener bluetoothConnectListener) {
        this.bluetoothConnectListener = bluetoothConnectListener;
    }

    /**
     * 添加蓝牙设备入列表，重复的去掉
     */
    public void addItemOnly(iBeaconClass.iBeacon iBeacon) {
        if (iBeacon == null)
            return;
        for (int i = 0; i < mDataList.size(); i++) {
            String btAddress = mDataList.get(i).bluetoothAddress;
            if (btAddress.equals(iBeacon.bluetoothAddress)) {
                mDataList.add(i + 1, iBeacon);
                mDataList.remove(i);
                return;
            }
        }
        mDataList.add(iBeacon);
        notifyDataSetChanged();
    }

    /**
     * 改变连接状态
     */
    public void changeConnectStatus(int position, boolean isConnect) {
        int length = mDataList.size();
        for (int i = 0; i < length; i++) {
            if (i == position) {
                mDataList.get(i).isConnect = isConnect;
            } else {
                mDataList.get(i).isConnect = false;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BluetoothAdapter.ItemViewHolder(mLayoutInflater.inflate(R.layout.adapter_bluetooth, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final iBeaconClass.iBeacon bluetoothListBean = mDataList.get(position);
        holder.mTvName.setText(bluetoothListBean.name);
        if (!bluetoothListBean.isConnect) {
            holder.mTvIsConnect.setText(mContext.getString(R.string.bluetooth_connect));
            holder.mTvIsConnect.setTextColor(mContext.getResources().getColor(R.color.c7));
        } else {
            holder.mTvIsConnect.setText(mContext.getString(R.string.bluetooth_disconnect));
            holder.mTvIsConnect.setTextColor(mContext.getResources().getColor(R.color.c2));
        }
        holder.mTvIsConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = position;
                if (holder.mTvIsConnect.getText().toString().equals(mContext.getString(R.string.bluetooth_connect))) {
                    bluetoothConnectListener.connectBluetoothOnClick((TextView) view, position);
                } else {
                    bluetoothConnectListener.disconnectBluetoothOnClick((TextView) view, position);
                }
            }
        });


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_bluetooth_tv_name)
        TextView mTvName;
        @BindView(R.id.adapter_bluetooth_tv_is_connect)
        TextView mTvIsConnect;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
