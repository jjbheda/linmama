package com.linmama.dinning.data.report;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.SaleReportBean;
import com.linmama.dinning.mvp.IModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleReportPresenter extends BasePresenter<SalesStatisticsFragment> implements
        SaleReportContract.SaleReportPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new SaleReportModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("SaleReport", models[0]);
        return map;
    }

    @Override
    public void trunToReport() {
        if (null == getIView())
            return;
        ((SaleReportModel) getiModelMap().get("SaleReport"))
                .trunToReport(new SaleReportModel.SaleReportHint() {
                    @Override
                    public void successSaleReport(SaleReportBean bean) {
                        if (null == getIView())
                            return;
                        getIView().saleReportSuccess(bean);
                    }

                    @Override
                    public void failSaleReport(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().saleReportFail(failMsg);
                    }
                });
    }
}
