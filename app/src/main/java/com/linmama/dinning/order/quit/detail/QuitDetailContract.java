package com.linmama.dinning.order.quit.detail;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.OrderDetailBean;

/**
 * Created by jingkang on 2017/3/14
 */

public class QuitDetailContract {
    public interface QuitDetailView {
        void getQuitDetailSuccess(OrderDetailBean bean);

        void getQuitDetailFail(String failMsg);
    }

    public interface RefuseRefundView {
        void refuseRefundSuccess(DataBean bean);

        void refuseRefundFail(String failMsg);
    }

    public interface RefundView {
        void refundSuccess(DataBean bean);

        void refundFail(String failMsg);
    }

    public interface QuitDetailPresenter {
        void getQuitDetail(int orderId);
    }

    public interface RefuseRefundPresenter {
        void refuseRefund(String refundId, String reason);
    }

    public interface RefundPresenter {
        void refund(String refundId, String operation_password);
    }
}
