package com.linmama.dinning.setting.complete;

import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.CompleteOrderBean;

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
        return loadModelMap(new CompleteOrderListModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("CompleteOrderList", models[0]);
        return map;
    }

    @Override
    public void getPrintData(int orderId) {
    }
}
