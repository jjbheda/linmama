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
public class SetWarnModel extends BaseModel {
    public void setWarn(@NonNull final String orderId, @NonNull final String warn_time,
        @NonNull final String warn_type, @NonNull final SetWarnHint hint) {

        if (hint == null)
            throw new RuntimeException("SetWarnHint不能为空");

        httpService.setWarn(orderId, warn_time, warn_type)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean dataBean) {
                        hint.successSetWarn(dataBean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSetWarn(e.getMessage());
                    }
                });
    }

    public interface SetWarnHint {
        void successSetWarn(DataBean dataBean);

        void failSetWarn(String str);
    }
}
