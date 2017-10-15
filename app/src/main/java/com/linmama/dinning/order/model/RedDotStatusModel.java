package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.RedDotStatusBean;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/16
 */

public class RedDotStatusModel extends BaseModel {

    public void getRedDotStatusBean(@NonNull final RedDotStatusHint hint) {
        if (null == hint)
            throw new RuntimeException("RedDotStatusHint cannot be null.");
        httpService.getRedDotStatus()
                .compose(new CommonTransformer<RedDotStatusBean>())
                .subscribe(new CommonSubscriber<RedDotStatusBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(RedDotStatusBean bean) {
                        hint.successRedDotStatus(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failRedDotStatus(e.getMessage());
                    }
                });
    }

    public interface RedDotStatusHint {
        void successRedDotStatus(RedDotStatusBean bean);

        void failRedDotStatus(String failMsg);
    }
}
