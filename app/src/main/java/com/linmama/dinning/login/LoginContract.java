package com.linmama.dinning.login;

import com.linmama.dinning.bean.LoginBean;
import com.linmama.dinning.bean.UserServerBean;

/**
 * 契约类,定义登录用到的一些接口方法
 */
public class LoginContract {

    public interface LoginView {
        String getUserName();

        String getPwd();

        void loginSuccess(LoginBean loginBean);

        void loginFail(String failMsg);
    }

    public interface UserServerView {
        void getServerSuccess(UserServerBean bean, String usercode);

        void getServerFail(String failMsg);
    }

//    public interface LoginPresenter {
//        void login(String name, String pwd);
//    }

    public interface UserServerPresenter {
        void getUserServer(String usercode);
    }
}
