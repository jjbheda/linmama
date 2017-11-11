package com.linmama.dinning.order.ordercompletesearch.refundsearch;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteContract;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteModel;
import com.linmama.dinning.order.ordercompletesearch.refund.OrderRefundFragment;

import java.util.HashMap;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderRefundSearchPresenter extends BasePresenter<OrderRefundFragment> implements OrderCompleteContract.RefundFailOrderPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderCompleteModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("OrderCompleteSearchModel", models[0]);
        return map;
    }

    @Override
    public void getRefundFailOrderListData(int page) {
        ((OrderCompleteModel) getiModelMap().get("OrderCompleteSearchModel")).getRefundFailOrderListData(page, new OrderCompleteModel.SearchCompleteOrderHint(){

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
        ((OrderCompleteModel) getiModelMap().get("OrderCompleteSearchModel")).refundRetry(id, new OrderCompleteModel.refundRetryHint(){

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
