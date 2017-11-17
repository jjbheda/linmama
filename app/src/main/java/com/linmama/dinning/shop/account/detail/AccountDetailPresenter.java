package com.linmama.dinning.shop.account.detail;


import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.bean.SingleAccountBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.shop.account.AccountContract;
import com.linmama.dinning.shop.account.AccountFragment;
import com.linmama.dinning.shop.account.AccountModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class AccountDetailPresenter extends BasePresenter<AccountDetailFragment> implements AccountContract.DetailHistoryBillQueryPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new AccountModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("AccountModel", models[0]);
        return map;
    }

    /***
     * @param page
     * @param type: 0//0 正常单  1调整单 默认查询正常单
     */
    @Override
    public void getDetailHistoryBillQueryData(int page,String date,int type) {
        if (null == getIView())
            return;
        ((AccountModel) getiModelMap().get("AccountModel"))
                .getDetailHistoryBillQueryData(page, date,type, new AccountModel.AccountDetailHint(){
                    @Override
                    public void successGetAccount(SingleAccountBean bean) {
                        getIView().AccountGetSuccess(bean);
                    }

                    @Override
                    public void failSaleRank(String failMsg) {
                        getIView().AccountGetFail(failMsg);
                    }
                });
    }
}
