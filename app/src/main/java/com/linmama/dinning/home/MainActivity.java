package com.linmama.dinning.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linmama.dinning.base.BaseActivity;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteFragment;
import com.linmama.dinning.receiver.WarnAlarmReceiver;
import com.linmama.dinning.shop.ShopManagerFragment;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.BuildConfig;
import com.linmama.dinning.R;
import com.linmama.dinning.order.order.OrderFragment;
import com.linmama.dinning.setting.SettingFragment;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.tencent.bugly.crashreport.CrashReport;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity {
    public static String TAG = "MainActivity";
    WarnAlarmReceiver warnAlarmReceiver;
    public static boolean ISCONNECT;//判断是否连接成功
    @BindView(R.id.layTabOrder)
    LinearLayout layTabOrder;
    @BindView(R.id.layTabGoods)
    LinearLayout layTabGoods;
    @BindView(R.id.ltTabDataQuery)
    LinearLayout ltTabDataQuery;
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
    private OrderCompleteFragment mData;
    private SettingFragment mSetting;
    private long exitTime = 0;
    private String mOrdertype = "";   // 订单类型  0 当日单 1预约单
    private int mId = 0;   // 订单id

    private Handler handler;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        Log.d(TAG, "初始化MainActivity");
        mManager = getSupportFragmentManager();
        if (null == mOrder) {
            mOrder = new OrderFragment();
        }
        if (!mOrdertype.equals("") && mId != 0) {   // 订单类型  0 当日单 1预约单
            Bundle args = new Bundle();
            args.putString("OrderType", mOrdertype);
            args.putInt("ID", mId);
            Log.e(TAG,"接收到推送下来的ID值"+mId);

           Log.w("FeiEPrinterUtils","接收到推送下来的ID值"+mId);
           mOrder.setNewOrder(args);
        }
        switchContent(mOrder);

//        replaceContent(new OrderFragment());
        resetSelected();
        menu_order_btn.setTextColor(getResources().getColor(R.color.actionsheet_red));
        imgimgPendingTreat.setImageResource(R.mipmap.pendingtreat_selected);
        if (BuildConfig.DEBUG) {
            JPushInterface.setDebugMode(true);
        } else {
            JPushInterface.setDebugMode(false);
        }
        JPushInterface.init(this);
        String username = (String) SpUtils.get(Constants.USERNAME, "");
        String usercode = (String) SpUtils.get(Constants.USERCODE, "");
        JPushInterface.setAlias(this, usercode + "_" + username, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                LogUtils.d("JPushAlias", "i:" + i);
            }
        });

        CrashReport.initCrashReport(getApplicationContext());

        HandlerThread thread = new HandlerThread("NetWork");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        //延迟一秒后进行
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "请求打印机接口");
                String result = FeiEPrinterUtils.queryPrinterStatusForMainAc();
                if (result.equals("在线")) {
                    ViewUtils.showToast(MainActivity.this, "票据打印机连接成功");
                    Log.d(TAG, "票据打印机连接成功");
                } else {
                    ViewUtils.showToast(MainActivity.this, result);
                    Log.d(TAG, result);
                }
            }
        },100);

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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            try {
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                Iterator<String> it = json.keys();
                while (it.hasNext()) {
                    String myKey = it.next().toString();
                    if (myKey.equals("type")) {
                        mOrdertype = json.optString(myKey);
                    }
                    if (myKey.equals("id")) {
                        mId = json.optInt(myKey);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "Get message extra JSON error!");
            }
        }
        if (mOrder == null) {
            mOrder = new OrderFragment();
        }

        if (!mOrdertype.equals("") && mId != 0) {   // 订单类型  0 当日单 1预约单
            Bundle args = new Bundle();
            args.putString("OrderType", mOrdertype);
            args.putInt("ID", mId);
            Log.e(TAG,"接收到推送下来的ID值"+mId);
            mOrder.setNewOrder(args);
        }
        switchContent(mOrder);

//        replaceContent(new OrderFragment());
        resetSelected();
        menu_order_btn.setTextColor(getResources().getColor(R.color.actionsheet_red));
        imgimgPendingTreat.setImageResource(R.mipmap.pendingtreat_selected);

    }

    private void switchContent(Fragment fragment) {
        FragmentTransaction transaction = mManager.beginTransaction();
        hideFragments(transaction);
        if (!fragment.isAdded()) {
            String tag = fragment.getClass().getName();
            transaction.add(R.id.content, fragment, tag);
            transaction.commit();
        } else if (!fragment.isVisible()){
            transaction.show(fragment).commit();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        String tag1 = OrderFragment.class.getName();
        String tag2 = ShopManagerFragment.class.getName();
        String tag3 = OrderCompleteFragment.class.getName();
        String tag4 = SettingFragment.class.getName();
        OrderFragment frag1 = (OrderFragment) mManager.findFragmentByTag(tag1);
        if (null != frag1) {
            transaction.hide(frag1);
        }
        ShopManagerFragment frag2 = (ShopManagerFragment) mManager.findFragmentByTag(tag2);
        if (null != frag2) {
            transaction.hide(frag2);
        }
        OrderCompleteFragment frag3 = (OrderCompleteFragment) mManager.findFragmentByTag(tag3);
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
        Log.d(TAG, "new Order onclick begin");
        if (null == mOrder) {
            mOrder = new OrderFragment();
        }
        switchContent(mOrder);

        replaceContent(new OrderFragment());
        resetSelected();
        imgimgPendingTreat.setImageResource(R.mipmap.pendingtreat_selected);
        menu_order_btn.setTextColor(getResources().getColor(R.color.actionsheet_red));
        Log.d(TAG, "new Order onclick end");
    }

    @OnClick(R.id.layTabGoods)
    public void showGoods(View view) {
        Log.d(TAG, "shopmanager onclick begin");
        if (null == mshops) {
            mshops = new ShopManagerFragment();
        }
        switchContent(mshops);

        replaceContent(new ShopManagerFragment());
        resetSelected();
        imgShopManager.setImageResource(R.mipmap.shop_manager_selected);
        mShopManagerTv.setTextColor(getResources().getColor(R.color.actionsheet_red));
        Log.d(TAG, "shopmanager onclick end");
    }

    @OnClick(R.id.ltTabDataQuery)
    public void showData(View view) {
        Log.d(TAG, "orderQuery onclick begin");
        if (null == mData) {
            mData = new OrderCompleteFragment();
        }
        switchContent(mData);

        replaceContent(new OrderCompleteFragment());
        resetSelected();
        imgOrderQuery.setImageResource(R.mipmap.order_query_selected);
        mOrderQueryTv.setTextColor(getResources().getColor(R.color.actionsheet_red));
        Log.d(TAG, "orderQuery onclick end");
    }

    @OnClick(R.id.layTabSettings)
    public void showSettings(View view) {
        Log.d(TAG, "setting onclick begin");
        if (null == mSetting) {
            mSetting = new SettingFragment();
        }
        switchContent(mSetting);

        replaceContent(new SettingFragment());
        resetSelected();
        imgTabSettings.setImageResource(R.mipmap.tab_setting_selected);
        mSettingTv.setTextColor(getResources().getColor(R.color.actionsheet_red));
        Log.d(TAG, "setting onclick end");
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
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ViewUtils.showToast(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            try {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
