package com.linmama.dinning.order.neu.timepick;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.DataBean;

/**
 * Created by jingkang on 2017/3/14
 */

public class TimePickerContract {
    public interface TimePickerView {
        String getWarnTime();

        void timePickerSuccess(DataBean bean);

        void timePickerFail(String failMsg);
    }

    public interface ReceiveOrderView {
        void receiveOrderSuccess(String orderId);

        void receiveOrderFail(String failMsg);
    }

    public interface PrintView {
        void getPrintDataSuccess(OrderDetailBean bean);

        void getPrintDataFail(String failMsg);
    }

    public interface TimePickerPresenter {
        void setWarn(String orderId, String warn_time, String warn_type);
    }

    public interface ReceiveOrderPresenter {
        void receiveOrder(String orderId);
    }

    public interface PrintPresenter {
        void getPrintData(int orderId);
    }
}
