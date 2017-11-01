package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

import java.util.List;

public class NewOrderModel extends BaseModel {

    public void getNewOrder(int page, @NonNull final NewOrderHint hint) {

        if (hint == null)
            throw new RuntimeException("NewOrderHint cannot be null.");
        httpService.getNewOrder()
                .compose(new CommonTransformer<List<LResultNewOrderBean>>())
                .subscribe(new CommonSubscriber<List<LResultNewOrderBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<LResultNewOrderBean> bean) {
                        hint.successNewOrder(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failNewOrder(e.getMessage());

                    }
                });
    }

    public interface NewOrderHint {
        void successNewOrder(List<LResultNewOrderBean> bean);

        void failNewOrder(String failMsg);
    }

}
