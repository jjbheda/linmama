package com.xcxid.dinning.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcxid.dinning.R;

/**
 * Created by jingkang on 2017/3/4
 */

public class QuitOrderRefuseItem extends RelativeLayout {
    private TextView name;
    private TextView num;
    private TextView amount;
    private TextView result;

    public QuitOrderRefuseItem(Context context) {
        super(context, null);
        init(context);
    }

    public QuitOrderRefuseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QuitOrderRefuseItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.quitorder_refuse_item, this);
        name = (TextView) this.findViewById(R.id.tvName);
        num = (TextView) this.findViewById(R.id.tvNum);
        amount = (TextView) this.findViewById(R.id.tvAmount);
        result = (TextView) this.findViewById(R.id.tvResult);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setNum(String num) {
        this.num.setText(num);
    }

    public void setAmount(String amount) {
        this.amount.setText(amount);
    }

    public void setResult(String result) {
        this.result.setText(result);
    }
}
