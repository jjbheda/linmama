package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.QuitOrderBean;
import com.linmama.dinning.except.ApiException;

import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class TodayListModel extends BaseModel {

    public static void getReceivedOrder(int order_type,@NonNull final TodayOrderHint hint) {
        if (null == hint)
            throw new RuntimeException("QuitOrderHint cannot be null");
        httpService.getReceivedOrder(1,0,"2")
                .compose(new CommonTransformer<TakingOrderMenuBean>())
                .subscribe(new CommonSubscriber<TakingOrderMenuBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(TakingOrderMenuBean bean) {
                        hint.successTodayOrder(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failTodayOrder(e.getMessage());
                    }
                });
    }

    public interface TodayOrderHint {
        void successTodayOrder(TakingOrderMenuBean resultBean);

        void failTodayOrder(String failMsg);
    }
}
