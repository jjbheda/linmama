package com.linmama.dinning.order.ordercompletesearch;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchAdapter;
import com.linmama.dinning.widget.GetMoreListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jiangjingbo on 2017/11/6.
 */

public class OrderCompleteSearchFragment extends BasePresenterFragment<OrderCompleteSearchPresenter>
        implements OrderCompleteSearchContract.SearchOrderView{
    private List<TakingOrderBean> mResults = new ArrayList<>();
    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView lvSearchOrderLt;
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

    private int currentQueryType = 0;       //已完成


    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        mResults.clear();
        mResults.addAll(bean.data);

        mAdapter = new OrderUndoSearchAdapter(mActivity, mResults);
        lvSearchOrderLt.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(mActivity,failMsg,Toast.LENGTH_SHORT).show();
    }
    OrderCompleteSearchPresenter presenter;
    @Override
    protected OrderCompleteSearchPresenter loadPresenter() {
        presenter = new OrderCompleteSearchPresenter();
        return presenter;
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
                            presenter.getFinishedOrderListData(1,mStartDate,mEndDate);
                        }
                    }
                }, // 设置年,月,日
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("请选择结束日期");
        dialog.show();
    }

    @OnClick(R.id.nearly_tv)
    public void getNearlyData() {
        mSelectedDateTv.setVisibility(View.GONE);
        presenter.getFinishedOrderListData(1,"","");
    }

    @OnClick(R.id.tv_refundment)
    public void getRefundmentData() {
//        mSelectedDateTv.setVisibility(View.GONE);
//        presenter.getRefundFailOrderListData(1);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.order_complete_query_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        presenter.getFinishedOrderListData(1,"","");
    }
}
