package com.linmama.dinning.shop.account;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;

import java.util.List;


/**
 * Created by jingkang on 2017/3/13
 */

public class AccountModel extends BaseModel {

    public void getHistoryBillQueryData(int page,@NonNull int type,
                                 @NonNull final AccountHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleMonthRankHint cannot be null.");
        }

        httpService.getHistoryBillQueryData(page,type)
                .compose(new CommonTransformer<List<AccountBeanItem>>())
                .subscribe(new CommonSubscriber<List<AccountBeanItem>>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(List<AccountBeanItem> beans) {
                        hint.successGetAccount(beans);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        hint.failSaleRank(e.getMessage());
                    }
                });
    }

    public interface AccountHint {
        void successGetAccount(List<AccountBeanItem> beans);

        void failSaleRank(String failMsg);
    }
}
