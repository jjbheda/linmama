package com.linmama.dinning.order.orderundosearch;

import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;

import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/1.
 */

public class OrderUndoSearchContract {

    public interface SearchOrderView {

        void getSearchOrderSuccess(TakingOrderMenuBean bean);

        void getSearchOrderFail(String failMsg);
    }

    public interface SearchOrderPresenter {
        void getSearchOrderData(int order_type,String search);
    }
}

