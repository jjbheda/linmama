package com.linmama.dinning.order.ordersearch;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderSearchPresenter extends BasePresenter<OrderSearchActivity> implements OrderSearchContract.SearchOrderPresenter{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderSearchModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("OrderSearchModel", models[0]);
        return map;
    }


    @Override
    public void getSearchOrderData(int order_type, String search) {
        if (null == getIView())
            return;
        ((OrderSearchModel) getiModelMap().get("OrderSearchModel")).searchOrder(order_type,search, new OrderSearchModel.SearchOrderHint(){

            @Override
            public void successSearchOrder(List<TakingOrderBean> beans) {
                if (null == getIView())
                    return;
                getIView().getSearchOrderSuccess(beans);
            }

            @Override
            public void failSearchOrder(String failMsg) {
                getIView().getSearchOrderFail(failMsg);

            }
        });
    }

}
