package com.linmama.dinning.order.today.detail;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.order.model.RefundModel;
import com.linmama.dinning.order.model.RefuseRefundModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/14
 */

public class TodayOrderDetailPresenter extends BasePresenter<TodayOrderDetailActivity> implements
        TodayDetailContract.QuitDetailPresenter, TodayDetailContract.RefundPresenter, TodayDetailContract.RefuseRefundPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderDetailModel(), new RefuseRefundModel(), new RefundModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("QuitOrderDetail", models[0]);
        map.put("RefuseRefund", models[1]);
        map.put("Refund", models[2]);
        return map;
    }

    @Override
    public void getQuitDetail(int orderId) {
        if (null == getIView())
            return;
        ((OrderDetailModel)getiModelMap().get("QuitOrderDetail")).getOrderDetail(orderId,
                new OrderDetailModel.OrderDetailHint() {
                    @Override
                    public void successOrderDetail(OrderDetailBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getQuitDetailSuccess(bean);
                    }

                    @Override
                    public void failOrderDetail(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getQuitDetailFail(failMsg);
                    }
                });
    }

    @Override
    public void refund(String refundId, String operation_password) {
        if (null == getIView())
            return;
        ((RefundModel) getiModelMap().get("Refund")).refund(refundId, operation_password,
                new RefundModel.RefundHint() {
                    @Override
                    public void successRefund(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().refundSuccess(bean);
                    }

                    @Override
                    public void failRefund(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().refundFail(failMsg);
                    }
                }
        );
    }

    @Override
    public void refuseRefund(String refundId, String reason) {
        if (null == getIView())
            return;
        ((RefuseRefundModel) getiModelMap().get("RefuseRefund")).refuseRefund(refundId, reason,
                new RefuseRefundModel.RefuseRefundHint() {
                    @Override
                    public void successRefuseRefund(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().refuseRefundSuccess(bean);
                    }

                    @Override
                    public void failRefuseRefund(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().refuseRefundFail(failMsg);
                    }
                }
        );
    }
}
