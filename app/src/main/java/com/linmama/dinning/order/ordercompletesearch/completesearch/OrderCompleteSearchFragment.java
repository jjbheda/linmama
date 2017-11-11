package com.linmama.dinning.order.ordercompletesearch.completesearch;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteContract;
import com.linmama.dinning.order.ordercompletesearch.completesearch.OrderSearchCompletePresenter;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchActivity;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchAdapter;
import com.linmama.dinning.utils.LogUtils;
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
 * Created by jiangjingbo on 2017/11/11.
 */

public class OrderCompleteSearchFragment extends BasePresenterFragment<OrderSearchCompletePresenter>
        implements GetMoreListView.OnGetMoreListener,OrderCompleteContract.SearchOrderView, OrderCompleteContract.CancelView,OrderUndoSearchAdapter.ICancelFinishedOrder,OrderUndoSearchAdapter.IPrintOrder{

    @BindView(R.id.lv_order_search)
    GetMoreListView mLvSearchOrder;
    @BindView(R.id.ptr_search)
    PtrClassicFrameLayout mPtrSearchFt;
    @BindView(R.id.etSearch)
    ClearEditText mEtSearch;

    private List<TakingOrderBean> mResults = new ArrayList<>();
    private OrderUndoSearchAdapter mAdapter;
    private int currentPage = 1;

    @Override
    protected OrderSearchCompletePresenter loadPresenter() {
        return new OrderSearchCompletePresenter();
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

    @Override
    protected void initListener() {
        mLvSearchOrder.setOnGetMoreListener(this);
        mPtrSearchFt.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    currentPage = 1;
                    mPresenter.getSearchFinishedOrderListData(mEtSearch.getText().toString());
                }
            }
        });
    }

    @OnClick(R.id.orderSearchtv)
    public void getSearchData(){
        if (mEtSearch.getText()!=null){
            mPresenter.getSearchFinishedOrderListData(mEtSearch.getText().toString());
        }
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //隐藏软键盘 //
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

    }
    @Override
    protected void initData() {
    }

    @Override
    public void onGetMore() {

    }

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        dismissDialog();
        if (currentPage == 1 && mPtrSearchFt.isRefreshing()) {
            mPtrSearchFt.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }

        if (bean.data.size()>0){
            LogUtils.d("getTakingOrderSuccess", bean.data.toString());
            List<TakingOrderBean> results = bean.data;
            mResults.addAll(results);
            if (currentPage == 1 && results.size() == 0) {
                mPtrSearchFt.getHeader().setVisibility(View.GONE);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
            if (null == mAdapter) {
                mAdapter = new OrderUndoSearchAdapter(mActivity,0,mResults);
                mLvSearchOrder.setAdapter(mAdapter);
                mAdapter.setCancelOrder(this);
                mAdapter.setPrintOrder(this);
                mLvSearchOrder.setNoMore();
            } else {
                mAdapter.notifyDataSetChanged();
                mLvSearchOrder.setNoMore();
            }
        }
    }

    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(mActivity,failMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printOrder(TakingOrderBean bean) {

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
}
