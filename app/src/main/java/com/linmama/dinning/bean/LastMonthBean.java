package com.linmama.dinning.bean;

/**
 * Created by jingkang on 2017/3/14
 */

public class LastMonthBean {
    private String online_pay_sum = "";
    private String last_month_sum = "";
    private String offline_pay_sum = "";

    public String getOnline_pay_sum() {
        return online_pay_sum;
    }

    public void setOnline_pay_sum(String online_pay_sum) {
        this.online_pay_sum = online_pay_sum;
    }

    public String getLast_month_sum() {
        return last_month_sum;
    }

    public void setLast_month_sum(String last_month_sum) {
        this.last_month_sum = last_month_sum;
    }

    public String getOffline_pay_sum() {
        return offline_pay_sum;
    }

    public void setOffline_pay_sum(String offline_pay_sum) {
        this.offline_pay_sum = offline_pay_sum;
    }
}
