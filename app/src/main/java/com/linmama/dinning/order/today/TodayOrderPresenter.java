package com.linmama.dinning.order.today;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.order.model.QuitListModel;
import com.linmama.dinning.order.model.RefundModel;
import com.linmama.dinning.order.model.RefuseRefundModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.QuitOrderBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class TodayOrderPresenter extends BasePresenter<QuitFragment> implements
        TodayOrderContract.QuitOrderPresenter, TodayOrderContract.RefuseRefundPresenter,
        TodayOrderContract.RefundPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new QuitListModel(), new RefuseRefundModel(), new RefundModel());
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
    public void getQuitOrder() {
        if (null == getIView())
            return;
        ((QuitListModel) getiModelMap().get("QuitOrderList")).getQuitList(
                new QuitListModel.QuitOrderHint() {
                    @Override
                    public void successQuitOrder(List<QuitOrderBean> list) {
                        if (null == getIView())
                            return;
                        getIView().getQuitOrderSuccess(list);
                    }

                    @Override
                    public void failQUitOrder(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getQuitOrderFail(failMsg);
                    }
                }
        );
    }

    @Override
    public void refund(String refundId, String operation_password) {
        if (null == getIView())
            return;
        ((RefundModel) getiModelMap().get("Refund")).refund(refundId, operation_password,
                new RefundModel.RefundHint() {
                    @Override
                    public void successRefund(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().refundSuccess(bean);
                    }

                    @Override
                    public void failRefund(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().refundFail(failMsg);
                    }
                }
        );
    }

    @Override
    public void refuseRefund(String refundId, String reason) {
        if (null == getIView())
            return;
        ((RefuseRefundModel) getiModelMap().get("RefuseRefund")).refuseRefund(refundId, reason,
                new RefuseRefundModel.RefuseRefundHint() {
                    @Override
                    public void successRefuseRefund(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().refuseRefundSuccess(bean);
                    }

                    @Override
                    public void failRefuseRefund(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().refuseRefundFail(failMsg);
                    }
                }
        );
    }
}
