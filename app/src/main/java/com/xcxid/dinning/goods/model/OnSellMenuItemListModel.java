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

public class OnSellMenuItemListModel extends BaseModel {

    public void getOnSellMenu(@NonNull int menuCategory, @NonNull final OnSellMenuItemListHint hint) {
        if (hint == null)
            throw new RuntimeException("OnSellMenuItemListHint cannot be null!");

        httpService.getOnSellMenu(menuCategory)
                .compose(new CommonTransformer<MenuItemBean>())
                .subscribe(new CommonSubscriber<MenuItemBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(MenuItemBean bean) {
                        hint.successOnSellMenuItemList(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOnSellMenuItemList(e.getMessage());
                    }
                });
    }

    public interface OnSellMenuItemListHint {
        void successOnSellMenuItemList(MenuItemBean bean);

        void failOnSellMenuItemList(String str);
    }
}
