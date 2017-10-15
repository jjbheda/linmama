package com.xcxid.dinning.order.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.QuitOrderBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class QuitListModel extends BaseModel {

    public void getQuitList(@NonNull final QuitOrderHint hint) {
        if (null == hint)
            throw new RuntimeException("QuitOrderHint cannot be null");

        httpService.getQuitOrder()
                .compose(new CommonTransformer<List<QuitOrderBean>>())
                .subscribe(new CommonSubscriber<List<QuitOrderBean>>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(List<QuitOrderBean> list) {
                        hint.successQuitOrder(list);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failQUitOrder(e.getMessage());
                    }
                });
    }

    public interface QuitOrderHint {
        void successQuitOrder(List<QuitOrderBean> list);

        void failQUitOrder(String failMsg);
    }
}
