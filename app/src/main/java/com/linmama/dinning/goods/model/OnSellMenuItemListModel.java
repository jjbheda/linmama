package com.linmama.dinning.goods.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.goods.onsale.ShopItemBean;
import com.linmama.dinning.goods.onsale.ShopTotalBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class OnSellMenuItemListModel extends BaseModel {

    public void getProductlistById(int page,@NonNull int menuCategory, @NonNull final OnSellMenuItemListHint hint) {
        if (hint == null)
            throw new RuntimeException("OnSellMenuItemListHint cannot be null!");

        httpService.getProductlistById(page,menuCategory)
                .compose(new CommonTransformer<ShopTotalBean>())
                .subscribe(new CommonSubscriber<ShopTotalBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(ShopTotalBean bean) {
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
        void successOnSellMenuItemList(ShopTotalBean bean);

        void failOnSellMenuItemList(String str);
    }
}
