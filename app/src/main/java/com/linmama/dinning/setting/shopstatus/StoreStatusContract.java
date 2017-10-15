package com.linmama.dinning.setting.shopstatus;

import com.linmama.dinning.bean.AppVersionBean;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.StoreSettingsBean;

/**
 * Created by jingkang on 2017/3/16
 */

public class StoreStatusContract {
    public interface StoreStatusView {
        void getStoreStatusSuccess(StoreSettingsBean bean);

        void getStoreStatusFail(String failMsg);
    }

    public interface StoreStatusModifyView {
        void getStoreStatusModifySuccess(DataBean bean);

        void getStoreStatusModifyFail(String failMsg);
    }

    public interface StoreStatusPresenter {
        void getStoreStatus();
    }

    public interface StoreStatusModifyPresenter {
        void modifyStoreStatus(String opFlag);
    }

    public interface CheckAppVersionView {
        void getAppVersionSuccess(AppVersionBean bean);
        void getAppVersionFail(String failMsg);
    }

    public interface CheckAppVersionPresenter {
        void getAppVersion(String url);
    }
}
