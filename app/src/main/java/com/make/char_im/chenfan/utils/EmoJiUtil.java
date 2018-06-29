package com.make.char_im.chenfan.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 判断是否是表情
 * Created by Administrator on 2016/12/26.
 */

public class EmoJiUtil {

    public static boolean containsEmoJi(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmoJiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmoJiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /**
     * 禁止输入回车
     */
    public static void closeInputEnter(EditText mEt){
        mEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                            || keyCode == KeyEvent.KEYCODE_ENTER) {

                        return true;
                    }
                }

                return false;
            }
        });
    }

    //限定输入的字节数
    public static void limitWord(final EditText mEt){
        final String[] content = {""};
        mEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                content[0] =charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int wordSize = StringUtil.gbkStr(s.toString());
                if (wordSize>Constants.APP_DEFAULT){
                    mEt.setText(content[0]);
                }
            }
        });
    }

}
