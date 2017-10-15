package com.xcxid.dinning.order.neu.timepick;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.order.model.OrderDetailModel;
import com.xcxid.dinning.order.model.ReceiveOrderModel;
import com.xcxid.dinning.order.model.SetWarnModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/14
 */

public class TimePickerPresenter extends BasePresenter<TimePickerActivity> implements
        TimePickerContract.TimePickerPresenter, TimePickerContract.ReceiveOrderPresenter,
        TimePickerContract.PrintPresenter{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new SetWarnModel(), new ReceiveOrderModel(), new OrderDetailModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("SetWarn", models[0]);
        map.put("ReceiveOrder", models[1]);
        map.put("PrintData", models[2]);
        return map;
    }

    @Override
    public void setWarn(String orderId, String warn_time, String warn_type) {
        if (null == getIView())
            return;
        if (getIView().checkNull()) {
            return;
        }
        ((SetWarnModel) getiModelMap().get("SetWarn")).setWarn(orderId, warn_time, warn_type, new SetWarnModel.SetWarnHint() {
            @Override
            public void successSetWarn(DataBean bean) {
                if (null == getIView())
                    return;
                getIView().timePickerSuccess(bean);
            }

            @Override
            public void failSetWarn(String str) {
                if (null == getIView())
                    return;
                getIView().timePickerFail(str);
            }
        });
    }

    @Override
    public void receiveOrder(String orderId) {
        if (null == getIView())
            return;
        ((ReceiveOrderModel) getiModelMap().get("ReceiveOrder")).receivingOrder(orderId,
                new ReceiveOrderModel.ReceiveHint() {
                    @Override
                    public void successReceive(String orderId) {
                        if (null == getIView())
                            return;
                        getIView().receiveOrderSuccess(orderId);
                    }

                    @Override
                    public void failReceive(String msg) {
                        if (null == getIView())
                            return;
                        getIView().receiveOrderFail(msg);
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
}
