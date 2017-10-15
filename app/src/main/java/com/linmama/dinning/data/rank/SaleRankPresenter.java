package com.linmama.dinning.data.rank;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.SaleRankBean;
import com.linmama.dinning.mvp.IModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleRankPresenter extends BasePresenter<SalesRankFragment> implements
        SaleRankContract.SaleDayRankPresenter, SaleRankContract.SaleMonthRankPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new SaleDayRankModel(), new SaleMonthRankModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("SaleDayRank", models[0]);
        map.put("SaleMonthRank", models[1]);
        return map;
    }

    @Override
    public void getSaleDayRank(String day) {
        if (null == getIView())
            return;
        ((SaleDayRankModel) getiModelMap().get("SaleDayRank"))
                .getSaleDayRank(day, new SaleDayRankModel.SaleDayRankHint() {
                    @Override
                    public void successSaleDayRank(SaleRankBean bean) {
                        if (null == getIView())
                            return;
                        getIView().saleDayRankSuccess(bean);
                    }

                    @Override
                    public void failSaleDayRank(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().saleDayRankFail(failMsg);
                    }
                });
    }

    @Override
    public void getSaleMonthRank(int year, int month) {
        if (null == getIView())
            return;
        ((SaleMonthRankModel) getiModelMap().get("SaleMonthRank"))
                .getSaleMonthRank(year, month, new SaleMonthRankModel.SaleMonthRankHint() {
                    @Override
                    public void successSaleMonthRank(SaleRankBean bean) {
                        if (null == getIView())
                            return;
                        getIView().saleMonthRankSuccess(bean);
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
