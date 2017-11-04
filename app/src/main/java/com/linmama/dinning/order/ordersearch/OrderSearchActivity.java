package com.linmama.dinning.order.ordersearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.goods.search.SearchCategoryPresenter;
import com.linmama.dinning.widget.ClearEditText;
import com.linmama.dinning.widget.GetMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jiangjingbo on 2017/10/29.
 */

public class OrderSearchActivity extends BasePresenterActivity<OrderSearchPresenter>
        implements OrderSearchContract.SearchOrderView,View.OnClickListener {
    private int orderType = 0;      //0 当日单   1预约单
    private OrderSearchAdapter mAdapter;
    private List<TakingOrderBean> mResults = new ArrayList<>();

    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView  mSearchOrderLt;

    @BindView(R.id.etSearch)
    ClearEditText mEtSearch;

    OrderSearchPresenter presenter;
    @Override
    protected OrderSearchPresenter loadPresenter() {
        presenter = new OrderSearchPresenter();
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
    public void getSearchOrderSuccess(List<TakingOrderBean> beans) {
        mResults.clear();
        mResults.addAll(beans);

        mAdapter = new OrderSearchAdapter(OrderSearchActivity.this, mResults);
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
        }
    }
}