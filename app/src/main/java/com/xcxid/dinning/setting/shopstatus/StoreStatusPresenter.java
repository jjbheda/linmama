package com.xcxid.dinning.setting.shopstatus;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.AppVersionBean;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.bean.StoreSettingsBean;
import com.xcxid.dinning.mvp.IModel;
import com.xcxid.dinning.setting.SettingFragment;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/16
 */

public class StoreStatusPresenter extends BasePresenter<SettingFragment> implements StoreStatusContract.StoreStatusModifyPresenter,
        StoreStatusContract.StoreStatusPresenter, StoreStatusContract.CheckAppVersionPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new StoreStatusModel(), new StoreStatusModifyModel(),
                new CheckAppVersionModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("StoreStatus", models[0]);
        map.put("StoreStatusModify", models[1]);
        map.put("CheckAppVersion", models[2]);
        return map;
    }

    @Override
    public void modifyStoreStatus(String opFlag) {
        if (null == getIView())
            return;
        ((StoreStatusModifyModel) getiModelMap().get("StoreStatusModify")).openOrClose(opFlag,
                new StoreStatusModifyModel.StoreStatusModifyHint() {
                    @Override
                    public void successStoreStatusModify(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getStoreStatusModifySuccess(bean);
                    }

                    @Override
                    public void failStoreStatusModify(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getStoreStatusModifyFail(failMsg);
                    }
                });
    }

    @Override
    public void getStoreStatus() {
        if (null == getIView())
            return;
        ((StoreStatusModel) getiModelMap().get("StoreStatus")).getStoreStatus(
                new StoreStatusModel.StoreStatusHint() {

                    @Override
                    public void successStoreStatus(StoreSettingsBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getStoreStatusSuccess(bean);
                    }

                    @Override
                    public void failStoreStatus(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getStoreStatusFail(failMsg);
                    }
                });
    }

    @Override
    public void getAppVersion(String url) {
        if (null == getIView())
            return;
        ((CheckAppVersionModel) getiModelMap().get("CheckAppVersion")).getAppVersion(url,
                new CheckAppVersionModel.CheckAppVersionHint() {

                    @Override
                    public void successCheckAppVersion(AppVersionBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getAppVersionSuccess(bean);
                    }

                    @Override
                    public void failCheckAppVersion(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getAppVersionFail(failMsg);
                    }
                });
    }
}
