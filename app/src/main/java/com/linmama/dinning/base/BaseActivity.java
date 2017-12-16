package com.linmama.dinning.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/2/26
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mLoadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initListener();

    public void showDialog(String title,String msg) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = ProgressDialog.show(this, title, msg, false, true);
        mLoadingDialog.show();
    }

    public void dismissDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

}
