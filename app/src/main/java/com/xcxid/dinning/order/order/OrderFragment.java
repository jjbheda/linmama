package com.xcxid.dinning.order.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.adapter.MenuCategoryAdapter;
import com.xcxid.dinning.adapter.OrderAdapter;
import com.xcxid.dinning.base.BasePresenterFragment;
import com.xcxid.dinning.bean.RedDotStatusBean;
import com.xcxid.dinning.goods.category.MenuCategoryResultsBean;
import com.xcxid.dinning.order.neu.NewFragment;
import com.xcxid.dinning.order.pay.NonPayFragment;
import com.xcxid.dinning.order.quit.QuitFragment;
import com.xcxid.dinning.order.remind.RemindFragment;
import com.xcxid.dinning.order.taking.TakingFragment;
import com.xcxid.dinning.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class OrderFragment extends BasePresenterFragment<RedDotStatusPresenter> implements
        RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener,
        NewFragment.INewHint, RemindFragment.IRemindHint, RedDotStatusContract.RedDotStatusView,
        NonPayFragment.INonPayHint, NewFragment.INewReceiveOrder, QuitFragment.IRefundHint {
    @BindView(R.id.orderGroup)
    RadioGroup mOrderGroup;
    @BindView(R.id.newOrder)
    RadioButton mNewOrder;
    @BindView(R.id.takingOrder)
    RadioButton mTakingOrder;
    @BindView(R.id.remindOrder)
    RadioButton mRemindOrder;
    @BindView(R.id.quitOrder)
    RadioButton mQuitOrder;
    @BindView(R.id.payOrder)
    RadioButton mPayOrder;
    @BindView(android.R.id.content)
    ViewPager mViewPager;

    @BindView(R.id.orderTabWidget)
    ListView mTabWidget;

    private BadgeView mNewBadge;
    private BadgeView mRemindBadge;
    private BadgeView mRefundBadge;
    private BadgeView mNonpayBadge;

    private NewFragment mNewFragment;
    private TakingFragment mTakingFragment;
    private RemindFragment mRemindFragment;
    private QuitFragment mQuitFragment;
    private NonPayFragment mNonPayFragment;
    private MessageReceiver mMessageReceiver;
    public static boolean isForeground = false;
    public static String NEWORDER_REFRESH_ACTION = "com.xcxid.dining.refreshneworder";

    private MenuCategoryAdapter mCategorydapter;
    private TextView lastSelectView;

    @Override
    protected RedDotStatusPresenter loadPresenter() {
        return new RedDotStatusPresenter();
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
        if (null == mRemindFragment) {
            mRemindFragment = new RemindFragment();
        }
        if (null == mQuitFragment) {
            mQuitFragment = new QuitFragment();
        }
        if (null == mNonPayFragment) {
            mNonPayFragment = new NonPayFragment();
        }
        mFragmentList.add(mNewFragment);
        mFragmentList.add(mTakingFragment);
        mFragmentList.add(mRemindFragment);
        mFragmentList.add(mQuitFragment);
        mFragmentList.add(mNonPayFragment);
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
        mRemindOrder.setOnClickListener(this);
        mQuitOrder.setOnClickListener(this);
        mPayOrder.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void initData() {
        mPresenter.getRedDotStatus();
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
        mCategorydapter = new MenuCategoryAdapter(mActivity, results);
        mTabWidget.setAdapter(mCategorydapter);
        mTabWidget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if (position == 0 || position ==1) {
                   showDialog("加载中...");
                   itemBackChanged(view);
                   dismissDialog();

               }

            }
        });
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
                break;
            case 1:
                mOrderGroup.check(R.id.takingOrder);
                break;
            case 2:
                mOrderGroup.check(R.id.remindOrder);
                break;
            case 3:
                mOrderGroup.check(R.id.quitOrder);
                break;
            case 4:
                mOrderGroup.check(R.id.payOrder);
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
            case R.id.takingOrder:
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.remindOrder:
                mViewPager.setCurrentItem(2, true);
                break;
            case R.id.quitOrder:
                mViewPager.setCurrentItem(3, true);
                break;
            case R.id.payOrder:
                mViewPager.setCurrentItem(4, true);
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

    @Override
    public void remindHint() {
        int badgeCount = mRemindBadge.getBadgeCount();
        setRemindCount(badgeCount - 1);
    }

    private void setRemindCount(int count) {
        if (null == mRemindBadge) {
            mRemindBadge = new BadgeView(mActivity);
        }
        mRemindBadge.setTargetView(mRemindOrder);
        if (count == 0) {
            mRemindBadge.setVisibility(View.GONE);
        } else {
            mRemindBadge.setBadgeCount(count);
        }
    }

    @Override
    public void nonPayHint() {
        int badgeCount = mNonpayBadge.getBadgeCount();
        setNonPayCount(badgeCount - 1);
    }

    private void setNonPayCount(int count) {
        if (null == mNonpayBadge) {
            mNonpayBadge = new BadgeView(mActivity);
        }
        mNonpayBadge.setTargetView(mPayOrder);
        if (count == 0) {
            mNonpayBadge.setVisibility(View.GONE);
        } else {
            mNonpayBadge.setBadgeCount(count);
        }
    }

    @Override
    public void refundHint(int count) {
        setRefundCount(count);
    }

    private void setRefundCount(int count) {
        if (null == mRefundBadge) {
            mRefundBadge = new BadgeView(mActivity);
        }
        mRefundBadge.setTargetView(mQuitOrder);
        if (count == 0) {
            mRefundBadge.setVisibility(View.GONE);
        } else {
            mRefundBadge.setBadgeCount(count);
        }
    }

    @Override
    public void getRedDotStatusSuccess(RedDotStatusBean bean) {
        int newCount = bean.getNew_order_count();
        setNewHint(newCount);
        int remindCount = bean.getOrder_warn_count();
        setRemindCount(remindCount);
        int nonpayCount = bean.getNon_payment_order_count();
        setNonPayCount(nonpayCount);
        int refundCount = bean.getRefund_application_count();
        setRefundCount(refundCount);
    }

    @Override
    public void getRedDotStatusFail(String failMsg) {

    }

    @Override
    public void notifyReceiveOrder() {
        if (null != mTakingFragment) {
            mTakingFragment.refresh();
        }
    }

    @Override
    public void notifyNonPayOrder() {
        if (null != mNonPayFragment) {
            mNonPayFragment.refresh();
        }
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
}
