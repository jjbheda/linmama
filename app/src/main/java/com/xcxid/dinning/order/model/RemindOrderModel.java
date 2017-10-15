package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.RemindBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/10
 */
public class RemindOrderModel extends BaseModel {

    public void getRemindOrder(@NonNull final RemindOrderHint remindOrderHint) {

        if (remindOrderHint == null)
            throw new RuntimeException("InfoHint不能为空");

        httpService.getWarnOrder()
                .compose(new CommonTransformer<RemindBean>())
                .subscribe(new CommonSubscriber<RemindBean>(XcxidApplication.getInstance()) {
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
