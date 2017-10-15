package com.linmama.dinning.bean;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleReportBean {
    private YesterdayBean yesterday;
    private LastMonthBean last_month;
    private TodayBean today;
    private CurrentMonthBean current_month;

    public YesterdayBean getYesterday() {
        return yesterday;
    }

    public void setYesterday(YesterdayBean yesterday) {
        this.yesterday = yesterday;
    }

    public LastMonthBean getLast_month() {
        return last_month;
    }

    public void setLast_month(LastMonthBean last_month) {
        this.last_month = last_month;
    }

    public TodayBean getToday() {
        return today;
    }

    public void setToday(TodayBean today) {
        this.today = today;
    }

    public CurrentMonthBean getCurrent_month() {
        return current_month;
    }

    public void setCurrent_month(CurrentMonthBean current_month) {
        this.current_month = current_month;
    }
}
