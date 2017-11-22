package com.wevey.selector.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weavey.wheel.StrericWheelAdapter;
import com.weavey.wheel.WheelView;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 功能描述：时间对话框
 * </p>
 * Created by Administrator on 2017/5/27 0027.
 */
@SuppressWarnings("ALL")
public class TimePickerDialog extends Dialog {

    private int minYear = 2000;  //最小年份
    private TextView textHour, textMinute;
    private WheelView yearWheel, monthWheel, dayWheel, hourWheel, minuteWheel;
    private static String[] yearContent = null;
    private static String[] monthContent = null;
    private static String[] dayContent = null;
    private static String[] hourContent = null;
    private static String[] minuteContent = null;

    private DateType dateType = DateType.DATE;

    public void setDateType(DateType dateType) {
        this.dateType = dateType;
        switch (dateType) {
            case TIME:
                hourWheel.setVisibility(View.VISIBLE);
                minuteWheel.setVisibility(View.VISIBLE);

                textHour.setVisibility(View.VISIBLE);
                textMinute.setVisibility(View.VISIBLE);
                break;
            case DATE:
                hourWheel.setVisibility(View.GONE);
                minuteWheel.setVisibility(View.GONE);

                textHour.setVisibility(View.GONE);
                textMinute.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private OnTimePickerDialogListener listener;

    public void setListener(OnTimePickerDialogListener listener) {
        this.listener = listener;
    }

    public TimePickerDialog(Context context) {
        super(context);
        initDialog(context);
    }

    protected TimePickerDialog(Context context, int theme) {
        super(context, theme);
        initDialog(context);
    }

    protected TimePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文环境
     */
    private void initDialog(Context context) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
        setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getHeight();
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            onWindowAttributesChanged(wl);
        }
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {

        // 确定
        TextView dtp_sure = (TextView) findViewById(R.id.dtp_sure);
        // 取消
        TextView dtp_cancel = (TextView) findViewById(R.id.dtp_cancel);
        dtp_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                StringBuffer sb = new StringBuffer();
                sb.append(yearWheel.getCurrentItemValue()).append("-")
                        .append(monthWheel.getCurrentItemValue()).append("-")
                        .append(dayWheel.getCurrentItemValue());

                // 校验时间格式是否正确
                if (!validateDate(sb.toString())) {
                    if (listener != null) {
                        listener.onErrorDate(sb.toString());
                    }
                    return;
                }
                // 返回选择的时间或者日期
                switch (dateType) {
                    case TIME:// 时间
                        sb.append(" ");
                        sb.append(hourWheel.getCurrentItemValue())
                                .append(":").append(minuteWheel.getCurrentItemValue())
                                .append(":").append("00");
                        break;
                    case DATE:// 日期
                    default:
                        break;
                }
                if (listener != null) {
                    listener.onTimeSelect(sb.toString());
                }
            }
        });
        dtp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // 年
        yearWheel = (WheelView) findViewById(R.id.yearwheel);
        // 月
        monthWheel = (WheelView) findViewById(R.id.monthwheel);
        // 日
        dayWheel = (WheelView) findViewById(R.id.daywheel);
        // 时
        hourWheel = (WheelView) findViewById(R.id.hourwheel);
        // 分
        minuteWheel = (WheelView) findViewById(R.id.minutewheel);
        // 时
        textHour = (TextView) findViewById(R.id.textHour);
        // 分
        textMinute = (TextView) findViewById(R.id.textMinute);

        initContent();

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);

        yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
        yearWheel.setCurrentItem(curYear - minYear);
        yearWheel.setCyclic(true);
        yearWheel.setInterpolator(new AnticipateOvershootInterpolator());

        monthWheel.setAdapter(new StrericWheelAdapter(monthContent));
        monthWheel.setCurrentItem(curMonth - 1);
        monthWheel.setCyclic(true);
        monthWheel.setInterpolator(new AnticipateOvershootInterpolator());

        dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
        dayWheel.setCurrentItem(curDay - 1);
        dayWheel.setCyclic(true);
        dayWheel.setInterpolator(new AnticipateOvershootInterpolator());

        hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
        hourWheel.setCurrentItem(curHour);
        hourWheel.setCyclic(true);
        hourWheel.setInterpolator(new AnticipateOvershootInterpolator());

        minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
        minuteWheel.setCurrentItem(curMinute);
        minuteWheel.setCyclic(true);
        minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    /**
     * 初始化时间范围
     */
    private void initContent() {
        yearContent = new String[50];
        for (int i = 0; i < 50; i++) {
            yearContent[i] = String.valueOf(i + minYear);
        }
        monthContent = new String[12];
        for (int i = 0; i < 12; i++) {
            monthContent[i] = String.valueOf(i + 1);
            if (monthContent[i].length() < 2) {
                monthContent[i] = "0" + monthContent[i];
            }
        }

        dayContent = new String[31];
        for (int i = 0; i < 31; i++) {
            dayContent[i] = String.valueOf(i + 1);
            if (dayContent[i].length() < 2) {
                dayContent[i] = "0" + dayContent[i];
            }
        }

        hourContent = new String[24];
        for (int i = 0; i < 24; i++) {
            hourContent[i] = String.valueOf(i);
            if (hourContent[i].length() < 2) {
                hourContent[i] = "0" + hourContent[i];
            }
        }

        minuteContent = new String[60];
        for (int i = 0; i < 60; i++) {
            minuteContent[i] = String.valueOf(i);
            if (minuteContent[i].length() < 2) {
                minuteContent[i] = "0" + minuteContent[i];
            }
        }
    }

    /**
     * 判断选择的日期是否正确
     */
    private static boolean validateDate(String sDate) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }

    public interface OnTimePickerDialogListener {
        void onTimeSelect(String timeSelect);

        void onErrorDate(String errorDate);
    }
}
