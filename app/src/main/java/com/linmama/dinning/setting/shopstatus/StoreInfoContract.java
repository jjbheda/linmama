package com.linmama.dinning.setting.shopstatus;

import com.linmama.dinning.bean.AppVersionBean;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.ShopBaseInfoBean;
import com.linmama.dinning.bean.StoreSettingsBean;

/**
 * Created by jingkang on 2017/3/16
 *
 * 店铺状态 当日单状态
 *
 */


public class StoreInfoContract {
    public interface StoreInfoView {
        void getStoreInfoSuccess(ShopBaseInfoBean bean);

        void getStoreInfoFail(String failMsg);
    }

    public interface StoreStatusModifyView {
        void setStoreStatusModifySuccess(String bean);

        void setStoreStatusModifyFail(String failMsg);
    }

    public interface TodayStatusModifyView {
        void setTodayOrderStatusModifySuccess(String bean);

        void setTodayOrderStatusModifyFail(String failMsg);
    }

    public interface getStoreInfo {
        void getStoreInfo();
    }

    public interface modifyStoreStatus {
        void modifyStoreStatus(int status);
    }

    public interface modifyTodayOrderStatus {
        void modifyTodayOrderStatus(int status);
    }
}
