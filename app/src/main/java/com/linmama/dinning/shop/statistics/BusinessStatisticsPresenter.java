package com.linmama.dinning.shop.statistics;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.shop.bean.BusinessParseBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class BusinessStatisticsPresenter extends BasePresenter<BusinessStatisticsFragment> implements
       BusinessContract.SaleMonthRankPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new BusinessMonthStatisticsModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("SaleMonthRank", models[0]);
        return map;
    }

    @Override
    public void getSaleMonthRank(int month) {
        if (null == getIView())
            return;
        ((BusinessMonthStatisticsModel) getiModelMap().get("SaleMonthRank"))
                .getHistoryAnalysisData(month, new BusinessMonthStatisticsModel.SaleMonthRankHint() {
                    @Override
                    public void successSaleMonthRank(List<BusinessParseBean> beans) {
                        if (null == getIView())
                            return;
                        getIView().saleMonthRankSuccess(beans);
                    }

                    @Override
                    public void failSaleMonthRank(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().saleMonthRankFail(failMsg);
                    }
                });
    }
}
