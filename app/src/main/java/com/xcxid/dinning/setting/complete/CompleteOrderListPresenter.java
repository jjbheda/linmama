package com.xcxid.dinning.setting.complete;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.CompleteOrderBean;
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.order.model.OrderDetailModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompleteOrderListPresenter extends BasePresenter<CompleteOrderListFragment> implements
        CompleteOrderListContract.CompleteOrderListPresenter, CompleteOrderListContract.PrintPresenter {
    @Override
    public void getCompleteOrderList(int page) {
        if (null == getIView())
            return;
        ((CompleteOrderListModel) getiModelMap().get("CompleteOrderList")).getCompletedOrderList(page,
                new CompleteOrderListModel.CompleteOrderListHint() {


                    @Override
                    public void successCompleteListOrder(CompleteOrderBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getCompleteOrderListSuccess(bean);
                    }

                    @Override
                    public void failCompleteListOrder(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getCompleteOrderListFail(failMsg);
                    }
                });
    }

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new CompleteOrderListModel(), new OrderDetailModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("CompleteOrderList", models[0]);
        map.put("PrintData", models[1]);
        return map;
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
