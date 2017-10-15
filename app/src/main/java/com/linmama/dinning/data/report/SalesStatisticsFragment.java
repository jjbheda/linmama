package com.linmama.dinning.data.report;

import android.text.TextUtils;
import android.widget.LinearLayout;

import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.TodayBean;
import com.linmama.dinning.widget.SalesItem;
import com.linmama.dinning.R;
import com.linmama.dinning.bean.CurrentMonthBean;
import com.linmama.dinning.bean.LastMonthBean;
import com.linmama.dinning.bean.SaleReportBean;
import com.linmama.dinning.bean.YesterdayBean;
import com.linmama.dinning.utils.ViewUtils;

import butterknife.BindView;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class SalesStatisticsFragment extends BasePresenterFragment<SaleReportPresenter> implements
        SaleReportContract.SaleReportView {
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.itemToday)
    SalesItem itemToday;
    @BindView(R.id.itemYestoday)
    SalesItem itemYestoday;
    @BindView(R.id.itemMonth)
    SalesItem itemMonth;
    @BindView(R.id.itemLastMonth)
    SalesItem itemLastMonth;

    @Override
    protected SaleReportPresenter loadPresenter() {
        return new SaleReportPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_data_statistics;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mPresenter.trunToReport();
    }

    @Override
    public void saleReportSuccess(SaleReportBean bean) {
        if (null != bean) {
            TodayBean today = bean.getToday();
            if (null != today) {
                itemToday.setToday(String.format(getString(R.string.data_today_sales), today.getSum()));
                itemToday.setBar(String.format(getString(R.string.data_bar_sales), today.getOffline_pay_sum()));
                itemToday.setOnline(String.format(getString(R.string.data_online_sales), today.getOnline_pay_sum()));
            }
            YesterdayBean yesterday = bean.getYesterday();
            if (null != yesterday) {
                itemYestoday.setToday(String.format(getString(R.string.data_yestoday_sales), yesterday.getSum()));
                itemYestoday.setBar(String.format(getString(R.string.data_bar_sales), yesterday.getOffline_pay_sum()));
                itemYestoday.setOnline(String.format(getString(R.string.data_online_sales), yesterday.getOnline_pay_sum()));
            }
            CurrentMonthBean currentMonth = bean.getCurrent_month();
            if (null != currentMonth) {
                itemMonth.setToday(String.format(getString(R.string.data_month_sales), currentMonth.getSum()));
                itemMonth.setBar(String.format(getString(R.string.data_bar_sales), currentMonth.getOffline_pay_sum()));
                itemMonth.setOnline(String.format(getString(R.string.data_online_sales), currentMonth.getOnline_pay_sum()));
            }
            LastMonthBean lastMonth = bean.getLast_month();
            if (null != lastMonth) {
                itemLastMonth.setToday(String.format(getString(R.string.data_lastmonth_sales), lastMonth.getLast_month_sum()));
                itemLastMonth.setBar(String.format(getString(R.string.data_bar_sales), lastMonth.getOnline_pay_sum()));
                itemLastMonth.setOnline(String.format(getString(R.string.data_online_sales), lastMonth.getOnline_pay_sum()));
            }
        }
    }

    @Override
    public void saleReportFail(String failMsg) {
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

}
