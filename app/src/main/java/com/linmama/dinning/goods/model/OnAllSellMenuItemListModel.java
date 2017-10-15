package com.linmama.dinning.goods.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.goods.item.MenuItemBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/11
 */

public class OnAllSellMenuItemListModel extends BaseModel {

    public void getOnSellMenu(@NonNull final OnAllSellMenuItemListHint hint) {
        if (hint == null)
            throw new RuntimeException("OnAllSellMenuItemListHint cannot be null!");

        httpService.getOnSellMenu()
                .compose(new CommonTransformer<MenuItemBean>())
                .subscribe(new CommonSubscriber<MenuItemBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(MenuItemBean bean) {
                        hint.successOnAllSellMenuItemList(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOnAllSellMenuItemList(e.getMessage());
                    }
                });
    }

    public interface OnAllSellMenuItemListHint {
        void successOnAllSellMenuItemList(MenuItemBean bean);

        void failOnAllSellMenuItemList(String str);
    }
}
