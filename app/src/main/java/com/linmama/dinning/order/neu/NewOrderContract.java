package com.linmama.dinning.order.neu;

import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.NewOrderMenuBean;

import java.util.List;

public class NewOrderContract {

    public interface NewOrderView {
        void getNewOrderSuccess(NewOrderMenuBean bean);

        void getNewOrderFail(String failMsg);
    }
    public interface NewOrderPresenter {
        void getNewOrder(int page);
    }

}
