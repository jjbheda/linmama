package com.linmama.dinning.order.order;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.linmama.dinning.adapter.MenuCategoryAdapter;
import com.linmama.dinning.adapter.OrderAdapter;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.order.neu.NewFragment;
import com.linmama.dinning.order.ordersearch.OrderSearchActivity;
import com.linmama.dinning.order.taking.TakingFragment;
import com.linmama.dinning.R;
import com.linmama.dinning.goods.category.MenuCategoryResultsBean;
import com.linmama.dinning.order.today.TodayFragment;
import com.linmama.dinning.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class OrderFragment extends BasePresenterFragment implements
        RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener,
        NewFragment.INewHint,
        NewFragment.INewReceiveOrder {
    @BindView(R.id.toolBar_common)
    Toolbar mToolBar_common;
    @BindView(R.id.toolBar_taking)
    Toolbar mToolBar_taking;
    @BindView(R.id.orderGroup)
    RadioGroup mOrderGroup;
    @BindView(R.id.newOrder)
    RadioButton mNewOrder;

    @BindView(R.id.icon_today_search)
    ImageView mIconTodaySearch;

    @BindView(R.id.icon_taking_search)
    ImageView mIconTakingSearch;

    @BindView(R.id.takingOrder)
    RadioButton mTakingOrder;

    @BindView(R.id.todayOrder)
    RadioButton mTodayOrder;

    @BindView(R.id.order_today_title_tv)
    RadioButton mTodayTitleOrder;

    @BindView(R.id.order_tomorrow_title_tv)
    RadioButton mTomorrowTitleOrder;

    @BindView(R.id.order_all_title_tv)
    RadioButton mAllTitleOrder;

    @BindView(android.R.id.content)
    ViewPager mViewPager;

    @BindView(R.id.orderTabWidget)
    ListView mTabWidget;

    private BadgeView mNewBadge;
    private BadgeView mRemindBadge;

    private NewFragment mNewFragment;
    private TakingFragment mTakingFragment;
    private TodayFragment mTodayFragment;
    private MessageReceiver mMessageReceiver;
    public static boolean isForeground = false;
    public static String NEWORDER_REFRESH_ACTION = "com.xcxid.dining.refreshneworder";

    private MenuCategoryAdapter mCategorydapter;
    private TextView lastSelectView;
    private AppCompatActivity appCompatActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        OrderAdapter mAdapter = new OrderAdapter(getChildFragmentManager());
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        if (null == mNewFragment) {
            mNewFragment = new NewFragment();
        }
        if (null == mTakingFragment) {
            mTakingFragment = new TakingFragment();
        }
        if (null == mTodayFragment) {
            mTodayFragment = new TodayFragment();
        }
        mFragmentList.add(mNewFragment);
        mFragmentList.add(mTakingFragment);
        mFragmentList.add(mTodayFragment);
        mAdapter.setFragments(mFragmentList);
        mViewPager.setAdapter(mAdapter);
//        mViewPager.setOffscreenPageLimit(4);
        mOrderGroup.check(R.id.newOrder);
    }

    @Override
    protected void initListener() {
        mOrderGroup.setOnCheckedChangeListener(this);
        mNewOrder.setOnClickListener(this);
        mTakingOrder.setOnClickListener(this);
        mTodayOrder.setOnClickListener(this);

        mTodayTitleOrder.setOnClickListener(this);
        mTomorrowTitleOrder.setOnClickListener(this);
        mAllTitleOrder.setOnClickListener(this);

        mIconTodaySearch.setOnClickListener(this);
        mIconTakingSearch.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void initData() {
        List<MenuCategoryResultsBean> results = new ArrayList<>();
        MenuCategoryResultsBean allMenu = new MenuCategoryResultsBean();
        allMenu.setId(0);
        allMenu.setName("全部");

        MenuCategoryResultsBean allMenu2 = new MenuCategoryResultsBean();
        allMenu2.setId(1);
        allMenu2.setName("外卖");

        MenuCategoryResultsBean allMenu3 = new MenuCategoryResultsBean();
        allMenu3.setId(2);
        allMenu3.setName("堂点");
        results.add(allMenu);
        results.add(allMenu2);
        results.add(allMenu3);
//        mCategorydapter = new MenuCategoryAdapter(mActivity, results);
//        mTabWidget.setAdapter(mCategorydapter);
//        mTabWidget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               if (position == 0 || position ==1) {
//                   showDialog("加载中...");
//                   itemBackChanged(view);
//                   dismissDialog();
//               }
//            }
//        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mOrderGroup.check(R.id.newOrder);
                hideTileTaking(false);
                break;
            case 1:
                mOrderGroup.check(R.id.takingOrder);
                showTileTaking();
                break;
            case 2:
                mOrderGroup.check(R.id.todayOrder);
                hideTileTaking(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newOrder:
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.takingOrder:          //预约单
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.todayOrder:  //当日单
                mViewPager.setCurrentItem(2, true);
                break;
            case R.id.icon_today_search:
                Intent intent = new Intent(appCompatActivity, OrderSearchActivity.class);
                Bundle args = new Bundle();
                args.putInt("OrderType",0);
                intent.putExtras(args);
                appCompatActivity.startActivity(intent);
                break;
            case R.id.icon_taking_search:
                Intent intent2 = new Intent(appCompatActivity, OrderSearchActivity.class);
                Bundle args2 = new Bundle();
                args2.putInt("OrderType",1);
                intent2.putExtras(args2);
                appCompatActivity.startActivity(intent2);
                break;
            case R.id.order_today_title_tv:
                if (mTakingFragment!=null){
                    mTakingFragment.refresh();
                    mTakingFragment.getPresenter().getTakingOrder(0);
                }
                break;
            case R.id.order_tomorrow_title_tv:
                if (mTakingFragment!=null){
                    mTakingFragment.refresh();
                    mTakingFragment.getPresenter().getTakingOrder(1);
                }
                break;

            case R.id.order_all_title_tv:
                if (mTakingFragment!=null){
                    mTakingFragment.refresh();
                    mTakingFragment.getPresenter().getTakingOrder(2);
                }
                break;
            default:
                break;
        }
    }

    private void itemBackChanged(View view) {
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        if (null == tvName || tvName == lastSelectView) {
            return;
        }
        if (null != lastSelectView) {
            lastSelectView.setBackgroundColor(getResources().getColor(R.color.colorMenuTabBg));
        }
        tvName.setBackgroundColor(Color.WHITE);
        lastSelectView = tvName;
    }


    @Override
    public void newHint() {
        int count = mNewBadge.getBadgeCount();
        setNewHint(count - 1);
    }

    private void setNewHint(int count) {
        if (null == mNewBadge) {
            mNewBadge = new BadgeView(mActivity);
        }
        mNewBadge.setTargetView(mNewOrder);
        if (count == 0) {
            mNewBadge.setVisibility(View.GONE);
        } else {
            mNewBadge.setBadgeCount(count);
        }
    }

    private void setRemindCount(int count) {
        if (null == mRemindBadge) {
            mRemindBadge = new BadgeView(mActivity);
        }
        mRemindBadge.setTargetView(mTodayOrder);
        if (count == 0) {
            mRemindBadge.setVisibility(View.GONE);
        } else {
            mRemindBadge.setBadgeCount(count);
        }
    }


    @Override
    public void notifyReceiveOrder() {
        if (null != mTakingFragment) {
            mTakingFragment.refresh();
        }
    }

    @Override
    public void notifyNonPayOrder() {

    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(NEWORDER_REFRESH_ACTION);
        mActivity.registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        registerMessageReceiver();
        return rootView;
    }

    @Override
    protected BasePresenter loadPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mMessageReceiver);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NEWORDER_REFRESH_ACTION.equals(intent.getAction())) {
                if (null != mNewFragment) {
                    mNewFragment.refresh();
                }
            }
        }
    }
    /**
     * 预约单 标题栏显示
     */
    public void showTileTaking(){
        mToolBar_common.setVisibility(View.GONE);
        mToolBar_taking.setVisibility(View.VISIBLE);
    }

    public void hideTileTaking(boolean showSearchIcon){
        mToolBar_common.setVisibility(View.VISIBLE);
        mToolBar_taking.setVisibility(View.GONE);
        if (showSearchIcon)
            mIconTodaySearch.setVisibility(View.VISIBLE);
        else
            mIconTodaySearch.setVisibility(View.INVISIBLE);
    }

}
