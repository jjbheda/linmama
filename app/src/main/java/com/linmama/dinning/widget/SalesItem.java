package com.linmama.dinning.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.R;

/**
 * Created by jingkang on 2017/3/4
 */

public class SalesItem extends RelativeLayout {
    private TextView today;
    private TextView online;
    private TextView bar;

    public SalesItem(Context context) {
        super(context, null);
        init(context);
    }

    public SalesItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SalesItem);
        String _today = ta.getString(R.styleable.SalesItem_salesToday);
        today.setText(_today);
        String _online = ta.getString(R.styleable.SalesItem_salesOnline);
        online.setText(_online);
        String _bar = ta.getString(R.styleable.SalesItem_salesBar);
        bar.setText(_bar);
        ta.recycle();
    }

    public SalesItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setToday(String today) {
        this.today.setText(today);
    }

    public void setOnline(String online) {
        this.online.setText(online);
    }

    public void setBar(String bar) {
        this.bar.setText(bar);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sales_item, this);
        today = (TextView) this.findViewById(R.id.todaySales);
        online = (TextView) this.findViewById(R.id.olSales);
        bar = (TextView) this.findViewById(R.id.barSales);
    }

}
