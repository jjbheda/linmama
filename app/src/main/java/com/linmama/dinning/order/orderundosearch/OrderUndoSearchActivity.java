package com.linmama.dinning.order.orderundosearch;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteFragment;
import com.linmama.dinning.order.ordercompletesearch.completesearch.OrderCompleteOrRefundSearchAdapter;
import com.linmama.dinning.order.today.TodayOrderContract;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
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
 * Created by jiangjingbo on 2017/10/29.
 *
 * 预约单/当日单 界面跳转的搜索   未完成订单查询
 *
 */

public class OrderUndoSearchActivity extends BasePresenterActivity<OrderUndoSearchPresenter>
        implements OrderUndoSearchContract.SearchOrderView,View.OnClickListener,TakingOrderAdapter.ICompleteOrder,
        GetMoreListView.OnGetMoreListener,TakingOrderAdapter.ICancelOrder, TakingOrderAdapter.IPrintOrder{
    private static String TAG = "OrderUndoSearchActivity";
    private int orderType = 0;      //0 当日单   1预约单
    private int currentPage = 1;
    private int last_page = 1;
    private TakingOrderAdapter mAdapter;
    private MyAlertDialog mAlert;
    private List<TakingOrderBean> mResults = new ArrayList<>();

    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView  mSearchOrderLt;

    @BindView(R.id.ptr_search_order)
    PtrClassicFrameLayout mPtrSearch;

    @BindView(R.id.etSearch)
    ClearEditText mEtSearch;

    OrderUndoSearchPresenter presenter;
    @Override
    protected OrderUndoSearchPresenter loadPresenter() {
        presenter = new OrderUndoSearchPresenter();
        return presenter;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.order_search_layout;
    }

    @Override
    protected void initView() {
        TextView tvSearchBtn = (TextView) findViewById(R.id.orderSearchtv);
        tvSearchBtn.setOnClickListener(this);
        final WindmillHeader header = new WindmillHeader(this);
        mPtrSearch.setHeaderView(header);
        mPtrSearch.addPtrUIHandler(header);
    }

    private boolean isPullRefresh = false;
    @Override
    protected void initListener() {
        mSearchOrderLt.setOnGetMoreListener(this);
        mPtrSearch.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    if (mSearchOrderLt!=null) {
                        mSearchOrderLt.setNoMore();
                    }
                    currentPage = 1;
                    isPullRefresh = true;
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                    presenter.getSearchOrderData(orderType,mEtSearch.getText().toString());
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            orderType = bundle.getInt("OrderType", 0);     //0 当日单   1预约单
        }

    }

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        dismissDialog();
        if (mAdapter == null || currentPage == 1 || isPullRefresh) {
            mAdapter = new TakingOrderAdapter(this, orderType, mResults);
            mSearchOrderLt.setAdapter(mAdapter);
        }

        if (currentPage == 1 && mPtrSearch.isRefreshing()) {
            mPtrSearch.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }

        isPullRefresh = false;
        last_page = bean.last_page;
        if (currentPage == 1 && bean.data.size() == 0) {
            if (mPtrSearch.getHeader() != null)
                mPtrSearch.getHeader().setVisibility(View.GONE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (null != bean) {
            LogUtils.d("getTakingOrderSuccess", bean.toString());
            List<TakingOrderBean> results = bean.data;
            mResults.addAll(results);
            mAdapter.setCommitOrder(this);
            mAdapter.setCancelOrder(this);
            mAdapter.setPrintOrder(this);

            if (currentPage > 1) {
                mSearchOrderLt.getMoreComplete();
            }

            if (currentPage == last_page) {
                mSearchOrderLt.setNoMore();
            } else {
                mSearchOrderLt.setHasMore();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getSearchOrderFail(String failMsg) {
        Toast.makeText(OrderUndoSearchActivity.this,failMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.orderSearchtv){
            if (mEtSearch.getText()!=null && presenter!=null){
                presenter.getSearchOrderData(orderType,mEtSearch.getText().toString());
            }
            InputMethodManager imm = (InputMethodManager) OrderUndoSearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            //隐藏软键盘 //
            imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.back_lt)
    public void back(){
        this.finish();
    }


    @Override
    public void onCompleteOrder(TakingOrderBean bean) {
        mPresenter.completeOrder(bean.id);
    }

    @Override
    public void onCancelOrder(final TakingOrderBean bean) {
        mAlert = new MyAlertDialog(this).builder()
                .setTitle("取消订单后款项将原路返回")
                .setConfirmButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseModel.httpService.cancelOrder(bean.id, 1).compose(new CommonTransformer())
                                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                                    @Override
                                    public void onNext(String msg) {
                                        Toast.makeText(OrderUndoSearchActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                                            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
                                            if (rb != null && rb.id == bean.id) {
                                                mAdapter.removeItem(i);
                                                mAdapter.notifyDataSetChanged();
                                                return;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        super.onError(e);
                                        Toast.makeText(OrderUndoSearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setPositiveButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        mAlert.show();
    }

    @Override
    public void onPrintOrder(TakingOrderBean bean) {
        PrintUtils.printOrder(TAG,bean);
        dismissDialog();
    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            mSearchOrderLt.setNoMore();
            return;
        }
        currentPage++;
        presenter.getSearchOrderData(orderType,mEtSearch.getText().toString());
    }

    public void completeOrderSuccess(int orderId) {
        dismissDialog();
        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
            if (rb != null && rb.id == orderId) {
                mAdapter.removeItem(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
        if (mResults.size() == 0) {
            if (mPtrSearch.getHeader() != null)
                mPtrSearch.getHeader().setVisibility(View.GONE);
        }
        Toast.makeText(OrderUndoSearchActivity.this,"已完成",Toast.LENGTH_SHORT).show();
    }

    public void completeOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrSearch, failMsg);
        }
    }
}