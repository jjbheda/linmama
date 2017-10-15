package com.xcxid.dinning.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcxid.dinning.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;

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
        initView();
        initListener();
        return v;
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initListener();
}
