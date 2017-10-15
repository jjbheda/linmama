package com.xcxid.dinning.goods.search;

import android.support.annotation.NonNull;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.base.BaseModel;
import com.xcxid.dinning.bean.SarchItemBean;
import com.xcxid.dinning.except.ApiException;
import com.xcxid.dinning.subscriber.CommonSubscriber;
import com.xcxid.dinning.transformer.CommonTransformer;

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
