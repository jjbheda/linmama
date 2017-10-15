package com.linmama.dinning.order.order;

import com.linmama.dinning.bean.RedDotStatusBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.RedDotStatusModel;
import com.linmama.dinning.base.BasePresenter;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/16
 */

public class RedDotStatusPresenter extends BasePresenter<OrderFragment> implements RedDotStatusContract.RedDotStatusPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new RedDotStatusModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("RedDotStatus", models[0]);
        return map;
    }

    @Override
    public void getRedDotStatus() {
        if (null == getIView())
            return;
        ((RedDotStatusModel) getiModelMap().get("RedDotStatus")).getRedDotStatusBean(
                new RedDotStatusModel.RedDotStatusHint() {
                    @Override
                    public void successRedDotStatus(RedDotStatusBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getRedDotStatusSuccess(bean);
                    }

                    @Override
                    public void failRedDotStatus(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getRedDotStatusFail(failMsg);
                    }
                }
        );
    }
}
