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

public class OnItemModel extends BaseModel {

    public void onItem(final String item_id, final OnItemHint hint) {
        if (null == hint) {
            throw new RuntimeException("OnItemHint cannot be null.");
        }
        httpService.upProduct(item_id)
                .compose(new CommonTransformer())
                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(String bean) {
                        hint.successOnItem(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOnItem(e.getMessage());
                    }
                });

    }

    public interface OnItemHint {
        void successOnItem(String msg);

        void failOnItem(String failMsg);
    }
}
