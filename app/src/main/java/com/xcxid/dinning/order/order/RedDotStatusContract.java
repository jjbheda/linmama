package com.xcxid.dinning.order.order;

import com.xcxid.dinning.bean.RedDotStatusBean;

/**
 * Created by jingkang on 2017/3/16
 */

public class RedDotStatusContract {
    public interface RedDotStatusView {
        void getRedDotStatusSuccess(RedDotStatusBean bean);

        void getRedDotStatusFail(String failMsg);
    }

    public interface RedDotStatusPresenter {
        void getRedDotStatus();
    }
}
