package com.linmama.dinning.order.neu;

import com.linmama.dinning.bean.CancelBean;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.OrderDetailBean;

import java.util.List;

public class NewOrderContract {

    public interface NewOrderView {
        void getNewOrderSuccess(List<LResultNewOrderBean> bean);

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

    public interface NewOrderPresenter {
        void getNewOrder(int page);
    }

    public interface ReceiveOrderPresenter {
        void receiveOrder(String orderId);
    }


    public interface PrintPresenter {
        void getPrintData(int orderId);
    }
}
