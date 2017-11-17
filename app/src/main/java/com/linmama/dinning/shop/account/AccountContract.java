package com.linmama.dinning.shop.account;


import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.bean.SingleAccountBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class AccountContract {
    public interface AccountView {
        void AccountGetSuccess(List<AccountBeanItem> bean);

        void AccountGetFail(String failMsg);
    }

    public interface AccountDetailView {
        void AccountGetSuccess(SingleAccountBean bean);

        void AccountGetFail(String failMsg);
    }

    public interface HistoryBillQueryPresenter {
        void getHistoryBillQueryData(int page,int type);
    }

    public interface DetailHistoryBillQueryPresenter {
        void getDetailHistoryBillQueryData(int page,String date,int type);
    }
}
