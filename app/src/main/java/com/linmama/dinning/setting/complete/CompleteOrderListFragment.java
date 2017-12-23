package com.linmama.dinning.setting.complete;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.linmama.dinning.R;
import com.linmama.dinning.adapter.CompleteOrderAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.CompleteOrderBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.header.WindmillHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompleteOrderListFragment extends BasePresenterFragment<CompleteOrderListPresenter> implements
        CompleteOrderListContract.CompleteListOrderView, GetMoreListView.OnGetMoreListener,
        CompleteOrderAdapter.IPosOrder, AdapterView.OnItemClickListener, CompleteOrderListContract.PrintView{
    @BindView(R.id.lvCompleteOrder)
    GetMoreListView lvCompleteOrder;
    @BindView(R.id.ptr_complete)
    PtrClassicFrameLayout ptrComplete;

    private CompleteOrderAdapter mAdapter;
    private int currentPage = 1;
    private List<ResultsBean> mResults;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void posOrder(final int position) {

    }

    @Override
    protected CompleteOrderListPresenter loadPresenter() {
        return new CompleteOrderListPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_complete;
    }

    @Override
    protected void initView() {
        mResults = new ArrayList<>();
        final WindmillHeader header = new WindmillHeader(mActivity);
        ptrComplete.setHeaderView(header);
        ptrComplete.addPtrUIHandler(header);
    }

    @Override
    protected void initListener() {
        lvCompleteOrder.setOnGetMoreListener(this);
        ptrComplete.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
//                    showDialog("加载中...");
                    currentPage = 1;
                    mPresenter.getCompleteOrderList(currentPage);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            ptrComplete.autoRefresh(true);
        }
    }

    @Override
    public void getCompleteOrderListSuccess(CompleteOrderBean bean) {
        dismissDialog();
        if (currentPage == 1 && ptrComplete.isRefreshing()) {
            ptrComplete.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        if (TextUtils.isEmpty(bean.getNext())) {
            lvCompleteOrder.setNoMore();
        } else {
            lvCompleteOrder.setHasMore();
        }
        if (null != bean && null != bean.getResults()) {
            List<ResultsBean> results = bean.getResults();
            mResults.addAll(results);
            LogUtils.d("Results", results.size() + "");
            if (null == mAdapter) {
                mAdapter = new CompleteOrderAdapter(mActivity, mResults);
                mAdapter.setPosOrder(this);
                lvCompleteOrder.setAdapter(mAdapter);
                lvCompleteOrder.setOnItemClickListener(this);
            } else {
                mAdapter.notifyDataSetChanged();
                if (currentPage > 1) {
                    lvCompleteOrder.getMoreComplete();
                }
            }
        }
    }

    @Override
    public void getCompleteOrderListFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(ptrComplete, failMsg);
        }
    }

    @Override
    public void getPrintDataSuccess(OrderDetailBean bean) {
     dismissDialog();
    }

    @Override
    public void getPrintDataFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(ptrComplete, failMsg);
        }
    }

    @Override
    public void onGetMore() {
        currentPage++;
        mPresenter.getCompleteOrderList(currentPage);
    }
}
