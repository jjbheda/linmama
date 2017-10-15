package com.xcxid.dinning.order.remind.detail;

import com.xcxid.dinning.bean.OrderDetailBean;

/**
 * Created by jingkang on 2017/3/14
 */

public class RemindDetailContract {
    public interface RemindDetailView {
        void getRemindDetailSuccess(OrderDetailBean bean);

        void getRemindDetailFail(String failMsg);
    }

    public interface HandleWarnView {
        void handleWarnSuccess(String warnId);

        void handleWarnFail(String failMsg);
    }

    public interface RemindDetailPresenter {
        void getRemindDetail(int orderId);
    }

    public interface HandleWarnPresenter {
        void handleWarn(String warnId);
    }
}
