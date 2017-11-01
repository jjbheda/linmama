package com.linmama.dinning.setting.complete;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.CompleteOrderBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompleteOrderListModel extends BaseModel {
    public void getCompletedOrderList(int page, @NonNull final CompleteOrderListHint hint) {

        if (hint == null)
            throw new RuntimeException("CompleteOrderListHint cannot be null.");

        httpService.getCompletedOrderList(page)
                .compose(new CommonTransformer<CompleteOrderBean>())
                .subscribe(new CommonSubscriber<CompleteOrderBean>(LmamaApplication.getInstance()) {
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
