package com.linmama.dinning.setting.complete.detail;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.base.BasePresenter;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/14
 */

public class CompleteOrderDetailPresenter extends BasePresenter<CompletedOrderDetailActivity> implements
        CompleteOrderDetailContract.CompleteDetailPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderDetailModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("CompleteOrderDetail", models[0]);
        return map;
    }

    @Override
    public void getCompleteDetail(int orderId) {
        if (null == getIView())
            return;
        ((OrderDetailModel) getiModelMap().get("CompleteOrderDetail")).getOrderDetail(orderId,
                new OrderDetailModel.OrderDetailHint() {
                    @Override
                    public void successOrderDetail(OrderDetailBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getCompleteDetailSuccess(bean);
                    }

                    @Override
                    public void failOrderDetail(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getCompleteDetailFail(failMsg);
                    }
                });
    }
}
