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
public class HandleWarnModel extends BaseModel {
    public void handleWarn(@NonNull final String warnId, @NonNull final HandleHint hint) {

        if (hint == null)
            throw new RuntimeException("HandleHint不能为空");

        httpService.handleWarn(warnId)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean dataBean) {
                        hint.successHandle(dataBean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failHandle(e.getMessage());
                    }
                });
    }

    public interface HandleHint {
        void successHandle(DataBean dataBean);

        void failHandle(String str);
    }
}
