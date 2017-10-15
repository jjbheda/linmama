package com.linmama.dinning.data.rank;

import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.SaleRankAdapter;
import com.linmama.dinning.bean.SaleRankBean;
import com.linmama.dinning.bean.SaleRankResultsBean;
import com.linmama.dinning.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.linmama.dinning.utils.TimeUtils.getLastMonth;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class SalesRankFragment extends BasePresenterFragment<SaleRankPresenter> implements
        RadioGroup.OnCheckedChangeListener, SaleRankContract.SaleDayRankView, SaleRankContract.SaleMonthRankView {
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
    }

    @Override
    protected void initData() {
        mPresenter.getSaleDayRank(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FROMAT_Default));
    }

    @OnClick(R.id.rankToday)
    public void getTodayRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleDayRank(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FROMAT_Default));
    }

    @OnClick(R.id.rankYesterday)
    public void getYesterdayRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleDayRank(TimeUtils.getYesterdayTimeInString());
    }

    @OnClick(R.id.rankMonth)
    public void getMonthRank(View view) {
        showDialog("加载中...");
        mPresenter.getSaleMonthRank(TimeUtils.getCurrentYear(), TimeUtils.getCurrentMonth());
    }

    @OnClick(R.id.rankLastMonth)
    public void getLastMonthRank(View view) {
        showDialog("加载中...");
        int[] date = getLastMonth();
        mPresenter.getSaleMonthRank(date[0], date[1]);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public void saleDayRankSuccess(SaleRankBean bean) {
        dismissDialog();
        if (null != bean && null != bean.getResults()) {
            List<SaleRankResultsBean> beans = bean.getResults();
            SaleRankAdapter adapter = new SaleRankAdapter(mActivity, beans);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void saleDayRankFail(String failMsg) {
        dismissDialog();
    }

    @Override
    public void saleMonthRankSuccess(SaleRankBean bean) {
        dismissDialog();
        if (null != bean && null != bean.getResults()) {
            List<SaleRankResultsBean> beans = bean.getResults();
            SaleRankAdapter adapter = new SaleRankAdapter(mActivity, beans);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void saleMonthRankFail(String failMsg) {
        dismissDialog();
    }

}
