package com.linmama.dinning.order.ordercompletesearch;

import android.view.View;
import android.widget.ListView;

import com.linmama.dinning.R;
import com.linmama.dinning.adapter.SearchCategoryAdapter;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchActivity;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchAdapter;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchContract;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchPresenter;
import com.linmama.dinning.widget.GetMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jiangjingbo on 2017/11/6.
 */

public class OrderCompleteSearchFragment extends BasePresenterFragment<OrderCompleteSearchPresenter>
        implements OrderCompleteSearchContract.SearchOrderView,View.OnClickListener {
    private List<TakingOrderBean> mResults = new ArrayList<>();
    @BindView(R.id.lvSearchOrderLt)
    GetMoreListView lvSearchOrderLt;
    private OrderUndoSearchAdapter mAdapter;

    @Override
    public void onClick(View v) {

    }

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

    }
    OrderCompleteSearchPresenter presenter;
    @Override
    protected OrderCompleteSearchPresenter loadPresenter() {
        presenter = new OrderCompleteSearchPresenter();
        return presenter;
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
