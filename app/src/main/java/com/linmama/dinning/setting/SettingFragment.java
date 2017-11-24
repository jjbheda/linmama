package com.linmama.dinning.setting;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.AppVersionBean;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.ShopBaseInfoBean;
import com.linmama.dinning.bean.StoreSettingsBean;
import com.linmama.dinning.bluetooth.CheckBTActivity;
import com.linmama.dinning.login.LoginActivity;
import com.linmama.dinning.setting.shopstatus.StoreInfoContract;
import com.linmama.dinning.setting.shopstatus.StoreStatusPresenter;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ActivityUtils;
import com.linmama.dinning.utils.AppUtils;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.asynctask.CallEarliest;
import com.linmama.dinning.utils.asynctask.IProgressListener;
import com.linmama.dinning.utils.asynctask.ProgressCallable;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.SettingItem;
import com.linmama.dinning.R;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.asynctask.AsyncTaskUtils;
import com.linmama.dinning.utils.asynctask.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.linmama.dinning.utils.SpUtils.get;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class SettingFragment extends BasePresenterFragment<StoreStatusPresenter> implements
        StoreInfoContract.StoreInfoView,StoreInfoContract.StoreStatusModifyView{
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
        if (PrintDataService.isConnection()) {
            siPos.setSubTitle(R.string.set_item1_connect);
        } else {
            siPos.setSubTitle(R.string.set_item1_disconnect);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mPresenter.getStoreInfo();
    }

    @OnClick(R.id.btnOpen)
    public void openStore(View view) {
        showDialog("加载中...");
        if (isClosed) {
            mPresenter.modifyStoreStatus(0);
        } else {
            mPresenter.modifyStoreStatus(1);
        }
    }

    @OnClick(R.id.siPos)
    public void checkBT(View view) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == btAdapter) {
            ViewUtils.showSnack(llContent, R.string.bluetooth_not_available);
        } else {
            ActivityUtils.startActivityForResult(this, CheckBTActivity.class, REQUEST_BT_CONNECTION);
        }
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

    @OnClick(R.id.tvLogout)
    public void logOut(View view) {
        new MyAlertDialog(mActivity).builder()
                .setTitle("确认退出当前账户吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SpUtils.remove(Constants.USERNAME);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BT_CONNECTION && resultCode == Activity.RESULT_OK) {
            String btAddress = data.getStringExtra(Constants.BT_ADDRESS);
            if (!TextUtils.isEmpty(btAddress)) {
                LogUtils.d("BTAddress", btAddress);
                if (!TextUtils.isEmpty(btAddress)) {
                    if (PrintDataService.isConnection()) {
                        PrintDataService.disconnect();
                    }
                    AsyncTaskUtils.doProgressAsync(mActivity, ProgressDialog.STYLE_SPINNER, "请稍后...", "正在连接票据打印机",
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
                                        ViewUtils.showToast(mActivity, "已连接票据打印机");
                                        siPos.setSubTitle(R.string.set_item1_connect);
                                    } else {
                                        ViewUtils.showToast(mActivity, "票据打印机连接失败");
                                        siPos.setSubTitle(R.string.set_item1_disconnect);
                                    }
                                }
                            });
                }
            }
        }
        if (requestCode == REQUEST_MODIFY_LOGINPASSWORD && resultCode == Activity.RESULT_OK) {
            SpUtils.remove(Constants.TOKEN);
            ActivityUtils.startActivity(mActivity, LoginActivity.class);
            mActivity.finish();
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
