package com.ccy.lnb.en.activities.settingAcs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccy.lnb.en.R;
import com.ccy.lnb.en.base.BaseActivity;
import com.ccy.lnb.en.base.BaseRecyclerAdapter;
import com.ccy.lnb.en.utils.SPStrUtil;

import butterknife.Bind;


/**
 * 默认短语编辑界面
 * Created by make chen
 */

public class DefaultMessageActivity extends BaseActivity {
    @Bind(R.id.default_text)
    TextView mDefaultText;
    @Bind(R.id.default_relativeLayout)
    RelativeLayout mRelativeLayout;
    @Bind(R.id.default_message)
    TextView mMessage;

    private static final int MESSAGE = 0;

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_default);
        pageBack();
        setTopBarTitle(mContext.getString(R.string.setting_default_mess));
    }

    @Override
    protected void initViews() {
        Intent getIntent = getIntent();
        String mEmail = getIntent.getStringExtra("mEmail");
        mMessage.setText(mEmail);
        String message = SPStrUtil.getStringPreference(mContext, SPStrUtil.DEFAULT_MESS, mMessage.getText().toString());
        mMessage.setText(message);

    }

    @Override
    protected void setAllClick() {
        mRelativeLayout.setOnClickListener(this);

    }

    @Override
    protected void process() {

    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //RelativeLayout
            case R.id.default_relativeLayout:
                String message = SPStrUtil.getStringPreference(mContext, SPStrUtil.DEFAULT_MESS, "");
                Intent it = new Intent(mContext, DefaultMessage.class);
                it.putExtra("mEmail", message);
                startActivityForResult(it, MESSAGE);
                break;
            case R.id.default_message:

                break;
            case R.id.default_text:

                break;
            default:
                break;
        }

    }

    /**
     * 重写onResult方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MESSAGE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String string = bundle.getString("defaultMess");
                    mMessage.setText(string);
                }
                break;
            default:

        }
    }
}
