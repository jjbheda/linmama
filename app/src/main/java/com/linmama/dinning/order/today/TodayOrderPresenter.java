package com.linmama.dinning.order.today;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.CompleteOrderModel;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.order.model.TodayListModel;
import com.linmama.dinning.order.model.RefundModel;
import com.linmama.dinning.order.model.RefuseRefundModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/12
 */

public class TodayOrderPresenter extends BasePresenter<TodayFragment> implements
        TodayOrderContract.TodayOrderPresenter,TodayOrderContract.CompleteOrder{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new CompleteOrderModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("CompleteOrder", models[0]);
        return map;
    }

    @Override
    public void getTodayOrder(final int page ) {
        if (null == getIView())
            return;
        TodayListModel.getReceivedOrder(page, new TodayListModel.TodayOrderHint() {
                    @Override
                    public void successTodayOrder(TakingOrderMenuBean resultBean) {
                        if (null == getIView())
                            return;
                        getIView().getTodayOrderSuccess(resultBean);
                    }

                    @Override
                    public void failTodayOrder(String failMsg) {
                        if (null == getIView())
                            return;
                        LogUtils.e("LoginPresenter.failNewOrder", failMsg);
                        getIView().getTodayOrderFail(failMsg);
                    }
                }

        );
    }

    @Override
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
