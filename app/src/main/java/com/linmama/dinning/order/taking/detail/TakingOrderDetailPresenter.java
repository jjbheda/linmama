package com.linmama.dinning.order.taking.detail;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.order.model.CompleteOrderModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.OKOrderModel;
import com.linmama.dinning.order.model.OrderDetailModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/14
 */

public class TakingOrderDetailPresenter extends BasePresenter<TakingOrderDetailActivity> implements
        TakingDetailContract.TakingDetailPresenter, TakingDetailContract.ConfirmPayPresenter, TakingDetailContract.CompleteOrderPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderDetailModel(), new OKOrderModel(), new CompleteOrderModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("TakingOrderDetail", models[0]);
        map.put("ConfirmPay", models[1]);
        map.put("CompleteOrder", models[2]);
        return map;
    }

    @Override
    public void getTakingDetail(int orderId) {
        if (null == getIView())
            return;
        ((OrderDetailModel) getiModelMap().get("TakingOrderDetail")).getOrderDetail(orderId,
                new OrderDetailModel.OrderDetailHint() {
                    @Override
                    public void successOrderDetail(OrderDetailBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getTakingDetailSuccess(bean);
                    }

                    @Override
                    public void failOrderDetail(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getTakingDetailFail(failMsg);
                    }
                });
    }

//    @Override
//    public void cancelWarn(String orderId) {
//        if (null == getIView())
//            return;
//        ((CancelWarnModel) getiModelMap().get("CancelWarn")).cancelWarn(orderId,
//                new CancelWarnModel.CancelHint() {
//                    @Override
//                    public void successCancel(DataBean bean, String orderId) {
//                        getIView().cancelWarnSuccess(bean, orderId);
//                    }
//
//                    @Override
//                    public void failCancel(String failMsg) {
//                        getIView().cancelWarnFail(failMsg);
//                    }
//
//                });
//    }

    @Override
    public void confirmPayment(String orderId, String pwd) {
        if (null == getIView())
            return;
        ((OKOrderModel) getiModelMap().get("ConfirmPay")).okOrder(orderId, pwd,
                new OKOrderModel.OKHint() {
                    @Override
                    public void successOK(String orderId) {
                        if (null == getIView())
                            return;
                        getIView().confirmPaySuccess(orderId);
                    }

                    @Override
                    public void failOK(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().confirmPayFail(failMsg);
                    }

                });
    }

    @Override
    public void completeOrder(String orderId) {
        if (null == getIView())
            return;
        ((CompleteOrderModel) getiModelMap().get("CompleteOrder")).completeWarn(orderId,
                new CompleteOrderModel.CompleteHint() {
                    @Override
                    public void successComplete(String orderId) {
                        if (null == getIView())
                            return;
                        getIView().completeOrderSuccess(orderId);
                    }

                    @Override
                    public void failComplete(String str) {
                        if (null == getIView())
                            return;
                        getIView().completeOrderFail(str);
                    }

                });
    }
}
