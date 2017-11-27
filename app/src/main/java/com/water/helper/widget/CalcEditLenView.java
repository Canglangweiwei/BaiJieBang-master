package com.water.helper.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.water.helper.R;

/**
 * 自定义带剩余字符提示的editText
 */
@SuppressWarnings("ALL")
public class CalcEditLenView extends RelativeLayout {

    private EditText editText;
    private TextView leftText;

    private int MAX_COUNT = 60;

    public int getMAX_COUNT() {
        return MAX_COUNT;
    }

    public void setMAX_COUNT(int MAX_COUNT) {
        this.MAX_COUNT = MAX_COUNT;
        leftText.setText("还可以输入"
                + String.valueOf((MAX_COUNT - getInputCount())) + "个字符！");
    }

    public CalcEditLenView(Context context) {
        super(context);
        initView();
    }

    public CalcEditLenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CalcEditLenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalcEditLenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View mRootView = inflate(getContext(), R.layout.widget_calc_edit, null);
        editText = (EditText) mRootView.findViewById(R.id.edit_content);
        leftText = (TextView) mRootView.findViewById(R.id.left_count);

        addView(mRootView);
        init();
    }

    private void init() {
        editText.setSingleLine(false);
        editText.addTextChangedListener(mTextWatcher);
        editText.setSelection(editText.length());
    }

    /**
     * EditText的监听事件
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;
        private int editEnd;

        @Override
        public void afterTextChanged(Editable s) {
            editStart = editText.getSelectionStart();
            editEnd = editText.getSelectionEnd();

			/* 先去掉监听器，否则会出现栈溢出 */
            editText.removeTextChangedListener(mTextWatcher);

            /**
             * 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
             * 因为是中英文混合，单个字符而言，calculateLength函数都会返回1 当输入字符个数超过限制的大小时，
             * 进行截断操作
             */
            while (calculateLength(s.toString()) > MAX_COUNT) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            /* 将这行代码注释掉就不会出现后面所说的输入法在数字界面自动跳转回主界面的问题了 */
            // mEditText.setText(s);
            editText.setSelection(editStart);
            /* 恢复监听器 */
            editText.addTextChangedListener(mTextWatcher);
            setLeftCount();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    /**
     * 计算字符个数
     *
     * @param c 字符
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 标记剩余字符
     */
    private void setLeftCount() {
        leftText.setText("还可以输入"
                + String.valueOf((MAX_COUNT - getInputCount())) + "个字符！");
    }

    /**
     * 计算输入的字符个数
     */
    private long getInputCount() {
        return calculateLength(editText.getText().toString().trim());
    }

    public String getEditTextContent() {
        String editContent = editText.getText().toString().trim();
        if (TextUtils.isEmpty(editContent))
            return "";
        return editContent;
    }

    public void clear() {
        editText.setText("");
        leftText.setText("还可以输入" + MAX_COUNT + "个字符！");
    }
}
