package com.linmama.dinning.data.report;

import android.support.annotation.NonNull;

import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.SaleReportBean;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleReportModel extends BaseModel {

    public void trunToReport(@NonNull final SaleReportHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleReportHint cannot be null.");
        }

        httpService.turnoverReport()
                .compose(new CommonTransformer<SaleReportBean>())
                .subscribe(new CommonSubscriber<SaleReportBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(SaleReportBean bean) {
                        hint.successSaleReport(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSaleReport(e.getMessage());
                    }
                });
    }

    public interface SaleReportHint {
        void successSaleReport(SaleReportBean bean);

        void failSaleReport(String failMsg);
    }
}
