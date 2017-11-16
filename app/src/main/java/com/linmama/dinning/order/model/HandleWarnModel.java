package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/10
 */
public class HandleWarnModel extends BaseModel {
    public void handleWarn(@NonNull final String warnId, @NonNull final HandleHint hint) {

        if (hint == null)
            throw new RuntimeException("HandleHint不能为空");

//        httpService.handleWarn(warnId)
//                .compose(new CommonTransformer<DataBean>())
//                .subscribe(new CommonSubscriber<DataBean>(LmamaApplication.getInstance()) {
//                    @Override
//                    public void onNext(DataBean dataBean) {
//                        hint.successHandle(dataBean);
//                    }
//
//                    @Override
//                    public void onError(ApiException e) {
//                        super.onError(e);
//                        hint.failHandle(e.getMessage());
//                    }
//                });
    }

    public interface HandleHint {
        void successHandle(DataBean dataBean);

        void failHandle(String str);
    }
}
