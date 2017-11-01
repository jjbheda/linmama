package com.linmama.dinning.order.ordersearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.base.BasePresenterActivity;

/**
 * Created by jiangjingbo on 2017/10/29.
 */

public class OrderSearchActivity extends BasePresenterActivity {
    private int orderType = 0;      //0 当日单   1预约单

    @Override
    protected BasePresenter loadPresenter() {
        return null;
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
