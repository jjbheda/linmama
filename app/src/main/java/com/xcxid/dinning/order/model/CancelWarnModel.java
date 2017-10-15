package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

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
