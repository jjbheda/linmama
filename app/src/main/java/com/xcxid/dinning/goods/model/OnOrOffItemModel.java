package com.xcxid.dinning.goods.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/11
 */

public class OnOrOffItemModel extends BaseModel {
    public void OnOrOffItem(@NonNull String op_flag, @NonNull String item_id, @NonNull final OnOrOffItemHint hint) {
        if (hint == null)
            throw new RuntimeException("OnOrOffItemHint cannot be null!");
        httpService.onOrOffItem(op_flag, item_id)
                .compose(new CommonTransformer<DataBean>())
                .subscribe(new CommonSubscriber<DataBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(DataBean bean) {
                        hint.successOnOrOffItem();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOnOrOffItem(e.getMessage());
                    }
                });
    }

    public interface OnOrOffItemHint {
        void successOnOrOffItem();
        void failOnOrOffItem(String str);
    }
}
