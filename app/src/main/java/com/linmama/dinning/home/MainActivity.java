package com.linmama.dinning.home;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linmama.dinning.base.BaseActivity;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteSearchFragment;
import com.linmama.dinning.shop.ShopManagerFragment;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.BuildConfig;
import com.linmama.dinning.R;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.data.DataFragment;
import com.linmama.dinning.order.order.OrderFragment;
import com.linmama.dinning.setting.SettingFragment;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.asynctask.AsyncTaskUtils;
import com.linmama.dinning.utils.asynctask.CallEarliest;
import com.linmama.dinning.utils.asynctask.Callback;
import com.linmama.dinning.utils.asynctask.IProgressListener;
import com.linmama.dinning.utils.asynctask.ProgressCallable;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

// Jpush AppKey: 63c6a767cc9437077af1d0a1
// Master Secret: f417e836aa6352486ac8f873
public class MainActivity extends BaseActivity {
    @BindView(R.id.layTabOrder)
    LinearLayout layTabOrder;
    @BindView(R.id.layTabGoods)
    LinearLayout layTabGoods;
    @BindView(R.id.layTabData)
    LinearLayout layTabData;
    @BindView(R.id.layTabSettings)
    LinearLayout layTabSettings;
    @BindView(R.id.imgPendingTreat)
    ImageView imgimgPendingTreat;
    @BindView(R.id.imgShopManager)
    ImageView imgShopManager;
    @BindView(R.id.imgOrderQuery)
    ImageView imgOrderQuery;
    @BindView(R.id.imgTabSettings)
    ImageView imgTabSettings;

    @BindView(R.id.menu_order_btn)
    TextView menu_order_btn;
    @BindView(R.id.order_query_btn)
    TextView mOrderQueryTv;
    @BindView(R.id.shop_manager_btn)
    TextView mShopManagerTv;
    @BindView(R.id.setting_btn)
    TextView mSettingTv;

    private FragmentManager mManager;
    private OrderFragment mOrder;
    private ShopManagerFragment mshops;
    private OrderCompleteSearchFragment mData;
    private SettingFragment mSetting;
    private long exitTime = 0;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mManager = getSupportFragmentManager();

        if (null == mOrder) {
            mOrder = new OrderFragment();
        }
        switchContent(mOrder);

        replaceContent(new OrderFragment());
        resetSelected();
        imgimgPendingTreat.setImageResource(R.mipmap.pendingtreat_selected);
        if (BuildConfig.DEBUG) {
            JPushInterface.setDebugMode(true);
        } else {
            JPushInterface.setDebugMode(false);
        }
        JPushInterface.init(this);
        String username = (String) SpUtils.get(Constants.USERNAME, "");
        String usercode = (String) SpUtils.get(Constants.USERCODE, "");
        JPushInterface.setAlias(this, usercode + "_" +username, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                LogUtils.d("JPushAlias", "i:" + i);
            }
        });
        String btAddress = (String) SpUtils.get(Constants.BT_ADDRESS, "");
        if (!TextUtils.isEmpty(btAddress) && !PrintDataService.isConnection()) {
            AsyncTaskUtils.doProgressAsync(this, ProgressDialog.STYLE_SPINNER, "请稍后...", "正在连接票据打印机",
                    new CallEarliest<Void>() {

                        @Override
                        public void onCallEarliest() throws Exception {

                        }

                    }, new ProgressCallable<Void>() {

                        @Override
                        public Void call(IProgressListener pProgressListener)
                                throws Exception {
                            PrintDataService.init();
                            return null;
                        }

                    }, new Callback<Void>() {

                        @Override
                        public void onCallback(Void pCallbackValue) {
                            if (PrintDataService.isConnection()) {
                                ViewUtils.showToast(MainActivity.this, "已连接票据打印机");
                            } else {
                                ViewUtils.showToast(MainActivity.this, "票据打印机连接失败");
                            }
                        }
                    });
        }
    }

    @Override
    protected void initListener() {
    }

    private void replaceContent(Fragment fragment) {
        FragmentTransaction transaction = mManager.beginTransaction();
        String tag = fragment.getClass().getName();
        transaction.replace(R.id.content, fragment, tag);
        transaction.commit();
    }

    private void switchContent(Fragment fragment) {
        FragmentTransaction transaction = mManager.beginTransaction();
        hideFragments(transaction);
        if (!fragment.isAdded()) {
            String tag = fragment.getClass().getName();
            transaction.add(R.id.content, fragment, tag);
            transaction.commit();
        } else {
            transaction.show(fragment).commit();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        String tag1 = OrderFragment.class.getName();
        String tag2 = ShopManagerFragment.class.getName();
        String tag3 = OrderCompleteSearchFragment.class.getName();
        String tag4 = SettingFragment.class.getName();
        OrderFragment frag1 = (OrderFragment) mManager.findFragmentByTag(tag1);
        if (null != frag1) {
            transaction.hide(frag1);
        }
        ShopManagerFragment frag2 = (ShopManagerFragment) mManager.findFragmentByTag(tag2);
        if (null != frag2) {
            transaction.hide(frag2);
        }
        OrderCompleteSearchFragment frag3 = (OrderCompleteSearchFragment) mManager.findFragmentByTag(tag3);
        if (null != frag3) {
            transaction.hide(frag3);
        }
        SettingFragment frag4 = (SettingFragment) mManager.findFragmentByTag(tag4);
        if (null != frag4) {
            transaction.hide(frag4);
        }
    }

    @OnClick(R.id.layTabOrder)
    public void showOrder(View view) {
        if (null == mOrder) {
            mOrder = new OrderFragment();
        }
        switchContent(mOrder);

        replaceContent(new OrderFragment());
        resetSelected();
        imgimgPendingTreat.setImageResource(R.mipmap.pendingtreat_selected);
        menu_order_btn.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @OnClick(R.id.imgShopManager)
    public void showGoods(View view) {
        if (null == mshops) {
            mshops = new ShopManagerFragment();
        }
        switchContent(mshops);

        replaceContent(new ShopManagerFragment());
        resetSelected();
        imgShopManager.setImageResource(R.mipmap.shop_manager_selected);
        mShopManagerTv.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @OnClick(R.id.imgOrderQuery)
    public void showData(View view) {
        if (null == mData) {
            mData = new OrderCompleteSearchFragment();
        }
        switchContent(mData);

        replaceContent(new OrderCompleteSearchFragment());
        resetSelected();
        imgOrderQuery.setImageResource(R.mipmap.order_query_selected);
        mOrderQueryTv.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @OnClick(R.id.layTabSettings)
    public void showSettings(View view) {
        if (null == mSetting) {
            mSetting = new SettingFragment();
        }
        switchContent(mSetting);

        replaceContent(new SettingFragment());
        resetSelected();
        imgTabSettings.setImageResource(R.mipmap.tab_setting_selected);
        mSettingTv.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void resetSelected() {
        imgimgPendingTreat.setImageResource(R.mipmap.pendingtreat);
        imgOrderQuery.setImageResource(R.mipmap.order_query);
        imgShopManager.setImageResource(R.mipmap.shop_manager);
        imgTabSettings.setImageResource(R.mipmap.tab_setting);

        menu_order_btn.setTextColor(getResources().getColor(R.color.menu_grey));
        mOrderQueryTv.setTextColor(getResources().getColor(R.color.menu_grey));
        mShopManagerTv.setTextColor(getResources().getColor(R.color.menu_grey));
        mSettingTv.setTextColor(getResources().getColor(R.color.menu_grey));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (PrintDataService.isConnection()) {
            PrintDataService.disconnect();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            ViewUtils.showToast(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
