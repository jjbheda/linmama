package com.linmama.dinning.order.pay.detail;

import com.linmama.dinning.bean.CancelBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.CancelOrderModel;
import com.linmama.dinning.order.model.OKOrderModel;
import com.linmama.dinning.order.model.OrderDetailModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/14
 */

public class NonPayOrderDetailPresenter extends BasePresenter<NonPayOrderDetailActivity> implements
        NonPayDetailContract.NonPayDetailPresenter, NonPayDetailContract.ConfirmOrderPresenter,
        NonPayDetailContract.CancelOrderPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderDetailModel(), new OKOrderModel(), new CancelOrderModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("NonPayOrderDetail", models[0]);
        map.put("ConfirmOrder", models[1]);
        map.put("CancelOrder", models[2]);
        return map;
    }

    @Override
    public void getNonPayDetail(int orderId) {
        if (null == getIView())
            return;
        ((OrderDetailModel) getiModelMap().get("NonPayOrderDetail")).getOrderDetail(orderId,
                new OrderDetailModel.OrderDetailHint() {
                    @Override
                    public void successOrderDetail(OrderDetailBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getNonPayDetailSuccess(bean);
                    }

                    @Override
                    public void failOrderDetail(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getNonPayDetailFail(failMsg);
                    }
                });
    }

    @Override
    public void cancelOrder(String orderId, String reason) {
        if (null == getIView())
            return;
        ((CancelOrderModel) getiModelMap().get("CancelOrder")).cancelOrder(orderId, reason,
                new CancelOrderModel.CancelHint() {
                    @Override
                    public void successCancel(String orderId, CancelBean bean) {
                        if (null == getIView())
                            return;
                        getIView().cancelOrderSuccess(orderId, bean);
                    }

                    @Override
                    public void failCancel(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().cancelOrderFail(failMsg);
                    }
                });
    }

    @Override
    public void confirmOrder(String orderId, String password) {
        if (null == getIView())
            return;
        ((OKOrderModel) getiModelMap().get("ConfirmOrder")).okOrder(orderId, password,
                new OKOrderModel.OKHint() {
                    @Override
                    public void successOK(String orderId) {
                        if (null == getIView())
                            return;
                        getIView().confirmOrderSuccess(orderId);
                    }

                    @Override
                    public void failOK(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().confirmOrderFail(failMsg);
                    }
                });
    }
}
