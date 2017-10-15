package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/14
 */

public class OrderDetailModel extends BaseModel {

    public void getOrderDetail(@NonNull int orderId, @NonNull final OrderDetailHint hint) {
        if (null == hint) {
            throw new RuntimeException("OrderDetailHint cannot be null.");
        }
        httpService.getOrderDetail(orderId)
                .compose(new CommonTransformer<OrderDetailBean>())
                .subscribe(new CommonSubscriber<OrderDetailBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(OrderDetailBean bean) {
                        hint.successOrderDetail(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOrderDetail(e.getMessage());
                    }
                });
    }

    public interface OrderDetailHint {
        void successOrderDetail(OrderDetailBean bean);

        void failOrderDetail(String failMsg);
    }
}
