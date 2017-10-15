package com.xcxid.dinning.setting.login;

import com.xcxid.dinning.bean.DataBean;

/**
 * Created by jingkang on 2017/3/13
 */

public class ModifyLoginPwdContract {
    public interface ModifyLoginPwdView {
        String getOldPwd();

        String getNewPwd();

        String getConfirmPwd();

        void modifyLoginPwdSuccess(DataBean bean);

        void modifyLoginPwdFail(String failMsg);
    }

    public interface ModifyLoginPwdPresenter {
        void modifyPassword(String oldPwd, String newPwd);

    }
}
