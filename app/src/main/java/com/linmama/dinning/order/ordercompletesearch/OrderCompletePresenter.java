package com.linmama.dinning.order.ordercompletesearch;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.mvp.IModel;

import java.util.HashMap;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderCompletePresenter extends BasePresenter<OrderCompleteFragment> implements
        OrderCompleteContract.CompletedOrderPresenter,OrderCompleteContract.RefundFailOrderPresenter {
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
    public void getFinishedOrderListData(int page,String start, String end) {
        if (null == getIView())
            return;
        ((OrderCompleteModel) getiModelMap().get("OrderCompleteSearchModel")).getFinishedOrderListData(page,start,end, new OrderCompleteModel.SearchCompleteOrderHint(){

            @Override
            public void successSearchOrder(TakingOrderMenuBean beans) {
                if (null == getIView())
                    return;
                getIView().getSearchOrderSuccess(beans);
            }

            @Override
            public void failSearchOrder(String failMsg) {
                getIView().getSearchOrderFail(failMsg);

            }
        });
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
                getIView().getSearchOrderFail(failMsg);

            }
        });
    }
    public void cancelOrder(final int id) {
        ((OrderCompleteModel) getiModelMap().get("OrderCompleteSearchModel")).cancelOrder(id, new OrderCompleteModel.CancelOrderHint(){

            @Override
            public void cancelOrderSuccess(String msg) {
                if (null == getIView())
                    return;
                getIView().cancelOrderSuccess(id,msg);
            }

            @Override
            public void failCancelOrder(String failMsg) {
                if (null == getIView())
                    return;
                getIView().cancelOrderFail(failMsg);
            }
        });
    }
}
