package com.linmama.dinning.order.ordercompletesearch.completesearch;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteContract;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.PrintUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.ClearEditText;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.MyAlertDialog;
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
        implements GetMoreListView.OnGetMoreListener, OrderCompleteContract.SearchOrderView, OrderCompleteContract.CancelView, OrderCompleteOrRefundSearchAdapter.ICancelFinishedOrder, OrderCompleteOrRefundSearchAdapter.IPrintOrder {

    public static String TAG = "OrderCompleteSearchFragment";
    @BindView(R.id.lv_order_search)
    GetMoreListView mLvSearchOrder;
    @BindView(R.id.ptr_search)
    PtrClassicFrameLayout mPtrSearchFt;
    @BindView(R.id.etSearch)
    ClearEditText mEtSearch;

    private List<TakingOrderBean> mResults = new ArrayList<>();
    private OrderCompleteOrRefundSearchAdapter mAdapter;
    private int currentPage = 1;
    private int last_page = 1;

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
                    mPresenter.getSearchFinishedOrderListData(mEtSearch.getText().toString());

                }
            }
        });
    }

    @OnClick(R.id.orderSearchtv)
    public void getSearchData() {
        if (mEtSearch.getText() != null) {
            currentPage = 1;
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
        if (currentPage == last_page) {
            mLvSearchOrder.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getSearchFinishedOrderListData(mEtSearch.getText().toString());
    }

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        dismissDialog();
        if (mAdapter == null || currentPage == 1 || isPullRefresh) {
            mAdapter = new OrderCompleteOrRefundSearchAdapter(mActivity, 0, mResults);
            mLvSearchOrder.setAdapter(mAdapter);
        }
        if (currentPage == 1 && mPtrSearchFt.isRefreshing()) {
            mPtrSearchFt.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        isPullRefresh = false;
        last_page = bean.last_page;

        if (currentPage == 1 && bean.data.size() == 0) {
            if (mPtrSearchFt.getHeader() != null)
                mPtrSearchFt.getHeader().setVisibility(View.GONE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (bean.data.size() > 0) {
            LogUtils.d("getTakingOrderSuccess", bean.data.toString());
            List<TakingOrderBean> results = bean.data;
            mResults.addAll(results);
            mAdapter.setCancelOrder(this);
            mAdapter.setPrintOrder(this);
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

    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(mActivity, failMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printOrder(TakingOrderBean bean) {
        bean.ordertype = 10;
        PrintUtils.printOrder(TAG,bean);
        dismissDialog();
    }
    private MyAlertDialog mAlert;
    @Override
    public void cancelOrder(final TakingOrderBean bean) {
        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("取消订单，款项将原路返回")
                .setConfirmButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.cancelOrder(bean.id);
                    }
                }).setPositiveButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        mAlert.show();
    }

    @Override
    public void cancelOrderSuccess(int id, String msg) {
//        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
//            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
//            if (rb.id == id) {
//                mAdapter.removeItem(i);
//                mAdapter.notifyDataSetChanged();
//                break;
//            }
//        }
        mAdapter.updateCancelButton(id);
    }

    @Override
    public void cancelOrderFail(String failMsg) {
        Toast.makeText(mActivity, failMsg, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.back_lt)
    public void back() {
        mActivity.finish();
    }
}
