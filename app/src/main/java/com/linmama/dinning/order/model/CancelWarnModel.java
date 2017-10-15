package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/10
 */
public class CancelWarnModel extends BaseModel {
    public void cancelWarn(@NonNull final String orderId, @NonNull final CancelHint hint) {

        if (hint == null)
            throw new RuntimeException("CancelHint不能为空");

        httpService.cancelWarn(orderId)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean dataBean) {
                        hint.successCancel(dataBean, orderId);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failCancel(e.getMessage());
                    }
                });
    }

    public interface CancelHint {
        void successCancel(DataBean dataBean, String orderId);

        void failCancel(String str);
    }
}
