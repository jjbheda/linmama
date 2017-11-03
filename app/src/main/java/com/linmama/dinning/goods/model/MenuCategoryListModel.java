package com.linmama.dinning.goods.model;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.except.ApiException;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryListModel extends BaseModel {

    public void getMenuCategoryList(@NonNull final MenuCategoryListHint hint) {
        if (hint == null)
            throw new RuntimeException("MenuCategoryListHint cannot be null!");

        httpService.getMenuCategory()
                .compose(new CommonTransformer<List<MenuCategoryBean>>())
                .subscribe(new CommonSubscriber<List<MenuCategoryBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<MenuCategoryBean> beans) {
                        hint.successMenuCategoryList(beans);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failMenuCategoryList(e.getMessage());
                    }
                });
    }

    public interface MenuCategoryListHint {
        void successMenuCategoryList(List<MenuCategoryBean> beans);

        void failMenuCategoryList(String str);
    }
}
