package com.linmama.dinning.order.ordercompletesearch;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.ordercompletesearch.refund.OrderRefundFragment;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchAdapter;
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

public class OrderCompleteFragment extends BasePresenterFragment<OrderCompleteSearchPresenter>
        implements OrderCompleteSearchContract.SearchOrderView,OrderCompleteSearchContract.CancelView,
        GetMoreListView.OnGetMoreListener,OrderUndoSearchAdapter.ICancelFinishedOrder,OrderUndoSearchAdapter.IPrintOrder{
    private List<TakingOrderBean> mResults = new ArrayList<>();
    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView lvSearchOrderLt;
    @BindView(R.id.ptr_complete)
    PtrClassicFrameLayout mPreComplete;
    private OrderUndoSearchAdapter mAdapter;

    @BindView(R.id.nearly_tv)
    TextView mNearlyTv;

    @BindView(R.id.select_date)
    TextView mSelectDateTv;

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

        if (bean.data.size()>0){
            LogUtils.d("getTakingOrderSuccess", bean.data.toString());
            List<TakingOrderBean> results = bean.data;
            mResults.addAll(results);
            if (currentPage == 1 && results.size() == 0) {
                mPreComplete.getHeader().setVisibility(View.GONE);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
            if (null == mAdapter) {
                mAdapter = new OrderUndoSearchAdapter(mActivity,0,mResults);
                lvSearchOrderLt.setAdapter(mAdapter);
                mAdapter.setCancelOrder(this);
            } else {
                mAdapter.notifyDataSetChanged();
                if (currentPage > 1) {
                    lvSearchOrderLt.getMoreComplete();
                }

                if (currentPage == last_page) {
                    lvSearchOrderLt.setNoMore();
                }
            }
        }
    }

    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(mActivity,failMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected OrderCompleteSearchPresenter loadPresenter() {
        return  new OrderCompleteSearchPresenter();
    }

    @OnClick(R.id.select_date)
    public void showDateSelecterDiaglog(){
        final Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(mActivity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 设置
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        mStartDate = format.format(calendar.getTime());
                        showEndDialog();
                    }
                }, // 设置年,月,日
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("请选择开始日期");
        dialog.show();
//        final Calendar calendar = Calendar.getInstance();
//
//        DatePicker datePicker = new DatePicker(getActivity());
//        datePicker.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                mStartDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
//                mSelectedDateTv.setText(mStartDate +" 至 "+mEndDate);
//            }
//        });
//
//        new AlertDialog.Builder(getActivity())
//                .setTitle("Your title!!!")
//                .setView(datePicker)
//                .create().show();
    }

    private void showEndDialog(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(mActivity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 设置
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        mEndDate = format.format(calendar.getTime());
                        mSelectedDateTv.setVisibility(View.VISIBLE);
                        if (!mStartDate.equals("") && !mEndDate.equals("")) {
                            mSelectedDateTv.setText(mStartDate +" 至 "+mEndDate);
                            mPresenter.getFinishedOrderListData(1,mStartDate,mEndDate);
                        }
                    }
                }, // 设置年,月,日
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        TextView title = new TextView(mActivity);
        title.setText("请选择结束日期");
        dialog.setCustomTitle(title);
        dialog.show();
    }

    @OnClick(R.id.nearly_tv)
    public void getNearlyData() {
        mSelectedDateTv.setVisibility(View.GONE);
        mPresenter.getFinishedOrderListData(1,"","");
    }

    @OnClick(R.id.tv_refundment)
    public void getRefundmentData() {
//        mSelectedDateTv.setVisibility(View.GONE);
//        presenter.getRefundFailOrderListData(1);

        CommonActivity.start(mActivity, OrderRefundFragment.class,new Bundle());
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
                    mPresenter.getFinishedOrderListData(currentPage,mStartDate,mEndDate);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getFinishedOrderListData(1,"","");
    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            lvSearchOrderLt.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getFinishedOrderListData(currentPage,mStartDate,mEndDate);
    }

    @Override
    public void cancelOrder(TakingOrderBean bean) {
        mPresenter.cancelOrder(bean.id);
    }

    @Override
    public void cancelOrderSuccess(int id, String msg) {
        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
            if (rb.id  == id) {
                mAdapter.removeItem(i);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void cancelOrderFail(String failMsg) {
        Toast.makeText(mActivity,failMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printOrder(TakingOrderBean bean) {

    }
}
