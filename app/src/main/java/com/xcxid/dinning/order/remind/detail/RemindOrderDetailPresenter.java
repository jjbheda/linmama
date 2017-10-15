package com.xcxid.dinning.order.remind.detail;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.order.model.OrderDetailModel;
import com.xcxid.dinning.order.model.HandleWarnModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/14
 */

public class RemindOrderDetailPresenter extends BasePresenter<RemindOrderDetailActivity> implements
        RemindDetailContract.RemindDetailPresenter, RemindDetailContract.HandleWarnPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderDetailModel(), new HandleWarnModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("RemindOrderDetail", models[0]);
        map.put("HandleWarn", models[1]);
        return map;
    }

    @Override
    public void getRemindDetail(int orderId) {
        if (null == getIView())
            return;
        ((OrderDetailModel)getiModelMap().get("RemindOrderDetail")).getOrderDetail(orderId,
                new OrderDetailModel.OrderDetailHint() {
                    @Override
                    public void successOrderDetail(OrderDetailBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getRemindDetailSuccess(bean);
                    }

                    @Override
                    public void failOrderDetail(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getRemindDetailFail(failMsg);
                    }
                });
    }

    @Override
    public void handleWarn(final String warnId) {
        if (null == getIView())
            return;
        ((HandleWarnModel) getiModelMap().get("HandleWarn")).handleWarn(warnId, new HandleWarnModel.HandleHint() {
            @Override
            public void successHandle(DataBean dataBean) {
                if (null == getIView())
                    return;
                getIView().handleWarnSuccess(warnId);
            }

            @Override
            public void failHandle(String str) {
                if (null == getIView())
                    return;
                getIView().handleWarnFail(str);
            }

        });
    }
}
