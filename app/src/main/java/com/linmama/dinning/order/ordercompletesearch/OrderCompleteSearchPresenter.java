package com.linmama.dinning.order.ordercompletesearch;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchContract;
import com.linmama.dinning.order.orderundosearch.OrderUndoSearchModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/10/30.
 */

public class OrderCompleteSearchPresenter extends BasePresenter<OrderCompleteSearchFragment> implements OrderCompleteSearchContract.SearchOrderPresenter{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OrderCompleteSearchModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("OrderCompleteSearchModel", models[0]);
        return map;
    }


    @Override
    public void getFinishedOrderListData(int page,String start, String end) {
        if (null == getIView())
            return;
        ((OrderCompleteSearchModel) getiModelMap().get("OrderCompleteSearchModel")).getFinishedOrderListData(page,start,end, new OrderCompleteSearchModel.SearchCompleteOrderHint(){

            @Override
            public void successSearchOrder(TakingOrderMenuBean beans) {
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
