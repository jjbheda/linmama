package com.linmama.dinning.goods.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.goods.onsale.ShopItemBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.except.ApiException;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class OffMenuItemListModel extends BaseModel {
    public void getOffMenu(@NonNull final OffMenuItemListHint hint) {
        if (hint == null)
            throw new RuntimeException("OffMenuItemListHint cannot be null!");

        httpService.getUnderCarriageData()
                .compose(new CommonTransformer<List<ShopItemBean>>())
                .subscribe(new CommonSubscriber<List<ShopItemBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<ShopItemBean> beans) {
                        hint.successOffMenuItemList(beans);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failOffMenuItemList(e.getMessage());
                    }
                });
    }

    public interface OffMenuItemListHint {
        void successOffMenuItemList(List<ShopItemBean> beans);

        void failOffMenuItemList(String str);
    }
}
