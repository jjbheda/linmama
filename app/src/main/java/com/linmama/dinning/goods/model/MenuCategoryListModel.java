package com.linmama.dinning.goods.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

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
