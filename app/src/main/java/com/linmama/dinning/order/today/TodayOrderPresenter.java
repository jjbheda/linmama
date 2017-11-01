package com.linmama.dinning.order.today;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.order.model.TakingOrderModel;
import com.linmama.dinning.order.model.TodayListModel;
import com.linmama.dinning.order.model.RefundModel;
import com.linmama.dinning.order.model.RefuseRefundModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.order.taking.TakingOrderContract;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class TodayOrderPresenter extends BasePresenter<TodayFragment> implements
        TodayOrderContract.TodayOrderPresenter, TodayOrderContract.PrintPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new TodayListModel(), new RefuseRefundModel(), new RefundModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("QuitOrderList", models[0]);
        map.put("RefuseRefund", models[1]);
        map.put("Refund", models[2]);
        return map;
    }

    @Override
    public void getTodayOrder(int page ) {
        if (null == getIView())
            return;
        TodayListModel.getReceivedOrder(page, new TodayListModel.TodayOrderHint() {
                    @Override
                    public void successTodayOrder(List<TakingOrderBean> list) {
                        if (null == getIView())
                            return;
                        getIView().getTodayOrderSuccess(list);
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
