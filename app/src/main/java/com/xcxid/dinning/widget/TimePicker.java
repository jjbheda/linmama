package com.xcxid.dinning.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.utils.ViewUtils;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

/**
 * Created by jingkang on 2017/3/14
 */

public class TimePicker extends PopupWindow {
    private ICallBack iCallBack;

    public TimePicker(Activity context, int hour, int minute) {
        super(context);
        init(context, hour, minute);
    }

    private void init(Activity context, int hour, int minute) {
        View v = ViewUtils.layoutInflater(context, R.layout.pop_wheel_time, null);
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(v);
        this.setWidth(w);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        TextView btnDone = (TextView) v.findViewById(R.id.txtPopDone);
        TextView btnCancel = (TextView) v.findViewById(R.id.txtPopCancel);
        final WheelView wheelDay = (WheelView) v.findViewById(R.id.wv_day);
        final WheelView wheelHour = (WheelView) v.findViewById(R.id.wv_hour);
        final WheelView wheelMinute = (WheelView) v.findViewById(R.id.wv_minute);
        int visibleItemCnt = 7;
        wheelHour.setVisibleItems(visibleItemCnt);
        wheelMinute.setVisibleItems(visibleItemCnt);
        int wheelTxtColor = context.getResources().getColor(R.color.colorPickerSelected);
        int wheelTxtSize = 18;
        String[] days = {"今天", "明天"};
        ArrayWheelAdapter<String> dayWheelAdaper = new ArrayWheelAdapter<>(context, days);
        NumericWheelAdapter hourWheelAdapter = new NumericWheelAdapter(context, 0, 23, "%s 时");
        NumericWheelAdapter minuteWheelAdapter = new NumericWheelAdapter(context, 0, 59, "%s 分");
        dayWheelAdaper.setTextColor(wheelTxtColor);
        dayWheelAdaper.setTextSize(wheelTxtSize);
        hourWheelAdapter.setTextColor(wheelTxtColor);
        hourWheelAdapter.setTextSize(wheelTxtSize);
        minuteWheelAdapter.setTextColor(wheelTxtColor);
        minuteWheelAdapter.setTextSize(wheelTxtSize);
        wheelDay.setViewAdapter(dayWheelAdaper);
        wheelHour.setViewAdapter(hourWheelAdapter);
        wheelMinute.setViewAdapter(minuteWheelAdapter);
        wheelDay.setCyclic(false);
        wheelHour.setCyclic(true);
        wheelMinute.setCyclic(true);
        int[] shadowsColors = new int[]{0xFFBBBBBB, 0x00DDDDDD, 0x00DDDDDD};
        wheelDay.setShadowColor(shadowsColors[0], shadowsColors[1], shadowsColors[2]);
        wheelHour.setShadowColor(shadowsColors[0], shadowsColors[1], shadowsColors[2]);
        wheelMinute.setShadowColor(shadowsColors[0], shadowsColors[1], shadowsColors[2]);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker.this.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = wheelDay.getCurrentItem();
                int hour = wheelHour.getCurrentItem();
                int minute = wheelMinute.getCurrentItem();
                String chour = convert2Digitals(hour) + ":";
                String cminute = convert2Digitals(minute);
                if (null != iCallBack) {
                    iCallBack.pickTime(day, chour, cminute);
                }
                TimePicker.this.dismiss();
            }
        });
        wheelHour.setCurrentItem(hour);
        wheelMinute.setCurrentItem(minute);
    }

    public interface ICallBack {
        void pickTime(int d, String h, String m);
    }

    public void setCallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }

    public void showTimePicker(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, Gravity.BOTTOM, 18);
        } else {
            this.dismiss();
        }
    }

    private String convert2Digitals(int time) {
        if (time / 10 == 0) {
            return "0" + time;
        } else {
            return String.valueOf(time);
        }
    }
}
