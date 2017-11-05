package com.linmama.dinning.order.today;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.QuitOrderBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class TodayOrderContract {
    public interface TodayOrderView {
        void getTodayOrderSuccess(TakingOrderMenuBean resultBean);
        void getTodayOrderFail(String failMsg);
    }
    public interface PrintView {
        void getPrintDataSuccess(OrderDetailBean bean);

        void getPrintDataFail(String failMsg);
    }

    public interface TodayOrderPresenter {
        void getTodayOrder(int page);
    }

    public interface PrintPresenter {
        void getPrintData(int orderId);
    }
}
