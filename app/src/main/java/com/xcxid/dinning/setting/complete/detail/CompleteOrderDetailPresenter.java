package com.xcxid.dinning.setting.complete.detail;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.order.model.OrderDetailModel;

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
