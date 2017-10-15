package com.xcxid.dinning.order.quit;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.QuitOrderBean;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.order.model.QuitListModel;
import com.xcxid.dinning.order.model.RefundModel;
import com.xcxid.dinning.order.model.RefuseRefundModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class QuitOrderPresenter extends BasePresenter<QuitFragment> implements
        QuitOrderContract.QuitOrderPresenter, QuitOrderContract.RefuseRefundPresenter,
        QuitOrderContract.RefundPresenter {
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
