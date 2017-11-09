package com.linmama.dinning.goods.search;

import android.support.annotation.NonNull;

import com.linmama.dinning.bean.ShopSearchBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.except.ApiException;

import java.util.List;

/**
 * Created by jingkang on 2017/3/16
 */

public class SearchCategoryModel extends BaseModel {

    public void searchItem(@NonNull final String keyword, @NonNull final SearchCategoryHint hint) {
        if (hint == null)
            throw new RuntimeException("SearchCategoryHint cannot be null.");

        httpService.getShopSearchData(keyword)
                .compose(new CommonTransformer<List<ShopSearchBean>>())
                .subscribe(new CommonSubscriber<List<ShopSearchBean>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<ShopSearchBean> beans) {
                        hint.successSearchCategory(beans);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSearchCategory(e.getMessage());
                    }
                });
    }

    public interface SearchCategoryHint {
        void successSearchCategory(List<ShopSearchBean> beans);

        void failSearchCategory(String failMsg);
    }
}
