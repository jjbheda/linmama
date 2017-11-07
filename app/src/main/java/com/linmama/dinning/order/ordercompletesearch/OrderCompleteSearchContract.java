package com.linmama.dinning.order.ordercompletesearch;

import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;

import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/1.
 */

public class OrderCompleteSearchContract {

    public interface SearchOrderView {

        void getSearchOrderSuccess(TakingOrderMenuBean bean);

        void getSearchOrderFail(String failMsg);
    }

    public interface SearchOrderPresenter {
        void getFinishedOrderListData(int page,String start,String end);
    }
}
