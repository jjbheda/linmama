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

    public interface RefundRetryView {

        void refundRetrySuccess(int id,String bean);

        void refundRetryFail(String failMsg);
    }

    public interface CancelView {

        void cancelOrderSuccess(int id,String msg);

        void cancelOrderFail(String failMsg);
    }


    public interface CompletedOrderPresenter {
        void getFinishedOrderListData(int page,String start,String end);
    }

    public interface RefundFailOrderPresenter {
        void getRefundFailOrderListData(int page);
    }

}
