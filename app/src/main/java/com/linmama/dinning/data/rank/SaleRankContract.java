package com.linmama.dinning.data.rank;

import com.linmama.dinning.bean.SaleRankBean;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleRankContract {

    public interface SaleDayRankView {
        void saleDayRankSuccess(SaleRankBean bean);

        void saleDayRankFail(String failMsg);
    }

    public interface SaleMonthRankView {
        void saleMonthRankSuccess(SaleRankBean bean);

        void saleMonthRankFail(String failMsg);
    }

    public interface SaleDayRankPresenter {
        void getSaleDayRank(String day);
    }

    public interface SaleMonthRankPresenter {
        void getSaleMonthRank(int year, int month);
    }
}
