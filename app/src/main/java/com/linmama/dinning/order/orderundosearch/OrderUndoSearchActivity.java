package com.linmama.dinning.order.orderundosearch;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.widget.ClearEditText;
import com.linmama.dinning.widget.GetMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jiangjingbo on 2017/10/29.
 */

public class OrderUndoSearchActivity extends BasePresenterActivity<OrderUndoSearchPresenter>
        implements OrderUndoSearchContract.SearchOrderView,View.OnClickListener {
    private int orderType = 0;      //0 当日单   1预约单
    private OrderUndoSearchAdapter mAdapter;
    private List<TakingOrderBean> mResults = new ArrayList<>();

    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView  mSearchOrderLt;

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
    }

    @Override
    protected void initListener() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            orderType = bundle.getInt("OrderType", 0);     //0 当日单   1预约单
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void getSearchOrderSuccess(TakingOrderMenuBean bean) {
        mResults.clear();
        mResults.addAll(bean.data);

        mAdapter = new OrderUndoSearchAdapter(OrderUndoSearchActivity.this,1, mResults);
        mSearchOrderLt.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void getSearchOrderFail(String failMsg) {


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.orderSearchtv){
            if (mEtSearch.getText()!=null){
                presenter.getSearchOrderData(orderType,mEtSearch.getText().toString());
            }
            InputMethodManager imm = (InputMethodManager) OrderUndoSearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            //隐藏软键盘 //
            imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

        }
    }
}