package com.xcxid.dinning.setting.operate;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.base.BasePresenterActivity;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.utils.ViewUtils;
import com.xcxid.dinning.widget.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/13
 */

public class ModifyOperatePwdActivity extends BasePresenterActivity<ModifyOperatePwdPresenter> implements
        ModifyOperatePwdContract.ModifyOperatePwdView {
    @BindView((R.id.content))
    LinearLayout llContent;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.updateOldPwd)
    ClearEditText updateOldPwd;
    @BindView(R.id.updateNewPwd)
    ClearEditText updateNewPwd;
    @BindView(R.id.updateConfirmPwd)
    ClearEditText updateConfirmPwd;
    @BindView(R.id.updateOk)
    Button updateOk;

    @Override
    protected ModifyOperatePwdPresenter loadPresenter() {
        return new ModifyOperatePwdPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void initView() {
        title.setText("修改操作密码");
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateOldPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() > 0) {
                    if (!TextUtils.isEmpty(getNewPwd()) && !TextUtils.isEmpty(getConfirmPwd())) {
                        updateOk.setEnabled(true);
                    } else {
                        updateOk.setEnabled(false);
                    }
                } else {
                    updateOk.setEnabled(false);
                }
            }
        });

        updateNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() > 0) {
                    if (!TextUtils.isEmpty(getOldPwd()) && !TextUtils.isEmpty(getConfirmPwd())) {
                        updateOk.setEnabled(true);
                    } else {
                        updateOk.setEnabled(false);
                    }
                } else {
                    updateOk.setEnabled(false);
                }
            }
        });

        updateConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length() > 0) {
                    if (!TextUtils.isEmpty(getNewPwd()) && !TextUtils.isEmpty(getOldPwd())) {
                        updateOk.setEnabled(true);
                    } else {
                        updateOk.setEnabled(false);
                    }
                } else {
                    updateOk.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.updateOk)
    public void modifyPwd(View view) {
        showDialog("加载中...");
        mPresenter.modifyOperatePassword(getOldPwd(), getNewPwd());
    }

    @Override
    public String getOldPwd() {
        return updateOldPwd.getText().toString().trim();
    }

    @Override
    public String getNewPwd() {
        return updateNewPwd.getText().toString().trim();
    }

    @Override
    public String getConfirmPwd() {
        return updateConfirmPwd.getText().toString().trim();
    }

    public boolean checkNull() {
        if (TextUtils.isEmpty(getOldPwd())) {
            updateOldPwd.setError("旧密码不能为空");
            ViewUtils.showSnack(llContent, "旧密码不能为空");
            return true;
        } else if (TextUtils.isEmpty(getNewPwd())) {
            updateNewPwd.setError("新密码不能为空");
            ViewUtils.showSnack(llContent, "新密码不能为空");
            return true;
        } else if (!getNewPwd().equals(getConfirmPwd())) {
            updateConfirmPwd.setError("两次密码不一致");
            ViewUtils.showSnack(llContent, "两次密码不一致");
            return true;
        }
        return false;
    }

    @Override
    public void modifyOperatePwdSuccess(DataBean bean) {
        dismissDialog();
        ViewUtils.showSnack(llContent, "修改成功");
    }

    @Override
    public void modifyOperatePwdFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(llContent, failMsg);
        }
    }
}
