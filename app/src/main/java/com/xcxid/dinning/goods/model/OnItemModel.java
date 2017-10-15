package com.xcxid.dinning.goods.model;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

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
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
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
