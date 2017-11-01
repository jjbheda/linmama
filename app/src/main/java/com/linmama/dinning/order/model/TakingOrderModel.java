package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

import java.util.List;

public class TakingOrderModel extends BaseModel {

    public void getTakingOrder(int page, @NonNull final TakingOrderHint hint) {

        if (hint == null)
            throw new RuntimeException("TakingOrderHint cannot be null.");

        httpService.getReceivedOrder(page,1,"2")
                .compose(new CommonTransformer<List<TakingOrderBean>>())
                .subscribe(new CommonSubscriber<List<TakingOrderBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<TakingOrderBean> bean) {
                        hint.successInfo(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failInfo(e.getMessage());
                    }
                });
    }

    public interface TakingOrderHint {
        void successInfo(List<TakingOrderBean> bean);

        void failInfo(String failMsg);
    }

}
