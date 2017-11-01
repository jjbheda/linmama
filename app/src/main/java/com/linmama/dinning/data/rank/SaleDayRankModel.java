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

public class SaleDayRankModel extends BaseModel {

    public void getSaleDayRank(@NonNull String day, @NonNull final SaleDayRankHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleDayRankHint cannot be null.");
        }

        httpService.getSalesRank(day)
                .compose(new CommonTransformer<SaleRankBean>())
                .subscribe(new CommonSubscriber<SaleRankBean>(LmamaApplication.getInstance()) {
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
