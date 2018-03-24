package com.ccy.lnb.en.activities.pointSendAcs;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.activities.settingAcs.EditActivity;
import com.ccy.lnb.en.adapters.pointSendAds.MyPhraseAdapter;
import com.ccy.lnb.en.base.BaseActivity;
import com.ccy.lnb.en.dialogs.NormalDialog;
import com.ccy.lnb.en.greenDao.MyPhrase;
import com.ccy.lnb.en.greenDao.MyPhraseDao;
import com.ccy.lnb.en.utils.GreenDaoUtil;
import com.ccy.lnb.en.utils.SPStrUtil;
import com.ccy.lnb.en.utils.pullToRefresh.PullAbleRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;

public class MyPhraseActivity extends BaseActivity implements MyPhraseAdapter.ItemOnClickInterface{

    private static final int ADD_BACK_PHRASE = 0;//添加短语返回码
    private static final int UPDATE_BACK_PHRASE = 1;//修改短语返回码

    @Bind(R.id.activity_my_phrase)
    PullAbleRecyclerView mRecycler;
    @Bind(R.id.top_bar_tv_right)
    TextView mTvRight;
    @Bind(R.id.activity_my_phrase_no)
    LinearLayout mLinearNoPhrase;
    @Bind(R.id.no_my_phrase_bt_add)
    Button mBtAddPhrase;

    private MyPhraseAdapter mAdapter;

    private ArrayList<MyPhrase> mMyPhraseBeen = new ArrayList<>();//我的短语集合

    private MyPhraseDao mMyPhraseDao;

    private NormalDialog normalDialog;

    private int editPosition=0;//编辑的position

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_my_phrase);
        pageBack();
        setTopBarTitle(mContext.getString(R.string.my_phrase));
    }

    @Override
    protected void initViews() {

        mMyPhraseDao = mApp.getDaoSession().getMyPhraseDao();
        mTvRight.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);
        mAdapter = new MyPhraseAdapter(mContext, mMyPhraseBeen);
        mRecycler.setAdapter(mAdapter);
        //判断是否有我的短语
        if (!SPStrUtil.getBooleanPreference(mContext,SPStrUtil.IS_HAVE_MY_PHRASE,false)){
            mLinearNoPhrase.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        }else {
            mLinearNoPhrase.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);

            //查询我的短语
//            ArrayList<MyPhrase> m = GreenDaoUtil.queryPhraseData(mMyPhraseDao);
            mMyPhraseBeen = GreenDaoUtil.queryPhraseData(mMyPhraseDao);
            mAdapter.upDateItems(mMyPhraseBeen);
        }



    }

    @Override
    protected void setAllClick() {
        mBtAddPhrase.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mAdapter.setItemOnClickInterface(this);
    }

    @Override
    protected void process() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.no_my_phrase_bt_add:
                goAddEdit();
                break;
            case R.id.top_bar_tv_right:
                goAddEdit();
                break;
        }
    }

    //去编辑界面
    private void goAddEdit(){
        Intent intent = new Intent(mContext, EditActivity.class);
        intent.putExtra("content","");
        startActivityForResult(intent, ADD_BACK_PHRASE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_BACK_PHRASE:
                if (data != null) {
                    String phrase = data.getStringExtra("phrase");
                    MyPhrase myPhrase = new MyPhrase();
                    myPhrase.setMyMess(phrase);
                    mAdapter.addItem(myPhrase);
                    GreenDaoUtil.insertPhraseData(mMyPhraseDao,myPhrase);
                    mRecycler.setVisibility(View.VISIBLE);
                    mLinearNoPhrase.setVisibility(View.GONE);
                    SPStrUtil.setBooleanPreference(mContext,SPStrUtil.IS_HAVE_MY_PHRASE,true);

                }
                break;
            case UPDATE_BACK_PHRASE:
                if (data!=null){
                    String phrase = data.getStringExtra("phrase");
                    MyPhrase myPhrase = new MyPhrase();
                    myPhrase.setId((long) (editPosition+1));
                    myPhrase.setMyMess(phrase);

                    GreenDaoUtil.updatePhraseData(mMyPhraseDao,myPhrase);
                    mAdapter.editItemContent(editPosition,phrase);


                }
                break;

            default:
                break;
        }
    }

    //点击编辑
    @Override
    public void editOnClick(View view, int position,String content) {
        Intent intent = new Intent(mContext, EditActivity.class);
        intent.putExtra("content",content);
        editPosition=position;
        startActivityForResult(intent, UPDATE_BACK_PHRASE);
    }

    //点击删除
    @Override
    public void deleteOnclick(View view, final int position) {
        if (normalDialog != null) {
            if (normalDialog.isShowing()) {
                return;
            }
        }
        normalDialog = new NormalDialog(mContext, R.style.NormalDialog,
                new NormalDialog.OnSureClick() {

                    @Override
                    public void setOnClick() {
                        daoDelItem(position);
                        mAdapter.removeItem(position);
                        if (mAdapter.getItemCount()<1){
                            SPStrUtil.setBooleanPreference(mContext,SPStrUtil.IS_HAVE_MY_PHRASE,false);
                            mLinearNoPhrase.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);
                        }
                    }
                });
        normalDialog.show();
        normalDialog.setTitle(mContext.getString(R.string.dialog_title_delete));
        normalDialog.setContent(mContext.getString(R.string.dialog_content_delete));
        normalDialog.setSure(mContext.getString(R.string.dialog_sure_delete));
    }

    //删除数据库里的该条数据
    private void daoDelItem(int position){
        ArrayList<MyPhrase> displayMesses = GreenDaoUtil.queryPhraseData(mMyPhraseDao);
        GreenDaoUtil.deletePhraseData(mMyPhraseDao, displayMesses.get(position).getId());
    }

    @Override
    public void onItemOnClick(View view, String myMess) {
        Intent intent = new Intent();
        intent.putExtra("sendMess",myMess);
        setResult(0,intent);
        finish();
    }
}
