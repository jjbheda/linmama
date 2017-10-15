package com.xcxid.dinning.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xcxid.dinning.mvp.IView;

import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/3/5
 */

public abstract class BasePresenterActivity<P extends BasePresenter> extends AppCompatActivity implements
        IView {
    protected View view;
    protected P mPresenter;
    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        mPresenter = loadPresenter();
        initCommonData();
        initView();
        initListener();
        initData();
    }

    protected abstract P loadPresenter();

    private void initCommonData() {
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    protected void showDialog(String msg) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = ProgressDialog.show(this, "请稍后", msg, false, true);
        mLoadingDialog.show();
    }

    protected void dismissDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
