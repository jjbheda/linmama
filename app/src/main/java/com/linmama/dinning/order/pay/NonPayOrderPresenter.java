package com.linmama.dinning.order.pay;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.CancelBean;
import com.linmama.dinning.bean.NonPayOrderBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.CancelOrderModel;
import com.linmama.dinning.order.model.NonPayOrderModel;
import com.linmama.dinning.order.model.OKOrderModel;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/11
 */

public class NonPayOrderPresenter extends BasePresenter<NonPayFragment> implements
        NonPayOrderContract.NonPayOrderPresenter, NonPayOrderContract.CancelOrderPresenter,
        NonPayOrderContract.ConfirmOrderPresenter {
    @Override
    public void getNonPayOrder() {
        if (null == getIView())
            return;
        ((NonPayOrderModel) getiModelMap().get("NonPayOrder")).getNonPayOrder(new NonPayOrderModel.NonPayOrderHint() {

            @Override
            public void successNonPayOrder(NonPayOrderBean nonPayOrderBean) {
                if (null == getIView())
                    return;
                getIView().getNonPayOrderSuccess(nonPayOrderBean);
            }

            @Override
            public void failNonPayOrder(String str) {
                if (null == getIView())
                    return;
                getIView().getNonPayOrderFail(str);
            }
        });
    }

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new NonPayOrderModel(), new OKOrderModel(), new CancelOrderModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("NonPayOrder", models[0]);
        map.put("ConfirmOrder", models[1]);
        map.put("CancelOrder", models[2]);
        return map;
    }

    @Override
    public void cancelOrder(String orderId, String reason) {
        if (null == getIView())
            return;
        ((CancelOrderModel) getiModelMap().get("CancelOrder")).cancelOrder(orderId, reason, new CancelOrderModel
                .CancelHint() {
            @Override
            public void successCancel(String orderId, CancelBean result) {
                if (null == getIView())
                    return;
                LogUtils.d("successCancel", "success");
                getIView().cancelOrderSuccess(orderId, result);
            }

            @Override
            public void failCancel(String str) {
                if (null == getIView())
                    return;
                LogUtils.d("failCancel", "fail");
                getIView().cancelOrderFail(str);
            }

        });
    }

    @Override
    public void confirmOrder(String orderId, String password) {
        if (null == getIView())
            return;
        ((OKOrderModel) getiModelMap().get("ConfirmOrder")).okOrder(orderId, password, new OKOrderModel
                .OKHint() {
            @Override
            public void successOK(String orderId) {
                if (null == getIView())
                    return;
                getIView().confirmOrderSuccess(orderId);
            }

            @Override
            public void failOK(String str) {
                if (null == getIView())
                    return;
                getIView().confirmOrderFail(str);
            }
        });
    }
}
