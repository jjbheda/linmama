package com.linmama.dinning.goods.model;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/12
 */

public class OffItemModel extends BaseModel {

    public void offItem(int id, final OffItemHint hint) {
        if (null == hint) {
            throw new RuntimeException("OnItemHint cannot be null.");
        }
        httpService.underProduct(id)
                .compose(new CommonTransformer())
                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(String bean) {
                        hint.successOffItem(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOffItem(e.getMessage());
                    }
                });
    }

    public interface OffItemHint {
        void successOffItem(String bean);

        void failOffItem(String failMsg);
    }
}
