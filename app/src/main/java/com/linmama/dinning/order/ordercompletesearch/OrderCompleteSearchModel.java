package com.linmama.dinning.order.ordercompletesearch;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/1.
 */

public class OrderCompleteSearchModel extends BaseModel{
    public void getFinishedOrderListData(int page, final String start, final String end, final SearchCompleteOrderHint hint) {
        if (hint == null)
            throw new RuntimeException("SearchOrderHint cannot be null.");

        httpService.getFinishedOrderListData(page,start,end)
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

    public void getRefundFailOrderListData(int page,final SearchCompleteOrderHint hint) {
        if (hint == null)
            throw new RuntimeException("SearchOrderHint cannot be null.");

        httpService.getRefundFailOrderData(page)
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

    public void refundRetry(int id,final refundRetryHint hint) {
        if (hint == null)
            throw new RuntimeException("refundRetryHint cannot be null.");
        httpService.refundRetry(id).compose(new CommonTransformer())
                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(String bean) {
                        hint.refundRetrySucess(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.refundRetryFail(e.getMessage());
                    }
                });

    }


    public interface SearchCompleteOrderHint {
        void successSearchOrder(TakingOrderMenuBean bean);
        void failSearchOrder(String failMsg);
    }

    public interface refundRetryHint{
        void refundRetrySucess(String msg);
        void refundRetryFail(String failMsg);
    }
}
