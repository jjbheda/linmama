package com.linmama.dinning.order.neu;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.NewOrderModel;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

public class NewOrderPresenter extends BasePresenter<NewFragment> implements
        NewOrderContract.NewOrderPresenter{

    @Override
    public void getNewOrder(int page) {
        if (null == getIView())
            return;
        ((NewOrderModel) getiModelMap().get("NewOrderList")).getNewOrder(page, new NewOrderModel.NewOrderHint() {

            @Override
            public void successNewOrder(List<LResultNewOrderBean> bean) {
                if (null == getIView())
                    return;
                getIView().getNewOrderSuccess(bean);
            }

            @Override
            public void failNewOrder(String failMsg) {
                if (null == getIView())
                    return;
                LogUtils.e("LoginPresenter.failNewOrder", failMsg);
                getIView().getNewOrderFail(failMsg);
            }
        });
    }


    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new NewOrderModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("NewOrderList", models[0]);
        return map;
    }

}
