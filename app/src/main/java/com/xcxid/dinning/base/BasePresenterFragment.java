package com.xcxid.dinning.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcxid.dinning.mvp.IView;
import com.xcxid.dinning.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */
public abstract class BasePresenterFragment<P extends BasePresenter> extends Fragment implements
        IView {
    protected Activity mActivity;
    protected View view;
    protected P mPresenter;
    private ProgressDialog mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResID(), container, false);
        ButterKnife.bind(this, v);
        LogUtils.d("xcxid_onCreateView", getClass().getName());
        mPresenter = loadPresenter();
        initCommonData();
        initView();
        initListener();
        initData();
        return v;
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
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("onDestroyView", "onDestroyView");
        if (mPresenter != null)
            mPresenter.detachView();
    }

    protected void showDialog(String msg) {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = ProgressDialog.show(mActivity, "请稍后", msg, false, true);
        mLoadingDialog.show();
    }

    protected void dismissDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

}
