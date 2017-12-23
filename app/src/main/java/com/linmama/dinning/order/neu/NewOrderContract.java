package com.linmama.dinning.order.neu;

import com.linmama.dinning.bean.LResultNewOrderBean;

import java.util.List;

public class NewOrderContract {

    public interface NewOrderView {
        void getNewOrderSuccess(List<LResultNewOrderBean> bean);

        void getNewOrderFail(String failMsg);
    }
    public interface NewOrderPresenter {
        void getNewOrder(int page);
    }

}
