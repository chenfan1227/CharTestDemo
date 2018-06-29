package com.make.char_im.chenfan.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.make.char_im.chenfan.R;


/**
 * 自定义等待Dialog
 */
public class LoadingDialog extends ProgressDialog {
    private Context context;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }


    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((event.getAction() == KeyEvent.ACTION_DOWN)
                && (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
