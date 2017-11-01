package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

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
                .subscribe(new CommonSubscriber<OrderDetailBean>(LmamaApplication.getInstance()) {
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
