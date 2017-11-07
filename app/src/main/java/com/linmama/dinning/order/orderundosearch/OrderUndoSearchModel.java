package com.linmama.dinning.order.orderundosearch;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.goods.search.SearchCategoryModel;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/1.
 */

public class OrderUndoSearchModel extends BaseModel{
    public void searchOrder(int order_type, @NonNull final String keyword, @NonNull final SearchOrderHint hint) {
        if (hint == null)
            throw new RuntimeException("SearchOrderHint cannot be null.");

        httpService.orderQuery(1,order_type,keyword)
                .compose(new CommonTransformer<TakingOrderMenuBean>())
                .subscribe(new CommonSubscriber<TakingOrderMenuBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(TakingOrderMenuBean bean) {
                        hint.successSearchOrder(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSearchOrder(e.getMessage());
                    }
                });
    }

    public interface SearchOrderHint {
        void successSearchOrder(TakingOrderMenuBean bean);

        void failSearchOrder(String failMsg);
    }
}
