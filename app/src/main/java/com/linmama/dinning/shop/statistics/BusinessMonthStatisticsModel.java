package com.linmama.dinning.shop.statistics;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.shop.bean.BusinessParseBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class BusinessMonthStatisticsModel extends BaseModel {

    public void getHistoryAnalysisData(@NonNull int month,
                                 @NonNull final SaleMonthRankHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleMonthRankHint cannot be null.");
        }

        httpService.getHistoryAnalysisData(month)
                .compose(new CommonTransformer<List<BusinessParseBean>>())
                .subscribe(new CommonSubscriber<List<BusinessParseBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<BusinessParseBean> bean) {
                        hint.successSaleMonthRank(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSaleMonthRank(e.getMessage());
                    }
                });
    }

    public interface SaleMonthRankHint {
        void successSaleMonthRank(List<BusinessParseBean> bean);

        void failSaleMonthRank(String failMsg);
    }
}
