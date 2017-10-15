package com.xcxid.dinning.order.remind;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.RemindBean;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.order.model.HandleWarnModel;
import com.xcxid.dinning.order.model.RemindOrderModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/11
 */

public class RemindOrderPresenter extends BasePresenter<RemindFragment> implements
        RemindOrderContract.RemindOrderPresenter, RemindOrderContract.HandleWarnPresenter {

    @Override
    public void getRemindOrder() {
        if (null == getIView())
            return;
        ((RemindOrderModel) getiModelMap().get("RemindOrder")).getRemindOrder(new RemindOrderModel.RemindOrderHint() {
            @Override
            public void successRemindOrder(RemindBean remindBean) {
                if (null == getIView())
                    return;
                getIView().getRemindOrderSuccess(remindBean);
            }

            @Override
            public void failRemindOrder(String str) {
                if (null == getIView())
                    return;
                getIView().getRemindOrderFail(str);
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

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new RemindOrderModel(), new HandleWarnModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("RemindOrder", models[0]);
        map.put("HandleWarn", models[1]);
        return map;
    }

}
