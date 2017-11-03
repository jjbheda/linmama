package com.linmama.dinning.shop.salerank;


import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.shop.bean.SaleRankBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleRankPresenter extends BasePresenter<SalesRankFragment> implements SaleRankContract.SaleRankPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new SaleRankModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("SaleRank", models[0]);
        return map;
    }

    @Override
    public void getSaleRank(int type) {
        if (null == getIView())
            return;
        ((SaleRankModel) getiModelMap().get("SaleRank"))
                .getProductAnalysisData(type, new SaleRankModel.SaleRankHint() {
                    @Override
                    public void successSaleRank(List<SaleRankBean> beans) {
                        if (null == getIView())
                            return;
                        getIView().saleRankSuccess(beans);
                    }

                    @Override
                    public void failSaleRank(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().saleRankFail(failMsg);
                    }
                });
    }
}
