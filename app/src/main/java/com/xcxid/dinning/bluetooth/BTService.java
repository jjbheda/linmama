package com.xcxid.dinning.bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.adapter.BTDeviceAdapter;
import com.xcxid.dinning.url.Constants;
import com.xcxid.dinning.utils.SpUtils;
import com.xcxid.dinning.utils.ViewUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by jingkang on 2017/3/13
 */

public class BTService {
    private Activity context = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothDevice> unbondDevices = null;
    private ArrayList<BluetoothDevice> bondDevices = null;
    private ArrayList<BTDeviceBean> allDevices = null;
    private ImageView switchBT = null;
    private TextView statusBT = null;
    private Button searchDevices = null;
    private ListView devicesListView = null;

    public BTService(Activity context, ListView devicesListView,
                     ImageView switchBT, TextView statusBT, Button searchDevices) {
        this.context = context;
        this.devicesListView = devicesListView;
        this.unbondDevices = new ArrayList<>();
        this.bondDevices = new ArrayList<>();
        this.switchBT = switchBT;
        this.statusBT = statusBT;
        this.searchDevices = searchDevices;
        this.initIntentFilter();
    }

    private void initIntentFilter() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        context.registerReceiver(receiver, intentFilter);
    }

    public void unregisterReceiver() {
        context.unregisterReceiver(receiver);
    }

    /**
     * open bt
     */
    public void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 1);
    }

    /**
     * close bt
     */
    public void closeBluetooth() {
        if (this.bluetoothAdapter.disable()) {
            this.bondDevices.clear();
            this.unbondDevices.clear();
            this.allDevices.clear();
        }
    }

    /**
     * check wheather bt is open
     *
     * @return boolean
     */
    public boolean isOpen() {
        return this.bluetoothAdapter.isEnabled();
    }

    /**
     * search bt devices
     */
    public void searchDevices() {
        this.bondDevices.clear();
        this.unbondDevices.clear();
        this.bluetoothAdapter.startDiscovery();
    }

    private void addUnbondDevices(BluetoothDevice device) {
        if (!this.unbondDevices.contains(device)) {
            this.unbondDevices.add(device);
        }
    }

    private void addBandDevices(BluetoothDevice device) {
        if (!this.bondDevices.contains(device)) {
            this.bondDevices.add(device);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        ProgressDialog progressDialog = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    addBandDevices(device);
                } else {
                    addUnbondDevices(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                progressDialog = ProgressDialog.show(context, "请稍等...", "搜索蓝牙设备中...", true);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (null != progressDialog && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                addDevicesToListView();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    statusBT.setText("关闭蓝牙");
                    switchBT.setImageResource(R.mipmap.icon_on);
                    // searchDevices.setEnabled(true);
                    // devicesListView.setEnabled(true);
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    statusBT.setText("打开蓝牙");
                    switchBT.setImageResource(R.mipmap.icon_off);
                    // searchDevices.setEnabled(false);
                    // devicesListView.setEnabled(false);
                }
            }

        }

    };


    private void addDevicesToListView() {
        allDevices = new ArrayList<>();
        for (int i = 0, size = bondDevices.size(); i < size; i++) {
            BTDeviceBean bean = new BTDeviceBean(bondDevices.get(i), true);
            allDevices.add(bean);
        }
        bondDevices.clear();
        for (int i = 0, size = unbondDevices.size(); i < size; i++) {
            BTDeviceBean bean = new BTDeviceBean(unbondDevices.get(i), false);
            allDevices.add(bean);
        }
        unbondDevices.clear();
        if (null != allDevices) {
            final BTDeviceAdapter adapter = new BTDeviceAdapter(allDevices, context);
            devicesListView.setAdapter(adapter);
            devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    BTDeviceBean bt = allDevices.get(i);
                    if (bt.isBonded()) {
                        String address = bt.getDevice().getAddress();
                        SpUtils.put(Constants.BT_ADDRESS, address);
                        ViewUtils.showToast(context, "已选择" + bt.getDevice().getName() + "设备");
                        Intent data = new Intent();
                        data.putExtra(Constants.BT_ADDRESS, address);
                        context.setResult(Activity.RESULT_OK, data);
                        context.finish();
                    } else {
                        try {
                            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                            createBondMethod.invoke(allDevices.get(i).getDevice());
                        } catch (Exception e) {
                            ViewUtils.showToast(context, "配对失败！");
                        }
                    }
                }
            });
        }
    }
}
