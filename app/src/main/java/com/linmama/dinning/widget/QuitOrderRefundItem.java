package com.linmama.dinning.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.R;

/**
 * Created by jingkang on 2017/3/4
 */

public class QuitOrderRefundItem extends RelativeLayout {
    private TextView name;
    private TextView num;
    private TextView amount;
    private Button ignore;
    private Button refund;

    public QuitOrderRefundItem(Context context) {
        super(context, null);
        init(context);
    }

    public QuitOrderRefundItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QuitOrderRefundItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.quitorder_refund_item, this);
        name = (TextView) this.findViewById(R.id.tvName);
        num = (TextView) this.findViewById(R.id.tvNum);
        amount = (TextView) this.findViewById(R.id.tvAmount);
        ignore = (Button) this.findViewById(R.id.btnIgnore);
        refund = (Button) this.findViewById(R.id.btnRefund);
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

    public Button getIgnore() {
        return ignore;
    }

    public Button getRefund() {
        return refund;
    }
}
