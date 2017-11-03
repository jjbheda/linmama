package com.linmama.dinning.goods.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/11
 */

public class OnOrOffItemModel extends BaseModel {
    public void OnOrOffItem(@NonNull String op_flag, @NonNull String item_id, @NonNull final OnOrOffItemHint hint) {
        if (hint == null)
            throw new RuntimeException("OnOrOffItemHint cannot be null!");
//        httpService.onOrOffItem(op_flag, item_id)
//                .compose(new CommonTransformer<DataBean>())
//                .subscribe(new CommonSubscriber<DataBean>(LmamaApplication.getInstance()) {
//                    @Override
//                    public void onNext(DataBean bean) {
//                        hint.successOnOrOffItem();
//                    }
//
//                    @Override
//                    public void onError(ApiException e) {
//                        super.onError(e);
//                        hint.failOnOrOffItem(e.getMessage());
//                    }
//                });
    }

    public interface OnOrOffItemHint {
        void successOnOrOffItem();
        void failOnOrOffItem(String str);
    }
}
