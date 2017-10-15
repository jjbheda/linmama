package com.xcxid.dinning.data.rank;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.SaleRankBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

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
                .subscribe(new CommonSubscriber<SaleRankBean>(XcxidApplication.getInstance()) {
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
