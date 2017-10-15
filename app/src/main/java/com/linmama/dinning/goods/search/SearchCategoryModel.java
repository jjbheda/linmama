package com.linmama.dinning.goods.search;

import android.support.annotation.NonNull;

import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.XcxidApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.except.ApiException;

/**
 * Created by jingkang on 2017/3/16
 */

public class SearchCategoryModel extends BaseModel {

    public void searchItem(@NonNull final String keyword, @NonNull final SearchCategoryHint hint) {
        if (hint == null)
            throw new RuntimeException("SearchCategoryHint cannot be null.");

        httpService.searchMenuItem(keyword)
                .compose(new CommonTransformer<SarchItemBean>())
                .subscribe(new CommonSubscriber<SarchItemBean>(XcxidApplication.getInstance()) {
                    @Override
                    public void onNext(SarchItemBean bean) {
                        hint.successSearchCategory(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSearchCategory(e.getMessage());
                    }
                });
    }

    public interface SearchCategoryHint {
        void successSearchCategory(SarchItemBean bean);

        void failSearchCategory(String failMsg);
    }
}
