package com.ccy.lnb.en.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ccy.lnb.en.R;

import butterknife.ButterKnife;


/**
 * User: chenfan
 * Date: 2018/06/15
 * Content:
 */
public class TextEditItemView extends LinearLayout {

    public static final int TYPE_EDIT = 0;
    public static final int TYPE_SELECT = 1;

    public static final int INPUT_TYPE_TEXT = 0;
    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_PASSWORD = 2;
    public static final int INPUT_TYPE_PHONE = 3;

    TextView mItemNameView;
    TextView mItemMakerView;
    EditText mItemEditView;
    TextView mItemSelectView;
    ImageView mTailImageView;
    ImageView mItemImageView;

    private Context mContext;

    private String mItemName;
    private String mHintText;
    private String mEditText;
    private String mSelectText;
    private String mSelectHintText;
    private boolean isMarker;
    private boolean mClickable;
    private int mActionType;
    private int mInputType;
    private int mTailIconRes;
    private int mItemIconRes;
    private int mSelectTextColorRes;
    private int mItemNameTextColorRes;

    private boolean isViewLoadFinished = true;
    int maxLength = -1;

    public EditText getmItemEditView() {
        return mItemEditView;
    }

    public TextEditItemView(Context context) {
        super(context);
        initViews(context);
    }

    public TextEditItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray tArray = context.obtainStyledAttributes(attrs,
                R.styleable.TextEditItemView);
        mItemName = tArray.getString(R.styleable.TextEditItemView_item_name);
        mEditText = tArray.getString(R.styleable.TextEditItemView_edit_text);
        mHintText = tArray.getString(R.styleable.TextEditItemView_edit_hint_text);
        mSelectText = tArray.getString(R.styleable.TextEditItemView_select_text);
        mSelectHintText = tArray.getString(R.styleable.TextEditItemView_select_hint_text);
        isMarker = tArray.getBoolean(R.styleable.TextEditItemView_isMarker, false);
        mClickable = tArray.getBoolean(R.styleable.TextEditItemView_clickable, true);
        mActionType = tArray.getInt(R.styleable.TextEditItemView_action_type, TYPE_EDIT);
        mInputType = tArray.getInt(R.styleable.TextEditItemView_input_type, INPUT_TYPE_TEXT);
        mTailIconRes = tArray.getResourceId(R.styleable.TextEditItemView_tail_icon_res, R.mipmap.icon_right);
        mItemIconRes = tArray.getResourceId(R.styleable.TextEditItemView_item_icon, 0);
        mSelectTextColorRes = tArray.getResourceId(R.styleable.TextEditItemView_select_text_color, 0);
        mItemNameTextColorRes = tArray.getResourceId(R.styleable.TextEditItemView_item_name_text_color,
                R.color.default_text_color);
        maxLength = tArray.getInteger(R.styleable.TextEditItemView_maxLength, -1);
        tArray.recycle();

        initViews(context);
    }

    public TextEditItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextEditItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initViews(Context context) {
        mContext = context;
        setClickable(mClickable);
        View view = LayoutInflater.from(context).inflate(R.layout.simple_item_text_edit_layout, this, false);
        addView(view);
        mItemNameView = ButterKnife.findById(view, R.id.item_name);
        mItemMakerView = ButterKnife.findById(view, R.id.item_marker);
        mItemEditView = ButterKnife.findById(view, R.id.item_edit);
        mItemSelectView = ButterKnife.findById(view, R.id.item_select);
        mTailImageView = ButterKnife.findById(view, R.id.tail_image_view);
        mItemImageView = ButterKnife.findById(view, R.id.item_image_view);

        isViewLoadFinished = true;
        mItemEditView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        mItemNameView.setText(mItemName);
        if (mItemNameTextColorRes != 0)
            mItemNameView.setTextColor(getResources().getColor(mItemNameTextColorRes));
        int visibilityC = mClickable ? View.VISIBLE : View.INVISIBLE;
        mTailImageView.setVisibility(visibilityC);
        int visibilityM = isMarker ? View.VISIBLE : View.GONE;
        mItemMakerView.setVisibility(visibilityM);

        showTypeView();
        setInputTypeView();
    }

    private void showTypeView() {

        mTailImageView.setImageResource(mTailIconRes);
        if (mActionType == TYPE_SELECT) {
            mItemEditView.setVisibility(GONE);
            mItemSelectView.setVisibility(VISIBLE);
            mItemSelectView.setHint(mSelectHintText);
            mItemSelectView.setText(mSelectText);

            if (mSelectTextColorRes != 0) {
                mItemSelectView.setTextColor(mContext.getResources().getColor(mSelectTextColorRes));
            }
        } else {
            mItemEditView.setVisibility(VISIBLE);
            mItemEditView.setHint(mHintText);
            mItemEditView.setText(mEditText);
            mItemEditView.setSelection(mItemEditView.getText().length());
        }

        if (mItemIconRes != 0) {
            mItemImageView.setImageResource(mItemIconRes);
        }
    }

    private void setInputTypeView() {
        if (mInputType == INPUT_TYPE_NUMBER) {
            mItemEditView.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (mInputType == INPUT_TYPE_PASSWORD) {
            mItemEditView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (mInputType == INPUT_TYPE_PHONE) {
            mItemEditView.setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }

    public void setItemClickable(boolean clickable) {
        if (isViewLoadFinished) {
            mTailImageView.setVisibility(clickable ? VISIBLE : INVISIBLE);
            setClickable(clickable);
        }
    }

    /**
     * Item名称.
     */
    public void setItemName(String itemName) {
        mItemName = itemName;
        if (isViewLoadFinished) {
            mItemNameView.setText(itemName);
        }
    }

    /**
     * 输入框提示.
     */
    public void setHintText(String hintText) {
        mHintText = hintText;
        if (isViewLoadFinished) {
            mItemEditView.setHint(hintText);
        }
    }

    /**
     * 输入框内容.
     */
    public void setEditText(String editText) {
        mEditText = editText;
        if (isViewLoadFinished) {
            mItemEditView.setText(editText);
        }
    }

    /**
     * 输入框是否可以点击.
     */
    public void setEditFocusable(boolean focusable) {
        if (isViewLoadFinished) {
            mItemEditView.setFocusable(focusable);
        }
    }

    /**
     * 输入框提示内容.
     */
    public void setEditHintText(String hintText) {
        mHintText = hintText;
        if (isViewLoadFinished) {
            mItemEditView.setHint(hintText);
        }
    }

    /**
     * 选择提示内容.
     */
    public void setSelectText(String selectText) {
        mSelectText = selectText;
        if (isViewLoadFinished) {
            mItemSelectView.setText(selectText);
        }
    }

    /**
     * 是否标记为重要.
     */
    public void setMarker(boolean marker) {
        isMarker = marker;
        if (isViewLoadFinished) {
            int visibility = isMarker ? View.VISIBLE : View.GONE;
            mItemMakerView.setVisibility(visibility);
        }
    }

    /**
     * 选择item提示信息.
     */
    public void setSelectHintText(String selectHintText) {
        mSelectHintText = selectHintText;
        if (isViewLoadFinished) {
            mItemSelectView.setText(selectHintText);
        }
    }

    public void setTailIconRes(@DrawableRes int tailIconRes) {
        mTailIconRes = tailIconRes;
        if (isViewLoadFinished) {
            mTailImageView.setImageResource(mTailIconRes);
        }
    }

    public void setItemIconRes(@DrawableRes int itemIconRes) {
        mItemIconRes = itemIconRes;
        if (isViewLoadFinished) {
            mItemImageView.setImageResource(mItemIconRes);
        }
    }

    public void setSelectTextColor(@ColorRes int color) {
        mSelectTextColorRes = color;
        if (isViewLoadFinished) {
            if (mSelectTextColorRes != 0) {
                mItemSelectView.setTextColor(mContext.getResources().getColor(mSelectTextColorRes));
            }
        }
    }

    public void setItemNameTextColor(@ColorRes int color) {
        mItemNameTextColorRes = color;
        if (isViewLoadFinished) {
            if (mItemNameTextColorRes != 0) {
                mItemNameView.setTextColor(mContext.getResources().getColor(mItemNameTextColorRes));
            }
        }
    }

    /**
     * 选择点击事件.
     */
    public void setSelectItemClickListener(OnClickListener l) {
        if (isViewLoadFinished) {
            setOnClickListener(l);
        }
    }

    /**
     * 是否被标记.
     */
    public boolean isMarker() {
        return isMarker;
    }

    /**
     * 获取输入框类容.
     */
    public String getEditText() {
        return mItemEditView.getText().toString();
    }

    /**
     * 获取选择框内容.
     */
    public String getSelectText() {
        return mItemSelectView.getText().toString();
    }

    /**
     * 获取ItemName内容.
     */
    public String getItemText() {
        return mItemNameView.getText().toString();
    }

    /**
     * item类型，分输入类型{@link #TYPE_EDIT}与选择类型{@link #TYPE_SELECT}.
     */
    public void setActionType(int actionType) {
        this.mActionType = actionType;
        if (isViewLoadFinished) {
            showTypeView();
        }
    }

    public void setInputType(int inputType) {
        mInputType = inputType;
        if (isViewLoadFinished) {
            setInputTypeView();
        }
    }
}
