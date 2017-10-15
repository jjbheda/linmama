package com.xcxid.dinning.setting.complete.detail;

import com.xcxid.dinning.bean.OrderDetailBean;

/**
 * Created by jingkang on 2017/3/14
 */

public class CompleteOrderDetailContract {
    public interface CompleteDetailView {
        void getCompleteDetailSuccess(OrderDetailBean bean);

        void getCompleteDetailFail(String failMsg);
    }

    public interface CompleteDetailPresenter {
        void getCompleteDetail(int orderId);
    }

}
