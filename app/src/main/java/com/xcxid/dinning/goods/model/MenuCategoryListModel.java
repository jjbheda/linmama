package com.xcxid.dinning.goods.model;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.goods.category.MenuCategoryBean;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryListModel extends BaseModel {

    public void getMenuCategoryList(@NonNull final MenuCategoryListHint hint) {
        if (hint == null)
            throw new RuntimeException("MenuCategoryListHint cannot be null!");

        httpService.getMenuCategory()
                .compose(new CommonTransformer<MenuCategoryBean>())
                .subscribe(new CommonSubscriber<MenuCategoryBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(MenuCategoryBean bean) {
                        hint.successMenuCategoryList(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failMenuCategoryList(e.getMessage());
                    }
                });
    }

    public interface MenuCategoryListHint {
        void successMenuCategoryList(MenuCategoryBean bean);

        void failMenuCategoryList(String str);
    }
}
