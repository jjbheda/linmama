package com.linmama.dinning.setting.shopstatus;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.ShopBaseInfoBean;
import com.linmama.dinning.bean.StoreSettingsBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.setting.SettingFragment;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;

import static com.linmama.dinning.base.BaseModel.httpService;

/**
 * Created by jingkang on 2017/3/16
 */

public class StoreStatusPresenter extends BasePresenter<SettingFragment> implements StoreInfoContract.getStoreInfo,StoreInfoContract.modifyStoreStatus{

    @Override
    public void getStoreInfo() {
        if (null == getIView())
            return;
        httpService.getStoreInfo()
                .compose(new CommonTransformer<ShopBaseInfoBean>())
                .subscribe(new CommonSubscriber<ShopBaseInfoBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(ShopBaseInfoBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getStoreInfoSuccess(bean);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (null == getIView())
                            return;
                        getIView().getStoreInfoFail(e.getMessage());
                    }
                });
    }

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return null;
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        return null;
    }

    @Override
    public void modifyStoreStatus(int status) {
        if (null == getIView())
            return;
        httpService.openOrClose(status)
                .compose(new CommonTransformer())
                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(String msg) {
                            getIView().setStoreStatusModifySuccess(msg);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        getIView().setStoreStatusModifyFail(e.getMessage());
                    }
                });
    }
}
