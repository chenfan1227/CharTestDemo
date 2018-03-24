package com.ccy.lnb.en.adapters.pointSendAds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseRecyclerAdapter;
import com.ccy.lnb.en.been.MyPhraseBean;
import com.ccy.lnb.en.greenDao.MyPhrase;
import com.ccy.lnb.en.utils.pullToRefresh.PullAbleRecyclerView;
import com.ccy.lnb.en.views.slidehelper.ItemSlideHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的短语适配器
 * Created by MJ on 2017/4/13.
 */

public class MyPhraseAdapter  extends BaseRecyclerAdapter<MyPhraseAdapter.ItemViewHolder,MyPhrase>implements ItemSlideHelper.Callback {

    private RecyclerView mRecyclerView;
    private ItemOnClickInterface itemOnClickInterface;

    public void setItemOnClickInterface(ItemOnClickInterface itemOnClickInterface) {
        this.itemOnClickInterface = itemOnClickInterface;
    }

    public MyPhraseAdapter(Context context, List<MyPhrase> data) {
        super(context, data);
    }

    public void editItemContent(int position,String content){
        mDataList.get(position).setMyMess(content);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPhraseAdapter.ItemViewHolder(mLayoutInflater.inflate(R.layout.adapter_my_phrase,parent,false) );
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final MyPhrase myPhrase = mDataList.get(position);
        holder.mTvMess.setText(myPhrase.getMyMess());

        holder.mImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickInterface.editOnClick(view,position,myPhrase.getMyMess());
            }
        });
        holder.mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickInterface.deleteOnclick(view,position);
            }
        });
        holder.mTvMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickInterface.onItemOnClick(view,myPhrase.getMyMess());
            }
        });

        if (position%2==0){
            holder.mTvMess.setBackgroundResource(R.drawable.bg_item_press);
        }else {
            holder.mTvMess.setBackgroundResource(R.color.c10);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this));

    }

    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            if (viewGroup.getChildCount() == 2) {
                return viewGroup.getChildAt(1).getLayoutParams().width;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }

    @Override
    public void setIsShield(boolean isShield) {
        ((PullAbleRecyclerView)mRecyclerView).setIsShield(isShield);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.adapter_my_phrase_tv)
        TextView mTvMess;
        @Bind(R.id.adapter_my_phrase_img_edit)
        ImageView mImgEdit;
        @Bind(R.id.adapter_my_phrase_img_delete)
        ImageView mImgDelete;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemOnClickInterface{
        void editOnClick(View view,int position,String content);
        void deleteOnclick(View view,int position);
        void onItemOnClick(View view,String mess);
    }
}
