package com.linmama.dinning.setting;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.AppVersionBean;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.StoreSettingsBean;
import com.linmama.dinning.bluetooth.CheckBTActivity;
import com.linmama.dinning.login.LoginActivity;
import com.linmama.dinning.setting.advice.AdviceActivity;
import com.linmama.dinning.setting.complete.CompleteOrderListActivity;
import com.linmama.dinning.setting.operate.ModifyOperatePwdActivity;
import com.linmama.dinning.setting.shopstatus.StoreStatusContract;
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
import com.linmama.dinning.base.MapActivity;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.setting.login.ModifyLoginPwdActivity;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.asynctask.AsyncTaskUtils;
import com.linmama.dinning.utils.asynctask.Callback;

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
        StoreStatusContract.StoreStatusModifyView, StoreStatusContract.StoreStatusView,
        EasyPermissions.PermissionCallbacks, StoreStatusContract.CheckAppVersionView {
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
    @BindView(R.id.siUpdateLoginPwd)
    SettingItem siUpdateLoginPwd;
    @BindView(R.id.siUpdateOperatePwd)
    SettingItem siUpdateOperatePwd;
    @BindView(R.id.siCompleteOrder)
    SettingItem siCompleteOrder;
    @BindView(R.id.siFeedBack)
    SettingItem siFeedBack;
    @BindView(R.id.siService)
    SettingItem siService;
    @BindView(R.id.siVersion)
    SettingItem siVersion;
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
        String userFullname = (String) get(Constants.USER_FULLNAME, "");
        tvName.setText(userFullname);
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
        mPresenter.getStoreStatus();
    }

    @OnClick(R.id.btnOpen)
    public void openStore(View view) {
        showDialog("加载中...");
        if (isClosed) {
            mPresenter.modifyStoreStatus("1");
        } else {
            mPresenter.modifyStoreStatus("2");
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

    @OnClick(R.id.siUpdateLoginPwd)
    public void modifyLoginPwd(View view) {
        ActivityUtils.startActivityForResult(this, ModifyLoginPwdActivity.class, REQUEST_MODIFY_LOGINPASSWORD);
    }

    @OnClick(R.id.siUpdateOperatePwd)
    public void modifyOperatePwdiew(View view) {
        ActivityUtils.startActivity(mActivity, ModifyOperatePwdActivity.class);
    }

    @OnClick(R.id.siCompleteOrder)
    public void completeOrder(View view) {
        ActivityUtils.startActivity(mActivity, CompleteOrderListActivity.class);
    }

    @OnClick(R.id.siFeedBack)
    public void feedBack(View view) {
        ActivityUtils.startActivity(mActivity, AdviceActivity.class);
    }

    @OnClick(R.id.siService)
    public void phoneService(View view) {
        new MyAlertDialog(mActivity).builder()
                .setTitle("拨打客服电话")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callServiceTask();
                    }
                }).show();
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_CALL)
    private void callServiceTask() {
        if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.CALL_PHONE)) {
            callService();
        } else {
            EasyPermissions.requestPermissions(this, "小不点商户想使用您的拨打电话功能以接通客服电话", REQUEST_PERMISSION_CALL, Manifest.permission.CALL_PHONE);
        }
    }

    private void callService() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:4000506390");
        intent.setData(data);
        startActivity(intent);
    }

    @OnClick(R.id.siVersion)
    public void checkVersion(View view) {
//        showDialog("加载中...");
//        mPresenter.getAppVersion("http://work.xcxid.com/api/checkAppVersion/");


//        Toast.makeText(getActivity(),"!!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MapActivity.class);
        startActivity(intent);
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
                        SpUtils.remove(Constants.USERID);
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
    public void getStoreStatusModifySuccess(DataBean bean) {
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
    public void getStoreStatusModifyFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(llContent, failMsg);
        }
    }

    @Override
    public void getStoreStatusSuccess(StoreSettingsBean bean) {
        dismissDialog();
        isClosed = bean.is_closed();
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
    public void getStoreStatusFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(llContent, failMsg);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_PERMISSION_CALL) {
            callService();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
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
    public void getAppVersionSuccess(final AppVersionBean bean) {
        dismissDialog();
        if (null != bean) {
            int version_code = bean.getVersion_code();
            int versionCode = AppUtils.getVersionCode(mActivity);
            if (version_code > versionCode) {
                new MyAlertDialog(mActivity).builder()
                        .setTitle("已有新版本 " + bean.getVersion_no())
                        .setMsg(bean.getContent() + "\n" + bean.getUpdate_date())
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri uri = Uri.parse("http://work.xcxid.com" + bean.getApk());
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }).show();
            } else {
                ViewUtils.showSnack(llContent, "已经是最新版本");
            }
        }
    }

    @Override
    public void getAppVersionFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(llContent, failMsg);
        }
    }
}
