package com.linmama.dinning.order.taking;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.TakingOrderBean;

/**
 * 契约类,定义登录用到的一些接口方法
 */
public class TakingOrderContract {

    public interface TakingOrderView {
        void getTakingOrderSuccess(TakingOrderBean bean);

        void getTakingOrderFail(String failMsg);
    }

    public interface ConfirmPayView {
        void confirmPaySuccess(String orderId);
        void confirmPayFail(String failMsg);
    }

//    public interface CancelWarnView {
//        void cancelWarnSuccess(DataBean bean, String orderId);
//        void cancelWarnFail(String failMsg);
//    }

    public interface CompleteOrderView {
        void completeOrderSuccess(String orderId);
        void completeOrderFail(String failMsg);
    }

    public interface PrintView {
        void getPrintDataSuccess(OrderDetailBean bean);

        void getPrintDataFail(String failMsg);
    }

    public interface TakingOrderPresenter {
        void getTakingOrder(int page);
    }

    public interface ConfirmPayPresenter {
        void confirmPayment(String orderId, String pwd);
    }

//    public interface CancelWarnPresenter {
//        void cancelWarn(String warnId);
//    }

    public interface CompleteOrderPresenter {
        void completeOrder(String warnId);
    }

    public interface PrintPresenter {
        void getPrintData(int orderId);
    }
}
