package com.linmama.dinning.shop.salerank;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.shop.bean.SaleRankBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

import java.util.List;


/**
 * Created by jingkang on 2017/3/13
 */

public class SaleRankModel extends BaseModel {

    public void getProductAnalysisData(@NonNull int type,
                                 @NonNull final SaleRankHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleMonthRankHint cannot be null.");
        }

        httpService.getProductAnalysisData(type)
                .compose(new CommonTransformer<List<SaleRankBean>>())
                .subscribe(new CommonSubscriber<List<SaleRankBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<SaleRankBean> beans) {
                        hint.successSaleRank(beans);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSaleRank(e.getMessage());
                    }
                });
    }

    public interface SaleRankHint {
        void successSaleRank(List<SaleRankBean> bean);

        void failSaleRank(String failMsg);
    }
}
