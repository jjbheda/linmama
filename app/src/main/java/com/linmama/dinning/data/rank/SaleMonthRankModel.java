package com.linmama.dinning.data.rank;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.SaleRankBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleMonthRankModel extends BaseModel {

    public void getSaleMonthRank(@NonNull int year, @NonNull int month,
                                 @NonNull final SaleMonthRankHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleMonthRankHint cannot be null.");
        }

        httpService.getMonthSalesRank(year, month)
                .compose(new CommonTransformer<SaleRankBean>())
                .subscribe(new CommonSubscriber<SaleRankBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(SaleRankBean bean) {
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
        void successSaleMonthRank(SaleRankBean bean);

        void failSaleMonthRank(String failMsg);
    }
}
