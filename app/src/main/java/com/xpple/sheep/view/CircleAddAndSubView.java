package com.xpple.sheep.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xpple.sheep.R;


public class CircleAddAndSubView extends LinearLayout {

    private Context context = getContext();//上下文

    private OnNumChangeListener onNumChangeListener;

    private ImageView addButton;//加按钮

    private ImageView subButton;//减按钮

    private EditText editText;//数量显示

    private Integer num; //数量值
    /**
     * 减
     */
    public static final int TYPE_SUBTRACT = 0;
    /**
     * 加
     */
    public static final int TYPE_ADD = 1;

    public boolean isAutoChangeNum = true;//是否自动转变数量

    /**
     * 构造方法
     */
    public CircleAddAndSubView(Context context) {
        super(context);
        this.context = context;
        num = 1;
        control();
    }

    /**
     * 构造方法
     */
    public CircleAddAndSubView(Context context, int num) {
        super(context);
        this.context = context;
        this.num = num;
        control();
    }

    public CircleAddAndSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        num = 1;
        control();
    }

    /**
     * 初始化
     */
    private void control() {
        setPadding(1, 1, 1, 1);
        initView();
        setViewListener();
    }


    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_add_sub, null);
        addButton = (ImageView) view.findViewById(R.id.add_btn_id);
        subButton = (ImageView) view.findViewById(R.id.sub_btn_id);
        editText = (EditText) view.findViewById(R.id.num_text_id);
        editText.addTextChangedListener(mWatcher);
        setNum(1);
        addView(view);
    }

    /**
     * 设置中间的距离
     */
    public void setMiddleDistance(int distance) {
        editText.setPadding(distance, 0, distance, 0);
    }

    /**
     * 设置默认数量
     */
    public void setNum(int num) {
        this.num = num;
        if (num > 0) {
            subButton.setEnabled(true);
        } else {
            subButton.setEnabled(false);
        }
        editText.setText(String.valueOf(num));
        editText.setSelection(editText.length());
    }

    /**
     * 获取值
     */
    public int getNum() {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            return Integer.parseInt(editText.getText().toString());
        } else {
            return 1;
        }
    }


    /**
     * 设置加号图片
     */
    public void setAddBtnBackgroundResource(int addBtnDrawable) {
        addButton.setBackgroundResource(addBtnDrawable);
    }

    /**
     * 设置减法图片
     */
    public void setSubBtnBackgroundResource(int subBtnDrawable) {
        subButton.setBackgroundResource(subBtnDrawable);
    }

    /**
     * 设置是否自动改变数量玩
     */
    public void setAutoChangeNumber(boolean isAutoChangeNum) {
        this.isAutoChangeNum = isAutoChangeNum;
    }

    /**
     * 设置加法减法的背景色
     */
    public void setButtonBgColor(int addBtnColor, int subBtnColor) {
        addButton.setBackgroundColor(addBtnColor);
        subButton.setBackgroundColor(subBtnColor);
    }

    /**
     * 设置监听回调
     */
    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.onNumChangeListener = onNumChangeListener;
    }


    /**
     * 设置监听器
     */
    private void setViewListener() {
        addButton.setOnClickListener(new OnButtonClickListener());
        subButton.setOnClickListener(new OnButtonClickListener());
    }


    /**
     * 监听器监听事件
     */
    private class OnButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            String numString = editText.getText().toString();
            num = Integer.parseInt(numString);
            if (TextUtils.isEmpty(numString)) {
                num = 1;
                editText.setText("1");
                editText.setSelection(editText.length());
            } else {
                if (v.getId() == R.id.add_btn_id) {
                    if (++num < 0) {
                        num--;
                        subButton.setEnabled(false);
                    } else {
                        subButton.setEnabled(true);
                        if (isAutoChangeNum) {
                            setNum(num);
                        } else {
                            setNum(num - 1);
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(CircleAddAndSubView.this, TYPE_ADD, getNum());
                        }
                    }
                } else if (v.getId() == R.id.sub_btn_id) {
                    if (--num < 1) {
                        subButton.setEnabled(false);
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(CircleAddAndSubView.this, TYPE_SUBTRACT, getNum());
                        }
                    } else {
                        subButton.setEnabled(true);
                        if (isAutoChangeNum) {
                            editText.setText(String.valueOf(num));
                            editText.setSelection(editText.length());
                        } else {
                            num++;
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(CircleAddAndSubView.this, TYPE_SUBTRACT, getNum());
                        }
                    }
                }
            }
        }
    }


    public interface OnNumChangeListener {
        /**
         * 监听方法
         *
         * @param type 点击按钮的类型
         * @param num  返回的数值
         */
        void onNumChange(View view, int type, int num);
    }

    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString())) {
                editText.setText("1");
            }
        }
    };
}
