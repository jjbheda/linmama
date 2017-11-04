package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.base.BaseHttpResult;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

import java.util.List;

public class TakingOrderModel extends BaseModel {

    public void getTakingOrder(int range, @NonNull final TakingOrderHint hint) {

        if (hint == null)
            throw new RuntimeException("TakingOrderHint cannot be null.");

        httpService.getReceivedOrder(1,1,range+"")
                .compose(new CommonTransformer<TakingOrderMenuBean>())
                .subscribe(new CommonSubscriber<TakingOrderMenuBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(TakingOrderMenuBean bean) {

                        hint.successInfo(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failInfo(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    public interface TakingOrderHint {
        void successInfo(TakingOrderMenuBean bean);

        void failInfo(String failMsg);
    }

}
