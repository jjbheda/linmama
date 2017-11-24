package com.linmama.dinning.order.ordercompletesearch;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.ordercompletesearch.completesearch.OrderCompleteSearchFragment;
import com.linmama.dinning.order.ordercompletesearch.refund.OrderRefundFragment;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchAdapter;
import com.linmama.dinning.utils.DateRangePicker;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.header.WindmillHeader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jiangjingbo on 2017/11/6.
 */

public class OrderCompleteFragment extends BasePresenterFragment<OrderCompletePresenter>
        implements OrderCompleteContract.SearchOrderView, OrderCompleteContract.CancelView,
        GetMoreListView.OnGetMoreListener,
        OrderUndoSearchAdapter.ICancelFinishedOrder, OrderUndoSearchAdapter.IPrintOrder {
    private List<TakingOrderBean> mResults = new ArrayList<>();
    private Calendar calendar;
    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView lvSearchOrderLt;
    @BindView(R.id.ptr_complete)
    PtrClassicFrameLayout mPreComplete;
    private OrderUndoSearchAdapter mAdapter;

    @BindView(R.id.nearly_tv)
    RadioButton mNearlyTv;

    @BindView(R.id.select_date)
    RadioButton mSelectDateTv;

    @BindView(R.id.tv_refundment)
    TextView mRefundmentTv;

    @BindView(R.id.date_selected_tv)
    TextView mSelectedDateTv;

    private String mStartDate = "";
    private String mEndDate = "";
    private int currentPage = 1;
    private int last_page = 1;

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        dismissDialog();
        if (currentPage == 1 && mPreComplete.isRefreshing()) {
            mPreComplete.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        last_page = bean.last_page;
        if (currentPage == 1 && bean.data.size() == 0) {
            if (mPreComplete.getHeader() != null)
                mPreComplete.getHeader().setVisibility(View.GONE);

            if (lvSearchOrderLt.getHeaderViewsCount() > 0) {
                lvSearchOrderLt.removeAllViews();
            }
            mPreComplete.setVisibility(View.GONE);
            lvSearchOrderLt.setNoMore();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            return;
        }
        mAdapter = new OrderUndoSearchAdapter(mActivity, 0, mResults);
        mPreComplete.setVisibility(View.VISIBLE);
        if (bean.data.size() > 0) {
            LogUtils.d("getTakingOrderSuccess", bean.data.toString());
            List<TakingOrderBean> results = bean.data;
            mResults.addAll(results);
            if (mPreComplete.getHeader() != null) {
                mPreComplete.getHeader().setVisibility(View.GONE);
            }
            lvSearchOrderLt.setAdapter(mAdapter);
            mAdapter.setCancelOrder(this);
            mAdapter.notifyDataSetChanged();
            if (currentPage > 1) {
                lvSearchOrderLt.getMoreComplete();
            }
            if (currentPage == last_page) {
                lvSearchOrderLt.setNoMore();
            }

        }
    }

    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(mActivity, failMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected OrderCompletePresenter loadPresenter() {
        return new OrderCompletePresenter();
    }

    @OnClick(R.id.select_date)
    public void showDateSelecterDiaglog() {
//        mNearlyTv.setBackgroundColor(mActivity.getResources().getColor(R.color.commonWhile));
//        mSelectDateTv.setBackgroundColor(mActivity.getResources().getColor(R.color.colorOrderTake));
        DateRangePicker picker = new DateRangePicker(mActivity, DateRangePicker.YEAR_MONTH_DAY, true);
        //选择器
        picker.setGravity(Gravity.CENTER);
        picker.setDateRangeStart(1997, 1, 1);
        picker.setDateRangeEnd(2030, 12, 30);
        picker.setTextSize(16);
        picker.setSelectedItem(year(), month(), day());
        picker.setSelectedSecondItem(year(), month(), day());
        picker.setOnDatePickListener(new DateRangePicker.OnYearMonthDayDoublePickListener() {
            @Override
            public void onDatePicked(String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) {
                mStartDate = startYear + "-" + startMonth + "-" + startDay;
                mEndDate = endYear + "-" + endMonth + "-" + endDay;
                if (!mStartDate.equals("") && !mEndDate.equals("")) {
                    mSelectedDateTv.setVisibility(View.VISIBLE);
                    mSelectedDateTv.setText(mStartDate + " 至 " + mEndDate);
                    mPresenter.getFinishedOrderListData(1, mStartDate, mEndDate);
                }
            }
        });
        picker.show();
    }

    private int year() {
        return calendar.get(Calendar.YEAR);
    }

    private int month() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    private int day() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @OnClick(R.id.order_search_lt)
    public void goToSearch() {
        CommonActivity.start(mActivity, OrderCompleteSearchFragment.class, new Bundle());
    }

    @OnClick(R.id.nearly_tv)
    public void getNearlyData() {
        mSelectedDateTv.setVisibility(View.GONE);
//        mNearlyTv.setBackgroundColor(mActivity.getResources().getColor(R.color.colorOrderTake));
//        mSelectDateTv.setBackgroundColor(mActivity.getResources().getColor(R.color.commonWhile));
        mPresenter.getFinishedOrderListData(1, "", "");
    }

    @OnClick(R.id.tv_refundment)
    public void getRefundmentData() {
//        mSelectedDateTv.setVisibility(View.GONE);
//        presenter.getRefundFailOrderListData(1);

        CommonActivity.start(mActivity, OrderRefundFragment.class, new Bundle());
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.order_complete_query_fragment;
    }

    @Override
    protected void initView() {
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPreComplete.setHeaderView(header);
        mPreComplete.addPtrUIHandler(header);
        calendar = Calendar.getInstance();
    }

    @Override
    protected void initListener() {
        lvSearchOrderLt.setOnGetMoreListener(this);
        mPreComplete.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    currentPage = 1;
                    mPresenter.getFinishedOrderListData(currentPage, mStartDate, mEndDate);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPreComplete.autoRefresh(true);
        mPresenter.getFinishedOrderListData(1, "", "");
    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            lvSearchOrderLt.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getFinishedOrderListData(currentPage, mStartDate, mEndDate);
    }

    @Override
    public void cancelOrder(TakingOrderBean bean) {
        mPresenter.cancelOrder(bean.id);
    }

    @Override
    public void cancelOrderSuccess(int id, String msg) {
        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
            if (rb.id == id) {
                mAdapter.removeItem(i);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void cancelOrderFail(String failMsg) {
        Toast.makeText(mActivity, failMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printOrder(TakingOrderBean bean) {

    }
}
