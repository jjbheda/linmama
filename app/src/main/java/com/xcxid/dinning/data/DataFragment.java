package com.xcxid.dinning.data;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.xcxid.dinning.R;
import com.xcxid.dinning.base.BaseFragment;
import com.xcxid.dinning.data.rank.SalesRankFragment;
import com.xcxid.dinning.data.report.SalesStatisticsFragment;
import com.xcxid.dinning.utils.LogUtils;

import butterknife.BindView;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class DataFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(android.R.id.content)
    FrameLayout mContent;
    private FragmentManager mManager;
    private SalesStatisticsFragment mStatistics;
    private SalesRankFragment mRank;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_data;
    }

    @Override
    protected void initView() {
        mManager = getChildFragmentManager();
        FragmentTransaction transaction = mManager.beginTransaction();
        mStatistics = new SalesStatisticsFragment();
        String tag = mStatistics.getClass().getName();
        transaction.add(android.R.id.content, mStatistics, tag);
        transaction.commit();
    }

    @Override
    protected void initListener() {
        mTabLayout.addOnTabSelectedListener(mTabListener);
    }

    private void switchContent(Fragment fragment) {
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.replace(android.R.id.content, fragment).commit();
    }

    TabLayout.OnTabSelectedListener mTabListener = new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            LogUtils.d("onTabSelected", String.valueOf(tab.getPosition()));
            int position = tab.getPosition();
            switch (position) {
                case 0:
                    if (null == mStatistics) {
                        mStatistics = new SalesStatisticsFragment();
                    }
                    switchContent(mStatistics);
                    break;
                case 1:
                    if (null == mRank) {
                        mRank = new SalesRankFragment();
                    }
                    switchContent(mRank);
                    break;

            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

}
