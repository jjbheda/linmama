package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.bean.DataBean;

/**
 * Created by jingkang on 2017/3/12
 */

public class RefuseRefundModel extends BaseModel {

    public void refuseRefund(@NonNull String refund_id, @NonNull String reason, @NonNull final RefuseRefundHint hint) {
        if (null == hint)
            throw new RuntimeException("RefuseRefundHint cannot be null.");

        httpService.refusedRefund(refund_id, reason)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successRefuseRefund(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failRefuseRefund(e.getMessage());
                    }
                });
    }

    public interface RefuseRefundHint {
        void successRefuseRefund(DataBean bean);

        void failRefuseRefund(String failMsg);
    }
}
