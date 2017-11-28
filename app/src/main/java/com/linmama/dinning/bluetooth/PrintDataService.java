package com.linmama.dinning.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.text.TextUtils;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.PrintUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.ViewUtils;

import net.posprinter.posprinterface.UiExecute;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class PrintDataService {
    private static PrintDataService mInstance;
    private static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static BluetoothDevice device = null;
    private static BluetoothSocket bluetoothSocket = null;
    private static OutputStream outputStream = null;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static boolean isConnection = false;

    public static PrintDataService getInstance() {
        if (mInstance == null) {
            synchronized (PrintDataService.class) {
                if (mInstance == null) {
                    mInstance = new PrintDataService();
                    init();
                }
            }
        }
        return mInstance;
    }

    public static void init() {
        String btAddress = (String) SpUtils.get(Constants.BT_ADDRESS, "");
        if (!TextUtils.isEmpty(btAddress)) {
            device = bluetoothAdapter.getRemoteDevice(btAddress);       //
            if (!isConnection) {
                try {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                    bluetoothSocket.connect();
                    outputStream = bluetoothSocket.getOutputStream();
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.isDiscovering();
                    }
                } catch (Exception e) {
                }
            }
        }
    }
    /**
     * 获取设备名称
     *
     * @return String
     */
    public static String getDeviceName() {
        if (device!=null)
            return device.getName();
        else
            return "";
    }

    public interface ConnectCallback{
        void connectSucess();
        void connectFailed();
    }

    public boolean isConnection() {
        return isConnection;
    }

    /**
     * 连接蓝牙设备
     */
    public static void connect(final ConnectCallback callback) {
            String btAddress = (String) SpUtils.get(Constants.BT_ADDRESS, "");
            MainActivity.binder.connectBtPort(btAddress, new UiExecute() {
                @Override
                public void onsucess() {
                    isConnection = true;
                    callback.connectSucess();
                }

                @Override
                public void onfailed() {
                    isConnection = false;
                    callback.connectFailed();
                }
            });
    }

    /**
     * 断开蓝牙设备连接
     */
    public static void disconnect() {
        try {
            bluetoothSocket.close();
            outputStream.close();
            bluetoothSocket = null;
            outputStream = null;
            isConnection = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public static void send(String sendData) {
        if (isConnection) {
            LogUtils.d("print", "print send");
            try {
                byte[] data = sendData.getBytes("gbk");
                outputStream.write(data, 0, data.length);
                outputStream.flush();
            } catch (IOException e) {
                ViewUtils.showToast(LmamaApplication.getInstance(), "发送失败");
            }
        } else {
            ViewUtils.showToast(LmamaApplication.getInstance(), "设备未连接，请重新连接！");
        }
    }

}
