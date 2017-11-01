package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.RemindBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/10
 */
public class RemindOrderModel extends BaseModel {

    public void getRemindOrder(@NonNull final RemindOrderHint remindOrderHint) {

        if (remindOrderHint == null)
            throw new RuntimeException("InfoHint不能为空");

        httpService.getWarnOrder()
                .compose(new CommonTransformer<RemindBean>())
                .subscribe(new CommonSubscriber<RemindBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(RemindBean remindBean) {
                        remindOrderHint.successRemindOrder(remindBean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        remindOrderHint.failRemindOrder(e.getMessage());
                    }
                });
    }

    public interface RemindOrderHint {
        void successRemindOrder(RemindBean remindBean);

        void failRemindOrder(String str);
    }
}
