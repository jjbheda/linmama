package com.linmama.dinning.order.ordersearch;

import com.linmama.dinning.bean.SarchItemBean;

/**
 * Created by jiangjingbo on 2017/11/1.
 */

public class OrderSearchContract  {

    public interface SearchOrderView {
        String getSearchKeyword();

        void getSearchOrderSuccess(SarchItemBean bean);

        void getSearchOrderFail(String failMsg);
    }


}
