package com.xcxid.dinning.goods;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.adapter.GoodsAdapter;
import com.xcxid.dinning.base.BaseFragment;
import com.xcxid.dinning.goods.offsale.OffSaleFragment;
import com.xcxid.dinning.goods.onsale.OnSaleFragment;
import com.xcxid.dinning.goods.search.SearchCategoryActivity;
import com.xcxid.dinning.url.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class GoodsFragment extends BaseFragment implements OffSaleFragment.IOnSaleNotify,
        OnSaleFragment.IOffSaleNotify {
    @BindView(R.id.etSearch)
    TextView mEtSearch;
    @BindView(R.id.tvSearch)
    TextView mTvSearch;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private static final int REQUEST_SEARCH_CATEGORY = 1000;
    private OnSaleFragment onSaleFragment;
    private OffSaleFragment offSaleFragment;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView() {
        GoodsAdapter mAdapter = new GoodsAdapter(getChildFragmentManager());
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        if (null == onSaleFragment) {
            onSaleFragment = new OnSaleFragment();
        }
        mFragmentList.add(onSaleFragment);
        if (null == offSaleFragment) {
            offSaleFragment = new OffSaleFragment();
        }
        mFragmentList.add(offSaleFragment);
        mAdapter.setFragments(mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initListener() {
        if (null != onSaleFragment) {
            onSaleFragment.setOffSaleNotify(this);
        }
        if (null != offSaleFragment) {
            offSaleFragment.setOnSaleNotify(this);
        }
    }

    @OnClick(R.id.etSearch)
    public void serachCategory(View view) {
        startActivityForResult(new Intent(mActivity, SearchCategoryActivity.class), REQUEST_SEARCH_CATEGORY);
    }

    @OnClick(R.id.tvSearch)
    public void serachCategory2(View view) {
        startActivityForResult(new Intent(mActivity, SearchCategoryActivity.class), REQUEST_SEARCH_CATEGORY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH_CATEGORY && resultCode == Activity.RESULT_OK && null != data) {
            if (data.getBooleanExtra(Constants.CATEGORY_SEARCH_ON, false) && null != offSaleFragment) {
                offSaleFragment.refresh();
            }

            if (data.getBooleanExtra(Constants.CATEGORY_SEARCH_OFF, false) && null != onSaleFragment) {
                onSaleFragment.refresh();
            }
        }
    }

    @Override
    public void onSaleNotify() {
        if (null != offSaleFragment) {
            onSaleFragment.refresh();
        }
    }

    @Override
    public void offSaleNotify() {
        if (null != onSaleFragment) {
            offSaleFragment.refresh();
        }
    }

}
