package com.linmama.dinning.setting.advice;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/16
 */

public class AdviceModel extends BaseModel {
    public void submitAdvice(@NonNull final String url, @NonNull final String content, @NonNull final AdviceHint hint) {
        if (null == hint)
            throw new RuntimeException("AdviceHint cannot be null.");

        httpService.submitAdvice(url, content)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(LmamaApplication.getInstance()) {
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
