package com.xcxid.dinning.setting.advice;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/16
 */

public class AdviceModel extends BaseModel {
    public void submitAdvice(@NonNull final String url, @NonNull final String content, @NonNull final AdviceHint hint) {
        if (null == hint)
            throw new RuntimeException("AdviceHint cannot be null.");

        httpService.submitAdvice(url, content)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successAdvice(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failAdvice(e.getMessage());
                    }
                });
    }

    public interface AdviceHint {
        void successAdvice(DataBean bean);

        void failAdvice(String failMsg);
    }
}
