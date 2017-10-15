package com.xcxid.dinning.goods.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.goods.item.MenuItemBean;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/11
 */

public class OffMenuItemListModel extends BaseModel {
    public void getOffMenu(@NonNull final OffMenuItemListHint hint) {
        if (hint == null)
            throw new RuntimeException("OffMenuItemListHint cannot be null!");

        httpService.getOffMenu()
                .compose(new CommonTransformer<MenuItemBean>())
                .subscribe(new CommonSubscriber<MenuItemBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(MenuItemBean bean) {
                        hint.successOffMenuItemList(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOffMenuItemList(e.getMessage());
                    }
                });
    }

    public interface OffMenuItemListHint {
        void successOffMenuItemList(MenuItemBean bean);

        void failOffMenuItemList(String str);
    }
}
