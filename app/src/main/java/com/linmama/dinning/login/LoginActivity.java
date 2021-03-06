package com.linmama.dinning.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.LoginBean;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.receiver.TagAliasOperatorHelper;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.R;
import com.linmama.dinning.bean.UserServerBean;
import com.linmama.dinning.utils.ActivityUtils;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.widget.ClearEditText;
import com.linmama.dinning.widget.MyAlertDialog;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by jingkang on 2017/3/5
 * 1、Activity做了一些UI初始化的东西并需要实例化对应LoginPresenter的引用和实现LoginView的接口,监听界面动作;
 * 2、登录按钮按下后即接收到登录的事件,通过LoginPresenter的引用把他交给LoginPresenter处理, LoginPresenter
 * 接收到了登陆的逻辑就知道要登陆了;
 * 3、LoginPresenter显示进度条并且把逻辑交给我们的Model去处理，也就是这里面的LoginModel，（LoginModel的实现类LoginModelImpl），
 * 同时会把OnLoginFinishedListener也就是LoginPresenter自身传递给我们的Model（LoginModel);
 * 4、LoginModel处理完逻辑之后，结果通过OnLoginFinishedListener回调通知LoginPresenter;
 * 5、LoginPresenter再把结果返回给view层的Activity，最后activity显示结果;
 */
//admin / 123456
public class LoginActivity extends BasePresenterActivity<LoginPresenter> implements LoginContract.LoginView,
        LoginContract.UserServerView, MyAlertDialog.ICallBack {
    @BindView(R.id.loginName)
    ClearEditText mLoginName;
    @BindView(R.id.loginPwd)
    ClearEditText mLoginPwd;
    @BindView(R.id.login)
    TextView mLoginUsercode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = (String) SpUtils.get(Constants.TOKEN, "");
        String username = (String) SpUtils.get(Constants.USERNAME, "");
        String password = (String) SpUtils.get(Constants.PASSWORD, "");
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            ActivityUtils.startActivity(this, MainActivity.class);
            finish();
        }

    }

    @Override
    protected LoginPresenter loadPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
       String username = (String) SpUtils.get(Constants.USERNAME, "");
        mLoginName.setText(username);

    }

    @Override
    protected void initListener() {
        mLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void initData() {

    }
    private String userName = "";
    private String password = "";

    @OnClick(R.id.login)
    public void login(View view) {
        showDialog("登录中...");

        if (!checkNull()) {
            userName = getUserName();
            password = getPwd();
            LoginModel login = new LoginModel();

            login.login(getUserName(), getPwd(), new LoginModel.LoginHint() {
                @Override
                public void successLogin(LoginBean loginBean) {
                    LoginActivity.this.loginSuccess(loginBean);
                }

                @Override
                public void failLogin(String failMsg) {
                    LoginActivity.this.loginFail(failMsg);
                }
            });
        }
    }

    public boolean checkNull() {
        boolean isNull = false;
        if (TextUtils.isEmpty(getUserName())) {
            mLoginName.setError("账号不能为空");
            isNull = true;
        } else if (TextUtils.isEmpty(getPwd())) {
            mLoginPwd.setError("密码不能为空");
            isNull = true;
        }
        return isNull;
    }

    @Override
    public String getUserName() {
        return mLoginName.getText().toString().trim();
    }

    @Override
    public String getPwd() {
        return mLoginPwd.getText().toString().trim();
    }

    @Override
    public void loginSuccess(LoginBean loginBean) {
        dismissDialog();
        LogUtils.d("loginSuccess", loginBean.toString());
        if (!TextUtils.isEmpty(userName)) {
            SpUtils.put(Constants.USERNAME, userName);
        }
        if (!TextUtils.isEmpty(password)) {
            SpUtils.put(Constants.PASSWORD, password);
        }
        if (!TextUtils.isEmpty(loginBean.getSession_id())) {
            SpUtils.put(Constants.COOKIE, loginBean.getSession_id());
        }

        if (!TextUtils.isEmpty(loginBean.getToken())) {
            SpUtils.put(Constants.TOKEN, loginBean.getToken());
            String alias = loginBean.userid;
            boolean isAliasAction = true;

            if(TextUtils.isEmpty(alias)){
                return;
            }
            int action = TagAliasOperatorHelper.ACTION_SET;

            TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
            tagAliasBean.action = action;
            TagAliasOperatorHelper.sequence ++;
            tagAliasBean.alias = alias;

            tagAliasBean.isAliasAction = isAliasAction;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(),TagAliasOperatorHelper.sequence,tagAliasBean);
            ActivityUtils.startActivity(LoginActivity.this, MainActivity.class);
            this.finish();
        }
    }

    @Override
    public void loginFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(findViewById(android.R.id.content), failMsg);
            LogUtils.d("loginFail", failMsg);
        }
    }

    @Override
    public void getServerSuccess(UserServerBean bean, String usercode) {
        dismissDialog();
        if (null != bean) {
            ViewUtils.showSnack(findViewById(android.R.id.content), "用户验证成功");
            LogUtils.d("getServerSuccess", bean.toString());
            SpUtils.put(Constants.USER_SERVER, bean.getServer_address());
            SpUtils.put(Constants.USER_FULLNAME, bean.getUser_fullname());
            SpUtils.put(Constants.USERCODE, usercode);
        }
    }

    @Override
    public void getServerFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(findViewById(android.R.id.content), failMsg);
            LogUtils.d("loginFail", failMsg);
        }
    }

    @Override
    public void onEditText(String text) {
        if (TextUtils.isEmpty(text)) {
            ViewUtils.showSnack(findViewById(android.R.id.content), "未填写用户编号");
        } else {
            showDialog("加载中...");
            mPresenter.getUserServer(text);
        }
    }
}
