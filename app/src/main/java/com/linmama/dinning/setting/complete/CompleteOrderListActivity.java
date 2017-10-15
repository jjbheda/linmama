package com.linmama.dinning.setting.complete;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.linmama.dinning.base.BaseActivity;
import com.linmama.dinning.R;

import butterknife.BindView;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompleteOrderListActivity extends BaseActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.content)
    FrameLayout content;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_complete;
    }

    @Override
    protected void initView() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content, new CompleteOrderListFragment(), CompleteOrderListFragment.class.getName()).commit();
    }

    @Override
    protected void initListener() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
