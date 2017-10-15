package com.linmama.dinning.goods.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.goods.item.MenuItemBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.except.ApiException;

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
