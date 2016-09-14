package com.xpple.sheep.view.sKB;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class PasswordEditText extends EditText {
    private KeyboardDialog mDialog;
    private Context mContext;
    private byte[] mPassword;
    private final OnClickListener mButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
            String inputText = PasswordEditText.this.mDialog.getInputText();
            PasswordEditText.this.mPassword = Crypter.getInstance().encryptDes(inputText.getBytes());
            inputText = inputText.replaceAll(".", "*");
            PasswordEditText.this.setText(inputText);
            PasswordEditText.this.setSelection(inputText.length());
            PasswordEditText.this.mDialog.dismiss();
            PasswordEditText.this.mDialog = null;
        }
    };

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            if (this.mDialog == null) {
                this.mDialog = new KeyboardDialog(this.mContext, this.mButtonClickListener, 0.8f);
            }
            if (!this.mDialog.isShowing()) {
                this.mDialog.show();
                setText("");
                this.mDialog.setMaxLength();//6
            }
        }

        return true;
    }

    public String getString() {
        if (this.mPassword == null)
            return "";
        return Crypter.getInstance().decryptDes(this.mPassword);
    }
}