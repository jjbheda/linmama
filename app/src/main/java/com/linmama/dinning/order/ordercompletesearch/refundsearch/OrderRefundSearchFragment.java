package com.linmama.dinning.order.ordercompletesearch.refundsearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteContract;
import com.linmama.dinning.order.ordercompletesearch.completesearch.OrderCompleteOrRefundSearchAdapter;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.PrintUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.ClearEditText;
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

public class OrderRefundSearchFragment extends BasePresenterFragment<OrderRefundSearchPresenter>
        implements OrderCompleteContract.SearchOrderView, OrderCompleteContract.RefundRetryView, GetMoreListView.OnGetMoreListener,
        OrderCompleteOrRefundSearchAdapter.IRefundRetry, OrderCompleteOrRefundSearchAdapter.IPrintOrder {
    public static String TAG = "OrderRefundSearchFragment";
    @BindView(R.id.lv_order_search)
    GetMoreListView mLvSearchOrder;
    @BindView(R.id.ptr_search)
    PtrClassicFrameLayout mPtrSearchFt;
    @BindView(R.id.etSearch)
    ClearEditText mEtSearch;
    private OrderCompleteOrRefundSearchAdapter mAdapter;
    List<TakingOrderBean> mResults = new ArrayList<>();

    private int currentPage = 1;
    private int last_page = 1;

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        dismissDialog();
        if (mAdapter == null || currentPage == 1 || isPullRefresh) {
            mAdapter = new OrderCompleteOrRefundSearchAdapter(mActivity, 1, mResults);
            mLvSearchOrder.setAdapter(mAdapter);
        }
        if (currentPage == 1 && mPtrSearchFt.isRefreshing()) {
            mPtrSearchFt.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        if (currentPage == 1 && bean.data.size() == 0) {
            mPtrSearchFt.getHeader().setVisibility(View.GONE);
            if (mAdapter != null) {
                mLvSearchOrder.setNoMore();
                mAdapter.notifyDataSetChanged();
            }
            return;
        }
        last_page = bean.last_page;
        isPullRefresh = false;
        if (bean.data.size() > 0) {
            LogUtils.d("getTakingOrderSuccess", bean.data.toString());
            List<TakingOrderBean> results = bean.data;
            mResults.addAll(results);
            mAdapter.setPrintOrder(this);
            mAdapter.setRefundRetry(this);
            if (currentPage > 1) {
                mLvSearchOrder.getMoreComplete();
            }

            if (currentPage == last_page) {
                mLvSearchOrder.setNoMore();
            } else {
                mLvSearchOrder.setHasMore();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.orderSearchtv)
    public void getSearchData() {
        if (mEtSearch.getText() != null) {
            mPresenter.getSearchRefundFailOrderListData(currentPage,mEtSearch.getText().toString());
        }
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //隐藏软键盘 //
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

    }

    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(mActivity, failMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected OrderRefundSearchPresenter loadPresenter() {
        return new OrderRefundSearchPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.order_finished_search_layout;
    }

    @Override
    protected void initView() {
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrSearchFt.setHeaderView(header);
        mPtrSearchFt.addPtrUIHandler(header);
    }

    private boolean isPullRefresh = false;
    @Override
    protected void initListener() {
        mLvSearchOrder.setOnGetMoreListener(this);
        mPtrSearchFt.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    currentPage = 1;
                    mResults.clear();
                    if (mLvSearchOrder!=null) {
                        mLvSearchOrder.setNoMore();
                    }
                    currentPage = 1;
                    isPullRefresh = true;
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                    dismissDialog();
                    mPresenter.getSearchRefundFailOrderListData(currentPage,mEtSearch.getText().toString());

                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            mLvSearchOrder.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getSearchRefundFailOrderListData(currentPage,mEtSearch.getText().toString());
    }

    @Override
    public void printOrder(TakingOrderBean bean) {
        bean.ordertype = 10;
        PrintUtils.printOrder(TAG,bean);
        dismissDialog();
    }

    @Override
    public void refundRetry(TakingOrderBean bean) {
        mPresenter.refundRetry(bean.id);
    }

    @Override
    public void refundRetrySuccess(int id, String msg) {
        if (msg.equals("退款成功")) {
            for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
                if (rb!=null && rb.id == id) {
                    mAdapter.removeItem(i);
                    mAdapter.notifyDataSetChanged();
                }
            }

        } else {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void refundRetryFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrSearchFt, failMsg);
        }
    }
    @OnClick(R.id.back_lt)
    public void back(){
        mActivity.finish();
    }
}
