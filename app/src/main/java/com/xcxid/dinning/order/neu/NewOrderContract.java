package com.xcxid.dinning.order.neu;

import com.xcxid.dinning.bean.CancelBean;
import com.xcxid.dinning.bean.NewOrderBean;
import com.xcxid.dinning.bean.OrderDetailBean;

public class NewOrderContract {

    public interface NewOrderView {
        void getNewOrderSuccess(NewOrderBean bean);

        void getNewOrderFail(String failMsg);
    }

    public interface PrintView {
        void getPrintDataSuccess(OrderDetailBean bean);

        void getPrintDataFail(String failMsg);
    }

    public interface ReceiveOrderView {
        void receiveOrderSuccess(String orderId);

        void receiveOrderFail(String failMsg);
    }

    public interface CancelOrderView {
        void cancelOrderSuccess(String orderId, CancelBean bean);

        void cancelOrderFail(String failMsg);
    }

    public interface OKOrderView {
        void okOrderSuccess(String orderId);

        void okOrderFail(String failMsg);
    }

    public interface NewOrderPresenter {
        void getNewOrder(int page);
    }

    public interface ReceiveOrderPresenter {
        void receiveOrder(String orderId);
    }

    public interface CancelOrderPresenter {
        void cancelOrder(String orderId, String reason);
    }

    public interface OKOrderPresenter {
        void okOrder(String orderId, String password);
    }

    public interface PrintPresenter {
        void getPrintData(int orderId);
    }
}
