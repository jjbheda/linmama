package com.linmama.dinning.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linmama.dinning.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */
public abstract class BaseFragment extends Fragment {
    protected FragmentActivity mActivity;
    private ProgressDialog mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResID(), container, false);
        ButterKnife.bind(this, v);
        LogUtils.d("xcxid_onCreateView", getClass().getName());
        initView();
        initListener();
        return v;
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initListener();

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
