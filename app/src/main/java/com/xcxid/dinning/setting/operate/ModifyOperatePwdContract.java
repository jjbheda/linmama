package com.xcxid.dinning.setting.operate;

import com.xcxid.dinning.bean.DataBean;

/**
 * Created by jingkang on 2017/3/13
 */

public class ModifyOperatePwdContract {
    public interface ModifyOperatePwdView {
        String getOldPwd();

        String getNewPwd();

        String getConfirmPwd();

        void modifyOperatePwdSuccess(DataBean bean);

        void modifyOperatePwdFail(String failMsg);
    }

    public interface ModifyOperatePwdPresenter {
        void modifyOperatePassword(String oldPwd, String newPwd);

    }
}
