package com.xcxid.dinning.order.quit;

import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.bean.QuitOrderBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class QuitOrderContract {
    public interface QuitOrderView {
        void getQuitOrderSuccess(List<QuitOrderBean> list);

        void getQuitOrderFail(String failMsg);
    }

    public interface RefuseRefundView {
        void refuseRefundSuccess(DataBean bean);

        void refuseRefundFail(String failMsg);
    }

    public interface RefundView {
        void refundSuccess(DataBean bean);

        void refundFail(String failMsg);
    }

    public interface QuitOrderPresenter {
        void getQuitOrder();
    }

    public interface RefuseRefundPresenter {
        void refuseRefund(String refundId, String reason);
    }

    public interface RefundPresenter {
        void refund(String refundId, String operation_password);
    }
}
