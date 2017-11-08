package com.linmama.dinning.shop.account;


import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.mvp.IModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class AccountPresenter extends BasePresenter<AccountFragment> implements AccountContract.HistoryBillQueryPresenter {
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

    @Override
    public void getHistoryBillQueryData(int page,int type) {
        if (null == getIView())
            return;
        ((AccountModel) getiModelMap().get("AccountModel"))
                .getHistoryBillQueryData(page, type, new AccountModel.AccountHint() {
                    @Override
                    public void successGetAccount(List<AccountBeanItem> beans) {
                        getIView().AccountGetSuccess(beans);
                    }

                    @Override
                    public void failSaleRank(String failMsg) {
                        getIView().AccountGetFail(failMsg);
                    }
                });
    }
}
