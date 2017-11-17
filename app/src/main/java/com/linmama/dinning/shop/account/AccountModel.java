package com.linmama.dinning.shop.account;

import android.support.annotation.NonNull;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.bean.SingleAccountBean;
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

    public void getDetailHistoryBillQueryData(int page,String date,@NonNull int type,
                                        @NonNull final AccountDetailHint hint) {
        if (null == hint) {
            throw new RuntimeException("SaleMonthRankHint cannot be null.");
        }

        httpService.getBillDetailListData(page,date,type)
                .compose(new CommonTransformer<SingleAccountBean>())
                .subscribe(new CommonSubscriber<SingleAccountBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(SingleAccountBean bean) {
                        hint.successGetAccount(bean);
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

    public interface AccountDetailHint {
        void successGetAccount(SingleAccountBean bean);

        void failSaleRank(String failMsg);
    }
}
