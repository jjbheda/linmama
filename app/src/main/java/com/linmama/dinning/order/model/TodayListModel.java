package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.TakingOrderBean;
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

    public static void getReceivedOrder(int page,@NonNull final TodayOrderHint hint) {
        if (null == hint)
            throw new RuntimeException("QuitOrderHint cannot be null");

        httpService.getReceivedOrder(page,0,"0")
                .compose(new CommonTransformer<List<TakingOrderBean>>())
                .subscribe(new CommonSubscriber<List<TakingOrderBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<TakingOrderBean> list) {
                        hint.successTodayOrder(list);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failTodayOrder(e.getMessage());
                    }
                });
    }

    public interface TodayOrderHint {
        void successTodayOrder(List<TakingOrderBean> list);

        void failTodayOrder(String failMsg);
    }
}
