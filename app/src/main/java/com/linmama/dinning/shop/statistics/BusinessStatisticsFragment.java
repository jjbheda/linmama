package com.linmama.dinning.shop.statistics;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.linmama.dinning.R;
import com.linmama.dinning.adapter.BusinessStatisticsAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.shop.bean.BusinessParseBean;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class BusinessStatisticsFragment extends BasePresenterFragment<BusinessStatisticsPresenter> implements
        RadioGroup.OnCheckedChangeListener,BusinessContract.SaleMonthRankView {
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
    protected BusinessStatisticsPresenter loadPresenter() {
        return new BusinessStatisticsPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_data_business;
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
        mPresenter.getSaleMonthRank(0);
    }

    @OnClick(R.id.rankMonth)
    public void getMonthRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleMonthRank(0);
    }

    @OnClick(R.id.rankLastMonth)
    public void getLastMonthRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleMonthRank(1);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }


    @Override
    public void saleMonthRankSuccess(List<BusinessParseBean> beans) {
        dismissDialog();
        if (null != beans && beans.size()>0) {
            BusinessStatisticsAdapter adapter = new BusinessStatisticsAdapter(mActivity, beans);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void saleMonthRankFail(String failMsg) {
        dismissDialog();
    }

}
