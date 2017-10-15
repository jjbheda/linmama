package com.linmama.dinning.order.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.QuitOrderBean;
import com.linmama.dinning.except.ApiException;

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
