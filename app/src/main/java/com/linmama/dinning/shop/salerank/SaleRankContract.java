package com.linmama.dinning.shop.salerank;


import com.linmama.dinning.shop.bean.SaleRankBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleRankContract {


    public interface SaleRankView {
        void saleRankSuccess(List<SaleRankBean> bean);

        void saleRankFail(String failMsg);
    }

    public interface SaleRankPresenter {
        void getSaleRank(int type);
    }
}
