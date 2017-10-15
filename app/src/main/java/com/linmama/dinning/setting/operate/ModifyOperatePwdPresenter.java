package com.linmama.dinning.setting.operate;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.base.BasePresenter;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/13
 */

public class ModifyOperatePwdPresenter extends BasePresenter<ModifyOperatePwdActivity> implements
        ModifyOperatePwdContract.ModifyOperatePwdPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new ModifyOperatePwdModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("ModifyOperatePwd", models[0]);
        return map;
    }

    @Override
    public void modifyOperatePassword(String oldPwd, String newPwd) {
        if (null == getIView())
            return;
        if (getIView().checkNull()) {
            return;
        }
        ((ModifyOperatePwdModel) getiModelMap().get("ModifyOperatePwd"))
                .modifyOperatePassword(oldPwd, newPwd, new ModifyOperatePwdModel.ModifyOperatePwdHint() {
                    @Override
                    public void successModifyOperatePwd(DataBean bean) {
                        if (null == getIView())
                            return;
                        getIView().modifyOperatePwdSuccess(bean);
                    }

                    @Override
                    public void failModifyOperatePwd(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().modifyOperatePwdFail(failMsg);
                    }
                });
    }
}
