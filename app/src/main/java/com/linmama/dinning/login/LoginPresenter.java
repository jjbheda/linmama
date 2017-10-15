package com.linmama.dinning.login;

import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.UserServerBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.utils.LogUtils;

import java.util.HashMap;

public class LoginPresenter extends BasePresenter<LoginActivity> implements
        LoginContract.UserServerPresenter {

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new UserServerModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("UserServer", models[0]);
        return map;
    }

    @Override
    public void getUserServer(final String usercode) {
        if (null == getIView())
            return;
        ((UserServerModel) getiModelMap().get("UserServer")).getUserServer(usercode, new UserServerModel.UserServerHint() {
            @Override
            public void successServer(UserServerBean bean) {
                if (null == getIView())
                    return;
                getIView().getServerSuccess(bean, usercode);
            }

            @Override
            public void failServer(String failMsg) {
                if (null == getIView())
                    return;
                LogUtils.e("LoginPresenter.failLogin", failMsg);
                getIView().getServerFail(failMsg);
            }
        });

    }

    //    @Override
//    public void login(String name, String pwd) {
//        if (null == getIView())
//            return;
//        if (!getIView().checkNull()) {
//            ((LoginModel) getiModelMap().get("Login")).login(name, pwd, new LoginModel.LoginHint() {
//                @Override
//                public void successLogin(LoginBean bean) {
//                    if (null == getIView())
//                        return;
//                    getIView().loginSuccess(bean);
//                }
//
//                @Override
//                public void failLogin(String failMsg) {
//                    if (null == getIView())
//                        return;
//                    LogUtils.e("LoginPresenter.failLogin", failMsg);
//                    getIView().loginFail(failMsg);
//                }
//            });
//        }
//    }

//    @Override
//    public HashMap<String, IModel> getiModelMap() {
//        return loadModelMap(new LoginModel(), new UserServerModel());
//    }
//
//    @Override
//    public HashMap<String, IModel> loadModelMap(IModel... models) {
//        HashMap<String, IModel> map = new HashMap<>();
//        map.put("Login", models[0]);
//        map.put("UserServer", models[1]);
//        return map;
//    }
}
