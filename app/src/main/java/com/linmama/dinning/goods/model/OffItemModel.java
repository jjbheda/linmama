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

    public void offItem(String op_flag, final String item_id, final OffItemHint hint) {
        if (null == hint) {
            throw new RuntimeException("OnItemHint cannot be null.");
        }
        httpService.onOrOffItem(op_flag, item_id)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successOffItem(bean, item_id);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOffItem(e.getMessage());
                    }
                });
    }

    public interface OffItemHint {
        void successOffItem(DataBean bean, String itemId);

        void failOffItem(String failMsg);
    }
}
