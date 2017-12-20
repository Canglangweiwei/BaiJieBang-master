package com.water.helper.manager;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.jaydenxiao.common.commonutils.ToastUitl;
import com.water.helper.R;

import static android.view.Gravity.NO_GRAVITY;

@SuppressWarnings("ALL")
public class PopupWindowManager {

    private PopupWindow popupWindow;
    private PopCallback popCallback;
    private View view;
    private EditText et_main_type, et_main_number, et_main_severe_number;
    private Button cancel;
    private Button determine;

    public void setPopCallback(PopCallback popCallback) {
        this.popCallback = popCallback;
    }

    private Context context;

    private PopupWindowManager(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_input, null);
        et_main_type = (EditText) view.findViewById(R.id.et_main_type);
        et_main_number = (EditText) view.findViewById(R.id.et_main_number);
        et_main_severe_number = (EditText) view.findViewById(R.id.et_main_severe_number);
        cancel = (Button) view.findViewById(R.id.cancel);
        determine = (Button) view.findViewById(R.id.determine);
    }

    static class PopupWindowManagerHolder {

        public static PopupWindowManager instance = null;

        public static PopupWindowManager getInstance(Context context) {
            if (instance == null) {
                instance = new PopupWindowManager(context);
            }
            return instance;
        }
    }

    public static PopupWindowManager getInstance() {
        return PopupWindowManagerHolder.instance;
    }

    public static PopupWindowManager getInstance(Context context) {
        return PopupWindowManagerHolder.getInstance(context);
    }

    public void showPopupWindow(String type, String number, String serverNumber, View v) {
        et_main_type.setText(type);
        et_main_number.setText(number);
        et_main_severe_number.setText(serverNumber);
        MyOnClickListener onClickListener = new MyOnClickListener();
        cancel.setOnClickListener(onClickListener);
        determine.setOnClickListener(onClickListener);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(v, NO_GRAVITY, 0, 0);
    }

    public void showPopupWindow(View v) {
        showPopupWindow("", "", "0", v);
    }

    public interface PopCallback {
        void callBack(String type, String number, String serverNumber);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    break;
                case R.id.determine:
                    String typeText = et_main_type.getText().toString().trim();
                    String numberText = et_main_number.getText().toString().trim();
                    String serverText = et_main_severe_number.getText().toString().trim();

                    if (TextUtils.isEmpty(typeText)) {
                        ToastUitl.showShort("请输入类型");
                        return;
                    }
                    if (TextUtils.isEmpty(numberText)) {
                        ToastUitl.showShort("请输入数量");
                        return;
                    }
                    if (TextUtils.isEmpty(serverText)) {
                        ToastUitl.showShort("请输入重污数量");
                        return;
                    }
                    // 数据回调给主页面
                    try {
                        if (popCallback != null) {
                            popCallback.callBack(typeText, numberText, serverText);
                        }
                    } catch (Exception e) {
                        ToastUitl.showShort("数据异常");
                    }
                    break;
                default:
                    break;
            }
            if (popupWindow != null) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }
    }
}
