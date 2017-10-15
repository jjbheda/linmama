package com.xcxid.dinning.bean;

/**
 * Created by jingkang on 2017/3/16
 */

public class RefundInfoBean {
    private int refunded_num;
    private int rejected_num;
    private int pending_num;

    public int getRefunded_num() {
        return refunded_num;
    }

    public void setRefunded_num(int refunded_num) {
        this.refunded_num = refunded_num;
    }

    public int getRejected_num() {
        return rejected_num;
    }

    public void setRejected_num(int rejected_num) {
        this.rejected_num = rejected_num;
    }

    public int getPending_num() {
        return pending_num;
    }

    public void setPending_num(int pending_num) {
        this.pending_num = pending_num;
    }
}
