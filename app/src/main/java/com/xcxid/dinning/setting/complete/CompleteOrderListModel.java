package com.xcxid.dinning.setting.complete;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.CompleteOrderBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompleteOrderListModel extends BaseModel {
    public void getCompletedOrderList(int page, @NonNull final CompleteOrderListHint hint) {

        if (hint == null)
            throw new RuntimeException("CompleteOrderListHint cannot be null.");

        httpService.getCompletedOrderList(page)
                .compose(new CommonTransformer<CompleteOrderBean>())
                .subscribe(new CommonSubscriber<CompleteOrderBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(CompleteOrderBean bean) {
                        hint.successCompleteListOrder(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failCompleteListOrder(e.getMessage());
                    }
                });
    }

    public interface CompleteOrderListHint {
        void successCompleteListOrder(CompleteOrderBean bean);

        void failCompleteListOrder(String failMsg);
    }
}
