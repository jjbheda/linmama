package com.xcxid.dinning.order.neu.detail;

import com.xcxid.dinning.bean.CancelBean;
import com.xcxid.dinning.bean.OrderDetailBean;

/**
 * Created by jingkang on 2017/3/14
 */

public class NewDetailContract {
    public interface NewDetailView {
        void getNewDetailSuccess(OrderDetailBean bean);

        void getNewDetailFail(String failMsg);
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

    public interface NewDetailPresenter {
        void getNewDetail(int orderId);
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
}
