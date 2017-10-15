package com.linmama.dinning.bean;

/**
 * Created by jingkang on 2017/3/16
 */

public class RedDotStatusBean {
    private int new_order_count;
    private int order_warn_count;
    private int non_payment_order_count;
    private int refund_application_count;

    public int getNew_order_count() {
        return new_order_count;
    }

    public void setNew_order_count(int new_order_count) {
        this.new_order_count = new_order_count;
    }

    public int getNon_payment_order_count() {
        return non_payment_order_count;
    }

    public void setNon_payment_order_count(int non_payment_order_count) {
        this.non_payment_order_count = non_payment_order_count;
    }

    public int getOrder_warn_count() {
        return order_warn_count;
    }

    public void setOrder_warn_count(int order_warn_count) {
        this.order_warn_count = order_warn_count;
    }

    public int getRefund_application_count() {
        return refund_application_count;
    }

    public void setRefund_application_count(int refund_application_count) {
        this.refund_application_count = refund_application_count;
    }
}
