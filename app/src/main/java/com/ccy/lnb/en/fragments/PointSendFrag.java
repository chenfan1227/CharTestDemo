package com.ccy.lnb.en.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ccy.lnb.en.BluetoothOrder.BluetoothSendMessage;
import com.ccy.lnb.en.R;
import com.ccy.lnb.en.activities.pointSendAcs.MyPhraseActivity;
import com.ccy.lnb.en.adapters.DisplayAdapter;
import com.ccy.lnb.en.base.BaseFragment;
import com.ccy.lnb.en.been.DisplayBean;
import com.ccy.lnb.en.dialogs.NormalDialog;
import com.ccy.lnb.en.dialogs.YuYinDialog;
import com.ccy.lnb.en.greenDao.DisplayMess;
import com.ccy.lnb.en.greenDao.DisplayMessDao;
import com.ccy.lnb.en.interfaces.ItemClickListener;
import com.ccy.lnb.en.utils.BluetoothUtil;
import com.ccy.lnb.en.utils.Constants;
import com.ccy.lnb.en.utils.DateUtils;
import com.ccy.lnb.en.utils.DiyToast;
import com.ccy.lnb.en.utils.EmoJiUtil;
import com.ccy.lnb.en.utils.GreenDaoUtil;
import com.ccy.lnb.en.utils.pullToRefresh.PullAbleRecyclerView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.Bind;

/**
 * 点发的fragment
 * Created by MJ on 2017/4/10.
 */

public class PointSendFrag extends BaseFragment implements ItemClickListener, DisplayAdapter.ItemOnClickInterface {

    @Bind(R.id.frag_display_rg)
    RadioGroup mRgTime;
    @Bind(R.id.frag_display_rb_sec)
    RadioButton mRbSec;

    @Bind(R.id.display_input_linear_write)
    LinearLayout mLinearWrite;
    @Bind(R.id.display_input_linear_speak)
    LinearLayout mLinearSpeak;
    @Bind(R.id.display_input_go_write)
    ImageView mImgGoWrite;
    @Bind(R.id.display_input_img_go_voice)
    ImageView mImgGoVoice;
    @Bind(R.id.display_input_img_word_write)
    ImageView mImgWordW;
    @Bind(R.id.display_input_word_voice)
    ImageView mImgWordV;
    @Bind(R.id.display_input_img_send)
    ImageView mImgSend;
    @Bind(R.id.display_input_et)
    EditText mEtMess;
    @Bind(R.id.display_input_speak)
    Button mTvSpeak;

    @Bind(R.id.frag_display_recycler)
    PullAbleRecyclerView mRecycler;
    @Bind(R.id.display_no_word_linear)
    LinearLayout mLinearNoWord;

    private DisplayAdapter mAdapter;
    private LinearLayoutManager mManager;

    private ArrayList<DisplayBean> mDisplayBeen = new ArrayList<>();//发送的消息的实体类
    private DisplayMessDao mDisplayMessDao;

    private NormalDialog normalDialog;

    private YuYinDialog mYYDialog;//语音弹窗
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    SpeechRecognizer mIat;
    private SharedPreferences mSharedPreferences;//提取配置信息
    private VoiceButton mVoiceButton;

    private static final int MY_PHRASE_BACK_CODE = 0;//我的短语返回码


    /**
     * 初始化Fragment
     */
    public static Fragment instanceFrag() {
        PointSendFrag fragment = new PointSendFrag();
        return fragment;
    }

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_display, null);
        pageBack(view);
        goneBack(view);
        setTopBarTitle(view, mContext.getString(R.string.my_screen));
        return view;
    }

    @Override
    protected void initViews(View view) {
        mRbSec.setChecked(true);//默认选择12s

        mDisplayMessDao = mApp.getDaoSession().getDisplayMessDao();

        mManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(mManager);
        mAdapter = new DisplayAdapter(mContext, mDisplayBeen);
        mRecycler.setAdapter(mAdapter);

        mEtMess.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.APP_DEFAULT)});

        EmoJiUtil.closeInputEnter(mEtMess);//禁止输入回车

        yY();//初始化配置


    }

    @Override
    protected void setAllClick() {
        mImgGoVoice.setOnClickListener(this);
        mImgGoWrite.setOnClickListener(this);
        mImgWordW.setOnClickListener(this);
        mImgWordV.setOnClickListener(this);
        mImgSend.setOnClickListener(this);
        mAdapter.setItemClickListener(this);
        mAdapter.setItemOnClickInterface(this);
        mLinearNoWord.setOnClickListener(this);
        onSpeak();
        EmoJiUtil.limitWord(mEtMess);
    }

    @Override
    protected void process() {
        getUpdateListData();//得到并更新列表

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.display_input_go_write:
                //切到写模式
                mLinearWrite.setVisibility(View.VISIBLE);
                mLinearSpeak.setVisibility(View.GONE);
                break;
            case R.id.display_input_img_go_voice:
                //切到说话模式
                mLinearWrite.setVisibility(View.GONE);
                mLinearSpeak.setVisibility(View.VISIBLE);
                break;
            case R.id.display_input_img_word_write:
                goMyPhrase();
                break;
            case R.id.display_input_word_voice:
                goMyPhrase();
                break;
            case R.id.display_input_img_send:
                //发送消息
                sendMess(mEtMess.getText().toString());
                break;
            case R.id.display_no_word_linear:
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtMess.getWindowToken(), 0);
                break;
        }
    }

    //跳转到我的短语界面
    private void goMyPhrase() {
        Intent intent = new Intent(mContext, MyPhraseActivity.class);
        startActivityForResult(intent, MY_PHRASE_BACK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_PHRASE_BACK_CODE:
                if (data != null) {
                    String sendMessMy = data.getStringExtra("sendMess");
                    sendMess(sendMessMy);
                }
                break;
        }
    }

    /**
     * 得到并更新列表
     */
    private void getUpdateListData() {
        //从数据库里那数据初始化列表
        ArrayList<DisplayMess> displayMesses = GreenDaoUtil.queryData(mDisplayMessDao);
        int length = displayMesses.size();

        for (int i = 0; i < length; i++) {
            DisplayMess displayMess = displayMesses.get(i);
            mDisplayBeen.add(modelB2A(displayMess));
        }
        mAdapter.upDateItems(mDisplayBeen);
        mManager.scrollToPosition(mAdapter.getItemCount() - 1);

        judgeIsHaveMess();
    }

    /**
     * 发送消息
     */
    private void sendMess(String mess) {
        if (mess.trim().equals("")) {
            DiyToast.showShort(mContext, mContext.getString(R.string.cannot));
            return;
        }
        if (Constants.IS_CONNECT_BLUETOOTH) {
            DisplayBean displayBean = new DisplayBean();
            //加入数据库
            GreenDaoUtil.insertData(mDisplayMessDao, modelA2B(displayBean, mess));

            mAdapter.addItem(displayBean);
            mManager.scrollToPosition(mAdapter.getItemCount() - 1);
            mEtMess.setText("");
            BluetoothSendMessage.sendMessage(displayBean.getMessage(), 1, getPlayState(), 12);
            judgeIsHaveMess();
        } else {
            DiyToast.showShort(mContext, mContext.getString(R.string.go_connect_bluetooth));

        }

    }

    /**
     * 是否循环
     *
     * @return 0表示循环
     */
    private String getPlayState() {
        if (mRbSec.isChecked()) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 得到发送时长
     */
    private String getTimeLong() {
        if (mRbSec.isChecked()) {
            return "12 Secs";
        } else {
            return "Loop";
        }
    }

    /**
     * 将DisplayBean转换为DisplayMess
     *
     * @param displayBean--
     * @return --DisplayMess
     */
    private DisplayMess modelA2B(DisplayBean displayBean, String mess) {
        displayBean.setSendTime(DateUtils.getCurrentTime());
        displayBean.setMessage(mess);
        displayBean.setShowTimeLong(getTimeLong());

        DisplayMess displayMess = new DisplayMess();
        displayMess.setSendTime(DateUtils.getCurrentTime());
        displayMess.setMessage(mess);
        displayMess.setShowTimeLong(getTimeLong());
        return displayMess;
    }

    /**
     * 将DisplayMess转换为DisplayBean
     */
    private DisplayBean modelB2A(DisplayMess displayMess) {
        DisplayBean displayBean = new DisplayBean();
        displayBean.setSendTime(displayMess.getSendTime());
        displayBean.setShowTimeLong(displayMess.getShowTimeLong());
        displayBean.setMessage(displayMess.getMessage());
        return displayBean;
    }

    //item的点击事件
    @Override
    public void onItemClick(View view, int position) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtMess.getWindowToken(), 0);
    }

    /**
     * 判断是否有消息
     */
    private void judgeIsHaveMess() {
        if (mAdapter.getItemCount() > 0) {
            mRecycler.setVisibility(View.VISIBLE);
            mLinearNoWord.setVisibility(View.GONE);
        } else {
            mRecycler.setVisibility(View.GONE);
            mLinearNoWord.setVisibility(View.VISIBLE);
        }
    }

    //重新发送
    @Override
    public void sendAgainClick(View view, int position) {
        if (Constants.IS_CONNECT_BLUETOOTH) {
            //从数据库里删除
            daoDelItem(position);
            sendMess(mAdapter.getDataList().get(position).getMessage());
            mAdapter.removeItem(position);


        } else {
            DiyToast.showShort(mContext, mContext.getString(R.string.go_connect_bluetooth));

        }
    }

    //删除数据库里的该条数据
    private void daoDelItem(int position) {
        ArrayList<DisplayMess> displayMesses = GreenDaoUtil.queryData(mDisplayMessDao);
        GreenDaoUtil.deleteData(mDisplayMessDao, displayMesses.get(position).getId());
    }

    //长按监听
    @Override
    public void messLongClick(View view, final int position) {
        if (normalDialog != null) {
            if (normalDialog.isShowing()) {
                return;
            }
        }
        normalDialog = new NormalDialog(getActivity(), R.style.NormalDialog,
                new NormalDialog.OnSureClick() {

                    @Override
                    public void setOnClick() {
                        daoDelItem(position);
                        mAdapter.removeItem(position);
                        judgeIsHaveMess();
                    }
                });
        normalDialog.show();
        normalDialog.setTitle(mContext.getString(R.string.dialog_title_delete));
        normalDialog.setContent(mContext.getString(R.string.dialog_content_delete));
        normalDialog.setSure(mContext.getString(R.string.dialog_sure_delete));
    }


    /**
     * 按住说话监听
     */
    private void onSpeak() {
        mTvSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (BluetoothUtil.isSay(mContext)) {
                    float y1 = 0, y2 = 0;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        mYYDialog.dismiss();
                        if (mYYDialog.getStatus() == mYYDialog.SUMBITSEND) {
                            mIat.stopListening();
                        } else {
                            mIat.cancel();
                        }
                        mTvSpeak.setText(R.string.display_speak);
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mTvSpeak.setText(R.string.release_cancel);
                        y1 = event.getY();
                        startYY();
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        y2 = event.getY();
                        if (y1 - y2 > 20) {
                            mYYDialog.setStatus(mYYDialog.CANCELSEND);
                        }
                        if (y2 - y1 > 20) {
                            mYYDialog.setStatus(mYYDialog.SUMBITSEND);
                        }
                    }
                } else {
                    if (event.getAction() == MotionEvent.ACTION_UP)
                    DiyToast.showShort(mContext, getResources().getString(R.string.fail_permission));
                }

                return false;
            }

        });
    }

    /**
     * 开始语音监听
     */
    private void startYY() {
        mYYDialog = new YuYinDialog(mContext, R.style.dialog);
        mYYDialog.show();
        setParam();
        int ret = mIat.startListening(mRecognizerListener);

        if (21001 == ret) {
            DiyToast.showShort(mContext, getString(R.string.no_installed));
            mIat.stopListening();
            return;

        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.e("onInit", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {

            }
        }
    };

    /**
     * 初始化SharedPreferences，SpeechRecognizer
     */
    public void yY() {
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        mSharedPreferences = mContext.getSharedPreferences("com.iflytek.setting",
                Activity.MODE_PRIVATE);
    }

    /**
     * 按钮切换
     */
    public void setVoiceButton(VoiceButton b) {
        mVoiceButton = b;
    }


    public interface VoiceButton {
        void cancel();
    }

    /**
     * 参数设置
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        //if (lag.equals("en_us")) {
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        /*} else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }*/

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "2000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA, mSharedPreferences.getString("iat_dwa_preference", "0"));
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            MyToast.showShort(mcContext, "开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (error.toString().contains(error.getPlainDescription(false))) {
                DiyToast.showShort(mContext, mContext.getString(R.string.spoken));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //DiyToast.showShort(mContext, "结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.e("result:", results.getResultString());

            if (isLast) {
                // TODO 最后的结果
            } else {
                printResult(results);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            MyToast.showShort(mcContext, "当前正在说话，音量大小：" + volume);
            mYYDialog.setVolice(volume);
            Log.e("volume", "" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
//            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//                MyToast.showShort(mcContext, "session id =" + sid);
//            }
        }
    };

    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        if (mYYDialog.getStatus() == mYYDialog.SUMBITSEND) {
           /* mEditText_Content.setText(resultBuffer.toString());
            sendmessage();*/
            sendMess(resultBuffer.toString());
            mYYDialog.dismiss();
        }
    }


    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

}
