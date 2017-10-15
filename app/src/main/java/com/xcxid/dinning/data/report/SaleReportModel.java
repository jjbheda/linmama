package com.xcxid.dinning.data.report;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.SaleReportBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

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
