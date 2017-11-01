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
    //op_flag为操作标记，1表示上架，2表示下架；
    public void onItem(String op_flag, final String item_id, final OnItemHint hint) {
        if (null == hint) {
            throw new RuntimeException("OnItemHint cannot be null.");
        }
        httpService.onOrOffItem(op_flag, item_id)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successOnItem(bean, item_id);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOnItem(e.getMessage());
                    }
                });

    }

    public interface OnItemHint {
        void successOnItem(DataBean bean, String itemId);

        void failOnItem(String failMsg);
    }
}
