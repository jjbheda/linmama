package com.linmama.dinning.order.ordersearch;

import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.bean.TakingOrderBean;

import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/1.
 */

public class OrderSearchContract  {

    public interface SearchOrderView {

        void getSearchOrderSuccess(List<TakingOrderBean> beans);

        void getSearchOrderFail(String failMsg);
    }

    public interface SearchOrderPresenter {
        void getSearchOrderData(int order_type,String search);
    }
}
