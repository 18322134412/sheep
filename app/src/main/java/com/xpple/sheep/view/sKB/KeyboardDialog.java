package com.xpple.sheep.view.sKB;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xpple.sheep.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardDialog extends Dialog
        implements OnClickListener {
    public static String MOTHER_PKG_NAME = "com.xpple.sheep";
    private EditText mInputText_1, mInputText_2, mInputText_3, mInputText_4, mInputText_5, mInputText_6;
    private List<Integer> mNumArray;
    private float mTextSize_num = 0.0F;
    private View.OnClickListener mClickListener;
    private Context mContext;

    public KeyboardDialog(Context context, View.OnClickListener listener, float numTextSize) {
        super(context, context.getResources().getIdentifier("KeyboardDialogStyle", "style", MOTHER_PKG_NAME));
        this.mContext = context;
        this.mClickListener = listener;
        this.mTextSize_num = numTextSize;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    public void setMaxLength() {
        this.mInputText_1.setFilters(new InputFilter[]{new LengthFilter(1)});
        this.mInputText_2.setFilters(new InputFilter[]{new LengthFilter(1)});
        this.mInputText_3.setFilters(new InputFilter[]{new LengthFilter(1)});
        this.mInputText_4.setFilters(new InputFilter[]{new LengthFilter(1)});
        this.mInputText_5.setFilters(new InputFilter[]{new LengthFilter(1)});
        this.mInputText_6.setFilters(new InputFilter[]{new LengthFilter(1)});
    }

    public String getInputText() {
        return this.mInputText_1.getText().toString() + this.mInputText_2.getText().toString() + this.mInputText_3.getText().toString() + this.mInputText_4.getText().toString() + this.mInputText_5.getText().toString() + this.mInputText_6.getText().toString();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.mContext.getResources().getIdentifier("view_keyboard", "layout", MOTHER_PKG_NAME));
        initWidgets();
    }

    private void initWidgets() {
        LinearLayout mLayout = ((LinearLayout) findViewById(this.mContext.getResources().getIdentifier("keyboard_main_layout", "id", MOTHER_PKG_NAME)));
        View mKeyboardNum = getLayoutInflater().inflate(this.mContext.getResources().getIdentifier("view_keyboard_num", "layout", MOTHER_PKG_NAME), null);
        this.mInputText_1 = ((EditText) findViewById(this.mContext.getResources().getIdentifier("keyboard_edittext_1", "id", MOTHER_PKG_NAME)));
        this.mInputText_2 = ((EditText) findViewById(this.mContext.getResources().getIdentifier("keyboard_edittext_2", "id", MOTHER_PKG_NAME)));
        this.mInputText_3 = ((EditText) findViewById(this.mContext.getResources().getIdentifier("keyboard_edittext_3", "id", MOTHER_PKG_NAME)));
        this.mInputText_4 = ((EditText) findViewById(this.mContext.getResources().getIdentifier("keyboard_edittext_4", "id", MOTHER_PKG_NAME)));
        this.mInputText_5 = ((EditText) findViewById(this.mContext.getResources().getIdentifier("keyboard_edittext_5", "id", MOTHER_PKG_NAME)));
        this.mInputText_6 = ((EditText) findViewById(this.mContext.getResources().getIdentifier("keyboard_edittext_6", "id", MOTHER_PKG_NAME)));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -2);
        mLayout.addView(mKeyboardNum, param);
        initKeypadNum();
        LayoutParams wParam = getWindow().getAttributes();
        wParam.gravity = 80;
        wParam.width = -1;
        wParam.height = -2;
        getWindow().setAttributes(wParam);
    }

    private void initKeypadNum() {
        initNumArray();
        int[] idArray =
                {
                        this.mContext.getResources().getIdentifier("keyboard_button0", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button1", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button2", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button3", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button4", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button5", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button6", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button7", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button8", "id", MOTHER_PKG_NAME),
                        this.mContext.getResources().getIdentifier("keyboard_button9", "id", MOTHER_PKG_NAME)};

        for (int i = 0; i < idArray.length; i++) {
            Button buttonNum = (Button) findViewById(idArray[i]);
            buttonNum.setOnClickListener(this);
            buttonNum.setText(String.format(mContext.getString(R.string.number_to_string), mNumArray.get(i)));
            if (this.mTextSize_num != 0.0F) {
                float oldSize = buttonNum.getTextSize();
                buttonNum.setTextSize(oldSize * this.mTextSize_num);
            }
        }

        Button buttonDelete = (Button) findViewById(this.mContext.getResources().getIdentifier("keyboard_num_button_delete", "id", MOTHER_PKG_NAME));
        buttonDelete.setOnClickListener(this);
        buttonDelete.setOnLongClickListener(v -> {
            handleLongClearButton();
            return false;
        });
        Button buttonOk = (Button) findViewById(this.mContext.getResources().getIdentifier("keyboard_num_button_ok", "id", MOTHER_PKG_NAME));
        buttonOk.setOnClickListener(this.mClickListener);
    }

    private void initNumArray() {
        this.mNumArray = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            this.mNumArray.add(i);
        }
        Collections.shuffle(this.mNumArray);
    }

    private void handleInput(View v) {
        if (mInputText_1.getText().length() == 0) {
            this.mInputText_1.setText(((Button) v).getText().toString());
            this.mInputText_2.requestFocus();
            return;
        }
        if (mInputText_2.getText().length() == 0) {
            this.mInputText_2.setText(((Button) v).getText().toString());
            this.mInputText_3.requestFocus();
            return;
        }
        if (mInputText_3.getText().length() == 0) {
            this.mInputText_3.setText(((Button) v).getText().toString());
            this.mInputText_4.requestFocus();
            return;
        }
        if (mInputText_4.getText().length() == 0) {
            this.mInputText_4.setText(((Button) v).getText().toString());
            this.mInputText_5.requestFocus();
            return;
        }
        if (mInputText_5.getText().length() == 0) {
            this.mInputText_5.setText(((Button) v).getText().toString());
            this.mInputText_6.requestFocus();
            return;
        }
        if (mInputText_6.getText().length() == 0) {
            this.mInputText_6.setText(((Button) v).getText().toString());
        }
    }

    private void handleClearButton() {
        if (mInputText_6.getText().length() > 0) {
            this.mInputText_6.setText(null);
            this.mInputText_6.setSelection(0);
            this.mInputText_5.requestFocus();
            return;
        }
        if (mInputText_5.getText().length() > 0) {
            this.mInputText_5.setText(null);
            this.mInputText_5.setSelection(0);
            this.mInputText_4.requestFocus();
            return;
        }
        if (mInputText_4.getText().length() > 0) {
            this.mInputText_4.setText(null);
            this.mInputText_4.setSelection(0);
            this.mInputText_3.requestFocus();
            return;
        }
        if (mInputText_3.getText().length() > 0) {
            this.mInputText_3.setText(null);
            this.mInputText_3.setSelection(0);
            this.mInputText_2.requestFocus();
            return;
        }
        if (mInputText_2.getText().length() > 0) {
            this.mInputText_2.setText(null);
            this.mInputText_2.setSelection(0);
            this.mInputText_1.requestFocus();
            return;
        }
        if (mInputText_1.getText().length() > 0) {
            this.mInputText_1.setText(null);
            this.mInputText_1.setSelection(0);
        }
    }

    private void handleLongClearButton() {
        this.mInputText_6.setText(null);
        this.mInputText_6.setSelection(0);
        this.mInputText_5.setText(null);
        this.mInputText_5.setSelection(0);
        this.mInputText_4.setText(null);
        this.mInputText_4.setSelection(0);
        this.mInputText_3.setText(null);
        this.mInputText_3.setSelection(0);
        this.mInputText_2.setText(null);
        this.mInputText_2.setSelection(0);
        this.mInputText_1.setText(null);
        this.mInputText_1.setSelection(0);
        this.mInputText_1.requestFocus();
    }

    public void onClick(View v) {
        int hitId = v.getId();
        if ((hitId != this.mContext.getResources().getIdentifier("keyboard_num_button_ok", "id", MOTHER_PKG_NAME))) {
            if ((hitId == this.mContext.getResources().getIdentifier("keyboard_num_button_delete", "id", MOTHER_PKG_NAME))) {
                handleClearButton();
            } else {
                handleInput(v);
            }
        }
    }


}