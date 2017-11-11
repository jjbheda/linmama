package com.linmama.dinning.order.ordercompletesearch.refund;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteSearchContract;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteSearchFragment;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteSearchModel;

import java.util.HashMap;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderCompleteRefundSearchPresenter extends BasePresenter<OrderRefundFragment> implements OrderCompleteSearchContract.RefundFailOrderPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderCompleteSearchModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("OrderCompleteSearchModel", models[0]);
        return map;
    }

    @Override
    public void getRefundFailOrderListData(int page) {
        ((OrderCompleteSearchModel) getiModelMap().get("OrderCompleteSearchModel")).getRefundFailOrderListData(page, new OrderCompleteSearchModel.SearchCompleteOrderHint(){

            @Override
            public void successSearchOrder(TakingOrderMenuBean beans) {
                if (null == getIView())
                    return;
                getIView().getSearchOrderSuccess(beans);
            }

            @Override
            public void failSearchOrder(String failMsg) {
                if (null == getIView())
                    return;
                getIView().getSearchOrderFail(failMsg);

            }
        });
    }

    public void refundRetry(final int id) {
        ((OrderCompleteSearchModel) getiModelMap().get("OrderCompleteSearchModel")).refundRetry(id, new OrderCompleteSearchModel.refundRetryHint(){

            @Override
            public void refundRetrySucess(String msg) {
                if (null == getIView())
                    return;
                getIView().refundRetrySuccess(id,msg);
            }

            @Override
            public void refundRetryFail(String failMsg) {
                if (null == getIView())
                    return;
                getIView().refundRetryFail(failMsg);
            }
        });
    }

}
