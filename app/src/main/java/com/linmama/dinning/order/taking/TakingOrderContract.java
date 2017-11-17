package com.linmama.dinning.order.taking;

import com.linmama.dinning.base.BaseHttpResult;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;

import java.util.List;

/**
 * 契约类,定义登录用到的一些接口方法
 */
public class TakingOrderContract {

    public interface TakingOrderView {
        void getTakingOrderSuccess(TakingOrderMenuBean bean);

        void getTakingOrderFail(String failMsg);
    }

    public interface CompleteOrderView {
        void completeOrderSuccess(int orderId);
        void completeOrderFail(String failMsg);
    }

    public interface PrintView {
        void getPrintDataSuccess(OrderDetailBean bean);

        void getPrintDataFail(String failMsg);
    }

    public interface TakingOrderPresenter {
        void getTakingOrder(int page,int order_type,int range);
    }

    public interface ConfirmPayPresenter {
        void confirmPayment(int orderId, String pwd);
    }

//    public interface CancelWarnPresenter {
//        void cancelWarn(String warnId);
//    }

    public interface CompleteOrderPresenter {
        void completeOrder(int id);
    }

    public interface PrintPresenter {
        void getPrintData(int orderId);
    }
}
