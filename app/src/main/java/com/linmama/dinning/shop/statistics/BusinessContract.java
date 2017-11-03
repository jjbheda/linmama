package com.linmama.dinning.shop.statistics;

import com.linmama.dinning.shop.bean.BusinessParseBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class BusinessContract {

    public interface SaleMonthRankView {
        void saleMonthRankSuccess(List<BusinessParseBean> bean);

        void saleMonthRankFail(String failMsg);
    }

    public interface SaleMonthRankPresenter {
        void getSaleMonthRank(int month);
    }
}
