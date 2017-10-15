package com.linmama.dinning.order.remind;

import com.linmama.dinning.bean.RemindBean;

/**
 * Created by jingkang on 2017/3/11
 */
public class RemindOrderContract {

    public interface RemindOrderView {
        void getRemindOrderSuccess(RemindBean remindBean);

        void getRemindOrderFail(String failMsg);
    }

    public interface HandleWarnView {
        void handleWarnSuccess(String warnId);

        void handleWarnFail(String failMsg);
    }

    public interface RemindOrderPresenter {
        void getRemindOrder();
    }

    public interface HandleWarnPresenter {
        void handleWarn(String warnId);
    }
}
