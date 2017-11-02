package com.linmama.dinning.order.ordersearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.goods.search.SearchCategoryPresenter;

/**
 * Created by jiangjingbo on 2017/10/29.
 */

public class OrderSearchActivity extends BasePresenterActivity<OrderSearchPresenter> {
    private int orderType = 0;      //0 当日单   1预约单

    @Override
    protected OrderSearchPresenter loadPresenter() {
        return new OrderSearchPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.order_search_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        if (getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            orderType = bundle.getInt("OrderType",0);     //0 当日单   1预约单
        }
    }

    @Override
    protected void initData() {

    }


}
