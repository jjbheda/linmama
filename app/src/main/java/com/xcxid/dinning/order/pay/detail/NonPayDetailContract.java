package com.xcxid.dinning.order.pay.detail;

import com.xcxid.dinning.bean.CancelBean;
import com.xcxid.dinning.bean.OrderDetailBean;

/**
 * Created by jingkang on 2017/3/14
 */

public class NonPayDetailContract {
    public interface NonPayDetailView {
        void getNonPayDetailSuccess(OrderDetailBean bean);

        void getNonPayDetailFail(String failMsg);
    }

    public interface CancelOrderView {
        void cancelOrderSuccess(String orderId, CancelBean result);

        void cancelOrderFail(String failMsg);
    }

    public interface ConfirmOrderView {
        void confirmOrderSuccess(String orderId);

        void confirmOrderFail(String failMsg);
    }

    public interface NonPayDetailPresenter {
        void getNonPayDetail(int orderId);
    }

    public interface CancelOrderPresenter {
        void cancelOrder(String orderId, String reason);
    }

    public interface ConfirmOrderPresenter {
        void confirmOrder(String orderId, String password);
    }
}
