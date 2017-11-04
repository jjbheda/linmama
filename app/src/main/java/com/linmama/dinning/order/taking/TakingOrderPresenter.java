package com.linmama.dinning.order.taking;

import com.linmama.dinning.base.BaseHttpResult;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.order.model.CompleteOrderModel;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.order.model.TakingOrderModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.OKOrderModel;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

public class TakingOrderPresenter extends BasePresenter<TakingFragment> implements
        TakingOrderContract.TakingOrderPresenter, TakingOrderContract.ConfirmPayPresenter,
        TakingOrderContract.PrintPresenter, TakingOrderContract.CompleteOrderPresenter {

    @Override
    public void getTakingOrder(int range) {
        if (null == getIView())
            return;
        ((TakingOrderModel) getiModelMap().get("TakingOrder")).getTakingOrder(range, new TakingOrderModel.TakingOrderHint() {

            @Override
            public void successInfo(TakingOrderMenuBean bean) {
                if (null == getIView())
                    return;
                getIView().getTakingOrderSuccess(bean);
            }

            @Override
            public void failInfo(String failMsg) {
                if (null == getIView())
                    return;
                LogUtils.e("LoginPresenter.failNewOrder", failMsg);
                getIView().getTakingOrderFail(failMsg);
            }
        });
    }

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new TakingOrderModel(), new OKOrderModel(),
                new OrderDetailModel(), new CompleteOrderModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("TakingOrder", models[0]);
        map.put("ConfirmPay", models[1]);
        map.put("PrintData", models[2]);
        map.put("CompleteOrder", models[3]);
        return map;
    }

//    @Override
//    public void cancelWarn(String warnId) {
//        if (null == getIView())
//            return;
//        ((CancelWarnModel) getiModelMap().get("CancelWarn")).cancelWarn(warnId,
//                new CancelWarnModel.CancelHint() {
//                    @Override
//                    public void successCancel(DataBean dataBean, String orderId) {
//                        getIView().cancelWarnSuccess(dataBean, orderId);
//                    }
//
//                    @Override
//                    public void failCancel(String failMsg) {
//                        getIView().cancelWarnFail(failMsg);
//                    }
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
                    public void failComplete(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().completeOrderFail(failMsg);
                    }
                });
    }
}
