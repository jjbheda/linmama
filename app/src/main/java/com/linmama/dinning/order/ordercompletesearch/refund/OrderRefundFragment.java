package com.linmama.dinning.order.ordercompletesearch.refund;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteContract;
import com.linmama.dinning.order.ordercompletesearch.refundsearch.OrderRefundSearchFragment;
import com.linmama.dinning.order.ordercompletesearch.completesearch.OrderCompleteOrRefundSearchAdapter;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.header.WindmillHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jiangjingbo on 2017/11/6.
 */

public class OrderRefundFragment extends BasePresenterFragment<OrderCompleteRefundPresenter>
        implements OrderCompleteContract.SearchOrderView,OrderCompleteContract.RefundRetryView,GetMoreListView.OnGetMoreListener,
        OrderCompleteOrRefundSearchAdapter.IRefundRetry,OrderCompleteOrRefundSearchAdapter.IPrintOrder{
    private static String TAG = "OrderRefundFragment";
    @BindView(R.id.ptr_refund)
    PtrClassicFrameLayout mPreRefundLt;
    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView lvSearchOrderLt;
    private OrderCompleteOrRefundSearchAdapter mAdapter;
    List<TakingOrderBean> mResults = new ArrayList<>();

    private int currentPage = 1;
    private int last_page = 1;

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        dismissDialog();
        if (mAdapter == null || currentPage == 1 || isPullRefresh) {
            mAdapter = new OrderCompleteOrRefundSearchAdapter(mActivity, 1, mResults);
            lvSearchOrderLt.setAdapter(mAdapter);
        }
        if (currentPage == 1 && mPreRefundLt.isRefreshing()) {
            mPreRefundLt.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }

        if (currentPage == 1 && bean.data.size() == 0) {
            mPreRefundLt.getHeader().setVisibility(View.GONE);
            if (mAdapter != null) {
                lvSearchOrderLt.setNoMore();
                mAdapter.notifyDataSetChanged();
            }
            return;
        }

        last_page = bean.last_page;
        isPullRefresh = false;
        if (bean.data.size()>0){
            LogUtils.d("getTakingOrderSuccess", bean.data.toString());
            List<TakingOrderBean> results = bean.data;
            mResults.addAll(results);
            mAdapter.setPrintOrder(this);
            mAdapter.setRefundRetry(this);
            if (currentPage > 1) {
                lvSearchOrderLt.getMoreComplete();
            }

            if (currentPage == last_page) {
                lvSearchOrderLt.setNoMore();
            } else {
                lvSearchOrderLt.setHasMore();
            }
            mAdapter.notifyDataSetChanged();

        }
    }

    @OnClick(R.id.lt_taking_search)
    public void goSearch(){
        CommonActivity.start(mActivity, OrderRefundSearchFragment.class,new Bundle());
    }
    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(mActivity,failMsg,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected OrderCompleteRefundPresenter loadPresenter() {
        return new OrderCompleteRefundPresenter();
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
    private boolean isPullRefresh = false;
    @Override
    protected void initListener() {
        lvSearchOrderLt.setOnGetMoreListener(this);
        mPreRefundLt.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    currentPage = 1;
                    mResults.clear();
                    if (lvSearchOrderLt!=null) {
                        lvSearchOrderLt.setNoMore();
                    }
                    isPullRefresh = true;
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
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
        bean.ordertype = 10;
//        PrintUtils.printOrder(TAG,bean);
        FeiEPrinterUtils.FeiprintOrderWithLoading(mActivity,bean);
        dismissDialog();
    }

    @Override
    public void refundRetry(TakingOrderBean bean) {
     mPresenter.refundRetry(bean.id);
    }

    @Override
    public void refundRetrySuccess(int id,String msg) {
        if (msg.equals("退款成功")) {
//            for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
//                TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
//            if (rb != null && rb.id == id) {
//                    mAdapter.removeItem(i);
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
            mAdapter.updateCancelButton(id);

        } else {
            ViewUtils.showSnack(mPreRefundLt, msg);
        }

    }

    @Override
    public void refundRetryFail(String failMsg) {
        ViewUtils.showSnack(mPreRefundLt, failMsg);
    }
}
