package com.linmama.dinning.order.pay;

import com.linmama.dinning.bean.CancelBean;
import com.linmama.dinning.bean.NonPayOrderBean;

/**
 * Created by jingkang on 2017/3/11
 */
public class NonPayOrderContract {

    public interface NonPayOrderView {
        void getNonPayOrderSuccess(NonPayOrderBean nonPayOrderBean);

        void getNonPayOrderFail(String failMsg);
    }

    public interface CancelOrderView {
        void cancelOrderSuccess(String orderId, CancelBean result);

        void cancelOrderFail(String failMsg);
    }

    public interface ConfirmOrderView {
        void confirmOrderSuccess(String orderId);

        void confirmOrderFail(String failMsg);
    }

    public interface NonPayOrderPresenter {
        void getNonPayOrder();
    }

    public interface CancelOrderPresenter {
        void cancelOrder(String orderId, String reason);
    }

    public interface ConfirmOrderPresenter {
        void confirmOrder(String orderId, String password);
    }
}
