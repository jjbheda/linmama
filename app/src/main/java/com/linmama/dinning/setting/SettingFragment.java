package com.linmama.dinning.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.ShopBaseInfoBean;
import com.linmama.dinning.bluetooth.CheckPrinterActivity;
import com.linmama.dinning.login.LoginActivity;
import com.linmama.dinning.setting.shopstatus.StoreInfoContract;
import com.linmama.dinning.setting.shopstatus.StoreStatusPresenter;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ActivityUtils;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.SettingItem;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class SettingFragment extends BasePresenterFragment<StoreStatusPresenter> implements
        StoreInfoContract.StoreInfoView,StoreInfoContract.StoreStatusModifyView,
        StoreInfoContract.TodayStatusModifyView  {
    public static String TAG = "SettingFragment";
    @BindView(R.id.content)
    LinearLayout llContent;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.siPos)
    SettingItem siPos;
    @BindView(R.id.siRemind)
    SettingItem siRemind;
    @BindView(R.id.siAutoReceiveOrder)
    SettingItem siAutoReceiveOrder;
    @BindView(R.id.siAutoPrint)
    SettingItem siAutoPrint;

    @BindView(R.id.open_today_order)
    SettingItem openTodayOrder;

    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.openStatus)
    TextView openStatus;
    @BindView(R.id.btnOpen)
    Button btnOpen;

    private static final int REQUEST_PERMISSION_CALL = 1001;
    private static final int REQUEST_BT_CONNECTION = 1002;
    private static final int REQUEST_MODIFY_LOGINPASSWORD = 1003;
    private boolean isClosed;
    private boolean isTodayOrderClosed;

    @Override
    protected StoreStatusPresenter loadPresenter() {
        return new StoreStatusPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {
        LogUtils.d("Activity", mActivity.toString());
//        String username = (String) get(Constants.USERNAME, "");
        boolean isVoiceWarn = (boolean) SpUtils.get(Constants.VOICE_WARN, true);
        if (isVoiceWarn) {
            siRemind.setRightIcon(R.mipmap.icon_on);
        } else {
            siRemind.setRightIcon(R.mipmap.icon_off);
        }
        boolean isAutoReceive = (boolean) SpUtils.get(Constants.AUTO_RECEIVE_ORDER, false);
        if (isAutoReceive) {
            siAutoReceiveOrder.setRightIcon(R.mipmap.icon_on);
        } else {
            siAutoReceiveOrder.setRightIcon(R.mipmap.icon_off);
        }
        boolean isAutoPrint = (boolean) SpUtils.get(Constants.AUTO_PRINT, false);
        if (isAutoPrint) {
            siAutoPrint.setRightIcon(R.mipmap.icon_on);
        } else {
            siAutoPrint.setRightIcon(R.mipmap.icon_off);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {

    }

    //handler 处理返回的请求结果
    Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isConnect = data.getBoolean("IS_CONNECT");
            if (isConnect) {
                ViewUtils.showToast(mActivity, "已连接票据打印机");
                siPos.setSubTitle(R.string.set_item1_connect);
            } else {
                ViewUtils.showToast(mActivity, "票据打印机未连接");
                siPos.setSubTitle(R.string.set_item1_disconnect);
            }
        }
    };

    @Override
    protected void initData() {
        mPresenter.getStoreInfo();
        HandlerThread thread = new HandlerThread("checkNetWork");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        //延迟一秒后进行
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "请求打印机接口");
                boolean isConnect = false;
                if (FeiEPrinterUtils.queryPrinterStatus()) {
                    isConnect = true;
//                    Toast.makeText(MainActivity.this, "票据打印机连接成功", Toast.LENGTH_SHORT).show();
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("IS_CONNECT",isConnect);
                msg.setData(data);
                updateHandler.sendMessage(msg);

            }
        },100);
    }

    @OnClick(R.id.btnOpen)
    public void openStore(View view) {
        showDialog("加载中...");
        Log.d("http","加载中  Setting openStore .....。。。。。。。。。。。。。。。。。。。。。。");
        if (isClosed) {
            mPresenter.modifyStoreStatus(0);
        } else {
            mPresenter.modifyStoreStatus(1);
        }
    }

    @OnClick(R.id.siPos)
    public void checkBT(View view) {
        ActivityUtils.startActivityForResult(this, CheckPrinterActivity.class, REQUEST_BT_CONNECTION);
    }

    @OnClick(R.id.siRemind)
    public void voiceWarn(View view) {
        boolean isVoiceWarn = (boolean) SpUtils.get(Constants.VOICE_WARN, true);
        if (isVoiceWarn) {
            ViewUtils.showSnack(llContent, "已关闭订单语音提醒");
            SpUtils.put(Constants.VOICE_WARN, false);
            siRemind.setRightIcon(R.mipmap.icon_off);
        } else {
            ViewUtils.showSnack(llContent, "已打开订单语音提醒");
            SpUtils.put(Constants.VOICE_WARN, true);
            siRemind.setRightIcon(R.mipmap.icon_on);
        }
    }

    @OnClick(R.id.siAutoReceiveOrder)
    public void autoReceiveOrder(View view) {
        boolean isAutoReceiveOrder = (boolean) SpUtils.get(Constants.AUTO_RECEIVE_ORDER, false);
        if (isAutoReceiveOrder) {
            ViewUtils.showSnack(llContent, "已关闭自动接单");
            SpUtils.put(Constants.AUTO_RECEIVE_ORDER, false);
            siAutoReceiveOrder.setRightIcon(R.mipmap.icon_off);
        } else {
            ViewUtils.showSnack(llContent, "已打开自动接单");
            SpUtils.put(Constants.AUTO_RECEIVE_ORDER, true);
            siAutoReceiveOrder.setRightIcon(R.mipmap.icon_on);
        }
    }

    @OnClick(R.id.siAutoPrint)
    public void autoPrint(View view) {
        boolean isAutoPrint = (boolean) SpUtils.get(Constants.AUTO_PRINT, false);
        if (isAutoPrint) {
            ViewUtils.showSnack(llContent, "已关闭自动打印");
            SpUtils.put(Constants.AUTO_PRINT, false);
            siAutoPrint.setRightIcon(R.mipmap.icon_off);
        } else {
            ViewUtils.showSnack(llContent, "已打开自动打印");
            SpUtils.put(Constants.AUTO_PRINT, true);
            siAutoPrint.setRightIcon(R.mipmap.icon_on);
        }
    }

    @OnClick(R.id.open_today_order)
    public void openOrCloseTodayOrder(View view) {
        boolean isOpenOder = (boolean) SpUtils.get(Constants.OPEN_TODAY_ORDER, false);
        showDialog("设置中...");
        Log.d("http","加载中  Setting openStore .....。。。。。。。。。。。。。。。。。。。。。。");
        if (isOpenOder) {
            mPresenter.modifyTodayOrderStatus(1);
        } else {
            mPresenter.modifyTodayOrderStatus(0);
        }
    }

    @OnClick(R.id.tvLogout)
    public void logOut(View view) {
        new MyAlertDialog(mActivity).builder()
                .setTitle("确认退出当前账户吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        SpUtils.remove(Constants.USERNAME);
                        SpUtils.remove(Constants.TOKEN);
                        SpUtils.remove(Constants.PASSWORD);
                        SpUtils.remove(Constants.BT_ADDRESS);
                        ActivityUtils.startActivity(mActivity, LoginActivity.class);
//                        Intent i = new Intent(mActivity, LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
                        mActivity.finish();
                    }
                }).show();
    }

    @Override
    public void setStoreStatusModifySuccess(String bean) {
        dismissDialog();
        if (isClosed) {
            openStatus.setText(R.string.set_open_status);
            btnOpen.setText(R.string.set_close);
            btnOpen.setBackgroundResource(R.drawable.order_refuse_bg);
            btnOpen.setTextColor(getResources().getColor(R.color.colorOrderAppoint));
            isClosed = false;
        } else {
            openStatus.setText(R.string.set_close_status);
            btnOpen.setText(R.string.set_open);
            btnOpen.setBackgroundResource(R.drawable.order_take_bg);
            btnOpen.setTextColor(getResources().getColor(R.color.colorOrderTake));
            isClosed = true;
        }
    }

    @Override
    public void setStoreStatusModifyFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(llContent, failMsg);
        }
    }

    @Override
    public void getStoreInfoFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(llContent, failMsg);
        }
    }

    @Override
    public void getStoreInfoSuccess(ShopBaseInfoBean bean) {
        dismissDialog();
        isClosed = bean.is_open.equals("0");

        if (isClosed) {
            openStatus.setText(R.string.set_close_status);
            btnOpen.setText(R.string.set_open);
            btnOpen.setBackgroundResource(R.drawable.order_take_bg);
            btnOpen.setTextColor(getResources().getColor(R.color.colorOrderTake));
        } else {
            openStatus.setText(R.string.set_open_status);
            btnOpen.setText(R.string.set_close);
            btnOpen.setBackgroundResource(R.drawable.order_refuse_bg);
            btnOpen.setTextColor(getResources().getColor(R.color.colorOrderAppoint));
        }

        isTodayOrderClosed = bean.is_current_open.endsWith("0");

        if (isTodayOrderClosed) {
            openTodayOrder.setRightIcon(R.mipmap.icon_off);
            SpUtils.put(Constants.OPEN_TODAY_ORDER, false);
        } else {
            openTodayOrder.setRightIcon(R.mipmap.icon_on);
            SpUtils.put(Constants.OPEN_TODAY_ORDER, true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void setTodayOrderStatusModifySuccess(String bean) {
        dismissDialog();
        if (isTodayOrderClosed) {
            ViewUtils.showSnack(llContent, "已打开当日单");
            openTodayOrder.setRightIcon(R.mipmap.icon_on);
            SpUtils.put(Constants.OPEN_TODAY_ORDER, true);
            isTodayOrderClosed = false;
        } else {
            ViewUtils.showSnack(llContent, "已关闭当日单");
            SpUtils.put(Constants.OPEN_TODAY_ORDER, false);
            openTodayOrder.setRightIcon(R.mipmap.icon_off);
            isTodayOrderClosed = true;
        }
    }

    @Override
    public void setTodayOrderStatusModifyFail(String failMsg) {
        dismissDialog();
        ViewUtils.showSnack(llContent, "改变状态失败，请稍后重试!");
    }
}
