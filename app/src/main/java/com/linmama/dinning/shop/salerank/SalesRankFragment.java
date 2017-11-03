package com.linmama.dinning.shop.salerank;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.linmama.dinning.R;
import com.linmama.dinning.adapter.SaleRankAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.shop.bean.SaleRankBean;
import com.linmama.dinning.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class SalesRankFragment extends BasePresenterFragment<SaleRankPresenter> implements
        RadioGroup.OnCheckedChangeListener, SaleRankContract.SaleRankView{
    @BindView(R.id.rankToday)
    RadioButton rankToday;
    @BindView(R.id.rankYesterday)
    RadioButton rankYesterday;
    @BindView(R.id.rankMonth)
    RadioButton rankMonth;
    @BindView(R.id.rankLastMonth)
    RadioButton rankLastMonth;
    @BindView(R.id.rankGroup)
    RadioGroup rankGroup;
    @BindView(android.R.id.list)
    ListView list;

    @BindView(R.id.titleGoods)
    Toolbar toolbar;

    @Override
    protected SaleRankPresenter loadPresenter() {
        return new SaleRankPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_data_rank;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        rankGroup.setOnCheckedChangeListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }


    @Override
    protected void initData() {
        mPresenter.getSaleRank(0);
    }

    @OnClick(R.id.rankToday)
    public void getTodayRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleRank(0);
    }

    @OnClick(R.id.rankYesterday)
    public void getYesterdayRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleRank(1);
    }

    @OnClick(R.id.rankMonth)
    public void getMonthRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleRank(2);
    }

    @OnClick(R.id.rankLastMonth)
    public void getLastMonthRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleRank(3);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public void saleRankSuccess(List<SaleRankBean>  beans) {
        dismissDialog();
        if (null != beans && beans.size()>0) {
            SaleRankAdapter adapter = new SaleRankAdapter(mActivity, beans);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void saleRankFail(String failMsg) {
        dismissDialog();
    }

}
