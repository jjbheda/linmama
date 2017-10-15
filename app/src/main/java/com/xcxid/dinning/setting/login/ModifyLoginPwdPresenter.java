package com.xcxid.dinning.setting.login;

import com.xcxid.dinning.base.BasePresenter;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.mvp.IModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/13
 */

public class ModifyLoginPwdPresenter extends BasePresenter<ModifyLoginPwdActivity> implements
        ModifyLoginPwdContract.ModifyLoginPwdPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new ModifyLoginPwdModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("ModifyLoginPwd", models[0]);
        return map;
    }

    @Override
    public void modifyPassword(String oldPwd, String newPwd) {
        if (null == getIView())
            return;
        if (getIView().checkNull()) {
            return;
        }
        ((ModifyLoginPwdModel) getiModelMap().get("ModifyLoginPwd"))
                .modifyPassword(oldPwd, newPwd, new ModifyLoginPwdModel.ModifyLoginPwdHint() {
                    @Override
                    public void successModifyLoginPwd(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().modifyLoginPwdSuccess(bean);
                    }

                    @Override
                    public void failModifyLoginPwd(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().modifyLoginPwdFail(failMsg);
                    }
                });
    }
}
