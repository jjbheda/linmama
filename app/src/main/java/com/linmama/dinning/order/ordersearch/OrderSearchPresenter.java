package com.linmama.dinning.order.ordersearch;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.CompleteOrderModel;
import com.linmama.dinning.order.model.OKOrderModel;
import com.linmama.dinning.order.model.OrderDetailModel;
import com.linmama.dinning.order.model.OrderSearchModel;
import com.linmama.dinning.order.model.TakingOrderModel;

import java.util.HashMap;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderSearchPresenter extends BasePresenter<OrderSearchActivity> {
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
}
