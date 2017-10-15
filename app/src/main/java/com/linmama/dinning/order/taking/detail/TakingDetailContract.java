package com.linmama.dinning.order.taking.detail;

import com.linmama.dinning.bean.OrderDetailBean;

/**
 * Created by jingkang on 2017/3/14
 */

public class TakingDetailContract {
    public interface TakingDetailView {
        void getTakingDetailSuccess(OrderDetailBean bean);

        void getTakingDetailFail(String failMsg);
    }

    public interface ConfirmPayView {
        void confirmPaySuccess(String orderId);

        void confirmPayFail(String failMsg);
    }

//    public interface CancelWarnView {
//        void cancelWarnSuccess(DataBean bean, String orderId);
//
//        void cancelWarnFail(String failMsg);
//    }

    public interface CompleteOrderView {
        void completeOrderSuccess(String orderId);
        void completeOrderFail(String failMsg);
    }

    public interface PrintView {

    }

    public interface TakingDetailPresenter {
        void getTakingDetail(int orderId);
    }

    public interface ConfirmPayPresenter {
        void confirmPayment(String orderId, String pwd);
    }

//    public interface CancelWarnPresenter {
//        void cancelWarn(String orderId);
//    }

    public interface CompleteOrderPresenter {
        void completeOrder(String warnId);
    }

    public interface PrintPresenter {

    }
}
