package com.linmama.dinning.setting.complete;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.CompleteOrderBean;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompleteOrderListContract {
    public interface CompleteListOrderView {
        void getCompleteOrderListSuccess(CompleteOrderBean bean);

        void getCompleteOrderListFail(String failMsg);
    }

    public interface CompleteOrderListPresenter {
        void getCompleteOrderList(int page);
    }

    public interface PrintView {
        void getPrintDataSuccess(OrderDetailBean bean);

        void getPrintDataFail(String failMsg);
    }

    public interface PrintPresenter {
        void getPrintData(int orderId);
    }
}
