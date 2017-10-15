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

public class SaleDayRankModel extends BaseModel {

    public void getSaleDayRank(@NonNull String day, @NonNull final SaleDayRankHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleDayRankHint cannot be null.");
        }

        httpService.getSalesRank(day)
                .compose(new CommonTransformer<SaleRankBean>())
                .subscribe(new CommonSubscriber<SaleRankBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(SaleRankBean bean) {
                        hint.successSaleDayRank(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSaleDayRank(e.getMessage());
                    }
                });
    }

    public interface SaleDayRankHint {
        void successSaleDayRank(SaleRankBean bean);

        void failSaleDayRank(String failMsg);
    }
}
