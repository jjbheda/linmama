package com.linmama.dinning.order.neu;

import com.linmama.dinning.bean.CancelBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.NewOrderBean;
import com.linmama.dinning.order.model.CancelOrderModel;
import com.linmama.dinning.order.model.NewOrderModel;
import com.linmama.dinning.order.model.OKOrderModel;
import com.linmama.dinning.order.model.ReceiveOrderModel;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;

public class NewOrderPresenter extends BasePresenter<NewFragment> implements
        NewOrderContract.NewOrderPresenter, NewOrderContract.ReceiveOrderPresenter,
        NewOrderContract.CancelOrderPresenter, NewOrderContract.OKOrderPresenter, NewOrderContract.PrintPresenter {

    @Override
    public void getNewOrder(int page) {
        if (null == getIView())
            return;
        ((NewOrderModel) getiModelMap().get("NewOrderList")).getNewOrder(page, new NewOrderModel.NewOrderHint() {

            @Override
            public void successNewOrder(NewOrderBean bean) {
                if (null == getIView())
                    return;
                getIView().getNewOrderSuccess(bean);
            }

            @Override
            public void failNewOrder(String failMsg) {
                if (null == getIView())
                    return;
                LogUtils.e("LoginPresenter.failNewOrder", failMsg);
                getIView().getNewOrderFail(failMsg);
            }
        });
    }

    @Override
    public void receiveOrder(String orderId) {
        if (null == getIView())
            return;
        ((ReceiveOrderModel) getiModelMap().get("ReceiveOrder")).receivingOrder(orderId, new ReceiveOrderModel
                .ReceiveHint() {
            @Override
            public void successReceive(String orderId) {
                if (null == getIView())
                    return;
                LogUtils.d("successReceive", "success");
                getIView().receiveOrderSuccess(orderId);
            }

            @Override
            public void failReceive(String failMsg) {
                if (null == getIView())
                    return;
                LogUtils.d("failReceive", "fail");
                getIView().receiveOrderFail(failMsg);
            }

        });
    }

    @Override
    public void cancelOrder(String orderId, String reason) {
        if (null == getIView())
            return;
        ((CancelOrderModel) getiModelMap().get("CancelOrder")).cancelOrder(orderId, reason,  new CancelOrderModel
                .CancelHint() {
            @Override
            public void successCancel(String orderId, CancelBean bean) {
                if (null == getIView())
                    return;
                LogUtils.d("successCancel", "success");
                getIView().cancelOrderSuccess(orderId, bean);
            }

            @Override
            public void failCancel(String failMsg) {
                if (null == getIView())
                    return;
                LogUtils.d("failCancel", "fail");
                getIView().cancelOrderFail(failMsg);
            }

        });
    }

    @Override
    public void okOrder(String orderId, String password) {
        if (null == getIView())
            return;
        ((OKOrderModel) getiModelMap().get("OkOrder")).okOrder(orderId, password, new OKOrderModel
                .OKHint() {
            @Override
            public void successOK(String orderId) {
                if (null == getIView())
                    return;
                getIView().okOrderSuccess(orderId);
            }

            @Override
            public void failOK(String failMsg) {
                if (null == getIView())
                    return;
                getIView().okOrderFail(failMsg);
            }
        });
    }

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new NewOrderModel(), new ReceiveOrderModel(),
                new CancelOrderModel(), new OKOrderModel(), new OrderDetailModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("NewOrderList", models[0]);
        map.put("ReceiveOrder", models[1]);
        map.put("CancelOrder", models[2]);
        map.put("OkOrder", models[3]);
        map.put("PrintData", models[4]);
        return map;
    }

    @Override
    public void getPrintData(int orderId) {
        if (null == getIView())
            return;
        ((OrderDetailModel) getiModelMap().get("PrintData")).getOrderDetail(orderId,
                new OrderDetailModel.OrderDetailHint() {
                    @Override
                    public void successOrderDetail(OrderDetailBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getPrintDataSuccess(bean);
                    }

                    @Override
                    public void failOrderDetail(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getPrintDataFail(failMsg);
                    }
                });
    }
}
