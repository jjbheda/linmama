package com.linmama.dinning.order.orderundosearch;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.CompleteOrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderUndoSearchPresenter extends BasePresenter<OrderUndoSearchActivity> implements OrderUndoSearchContract.SearchOrderPresenter{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderUndoSearchModel(), new CompleteOrderModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("OrderSearchModel", models[0]);
        map.put("CompleteOrder", models[1]);
        return map;
    }


    @Override
    public void getSearchOrderData(int order_type, String search) {
        if (null == getIView())
            return;
        ((OrderUndoSearchModel) getiModelMap().get("OrderSearchModel")).searchOrder(order_type,search, new OrderUndoSearchModel.SearchOrderHint(){

            @Override
            public void successSearchOrder(TakingOrderMenuBean bean) {
                if (null == getIView())
                    return;
                getIView().getSearchOrderSuccess(bean);
            }

            @Override
            public void failSearchOrder(String failMsg) {
                getIView().getSearchOrderFail(failMsg);

            }
        });
    }

    public void completeOrder(int orderId) {
        if (null == getIView())
            return;
        ((CompleteOrderModel) getiModelMap().get("CompleteOrder")).completeOrder(orderId,
                new CompleteOrderModel.CompleteHint() {
                    @Override
                    public void successComplete(int orderId) {
                        if (null == getIView())
                            return;
                        getIView().completeOrderSuccess(orderId);
                    }

                    @Override
                    public void failComplete(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().completeOrderFail(failMsg);
                    }
                });
    }
}
