package com.linmama.dinning.order.ordercompletesearch.refund;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.OrderOrderMenuBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteSearchContract;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteSearchPresenter;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchAdapter;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.header.WindmillHeader;

import org.greenrobot.eventbus.EventBus;

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

public class OrderRefundFragment extends BasePresenterFragment<OrderCompleteRefundSearchPresenter>
        implements OrderCompleteSearchContract.SearchOrderView,OrderCompleteSearchContract.RefundRetryView,GetMoreListView.OnGetMoreListener,
        OrderUndoSearchAdapter.IRefundRetry,OrderUndoSearchAdapter.IPrintOrder{

    @BindView(R.id.ptr_refund)
    PtrClassicFrameLayout mPreRefundLt;
    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView lvSearchOrderLt;
    private OrderUndoSearchAdapter mAdapter;
    List<TakingOrderBean> mResults = new ArrayList<>();

    private int currentPage = 1;
    private int last_page = 1;

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        dismissDialog();
        if (currentPage == 1 && mPreRefundLt.isRefreshing()) {
            mPreRefundLt.refreshComplete();
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
                mPreRefundLt.getHeader().setVisibility(View.GONE);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
            if (null == mAdapter) {
                mAdapter = new OrderUndoSearchAdapter(mActivity,1,mResults);
                mAdapter.setPrintOrder(this);
                mAdapter.setRefundRetry(this);
                lvSearchOrderLt.setAdapter(mAdapter);
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
    protected OrderCompleteRefundSearchPresenter loadPresenter() {
        return new OrderCompleteRefundSearchPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.order_complete_refund_query_fragment;
    }

    @Override
    protected void initView() {
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPreRefundLt.setHeaderView(header);
        mPreRefundLt.addPtrUIHandler(header);
    }

    @Override
    protected void initListener() {
        lvSearchOrderLt.setOnGetMoreListener(this);
        mPreRefundLt.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    currentPage = 1;
                    mPresenter.getRefundFailOrderListData(currentPage);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getRefundFailOrderListData(currentPage);
    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            lvSearchOrderLt.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getRefundFailOrderListData(currentPage);
    }

    @Override
    public void printOrder(TakingOrderBean bean) {

    }

    @Override
    public void refundRetry(TakingOrderBean bean) {
     mPresenter.refundRetry(bean.id);
    }

    @Override
    public void refundRetrySuccess(int id,String msg) {
        if (msg.equals("退款成功")) {
            for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
                if (rb.id  == id) {
                    mAdapter.removeItem(i);
                    mAdapter.notifyDataSetChanged();
                }
            }

        } else {
            Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void refundRetryFail(String failMsg) {

    }
}
