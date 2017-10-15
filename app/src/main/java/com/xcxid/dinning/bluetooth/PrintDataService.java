package com.xcxid.dinning.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.text.TextUtils;

import com.xcxid.dinning.XcxidApplication;
import com.xcxid.dinning.url.Constants;
import com.xcxid.dinning.utils.LogUtils;
import com.xcxid.dinning.utils.SpUtils;
import com.xcxid.dinning.utils.ViewUtils;

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
//    final String[] items = {"复位打印机", "标准ASCII字体", "压缩ASCII字体", "字体不放大", "宽高加倍", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印",
//            "取消黑白反显", "选择黑白反显", "取消顺时针旋转90°", "选择顺时针旋转90°"};
//    final byte[][] byteCommands = {{0x1b, 0x40}, // 复位打印机
//            {0x1b, 0x4d, 0x00}, // 标准ASCII字体
//            {0x1b, 0x4d, 0x01}, // 压缩ASCII字体
//            {0x1d, 0x21, 0x00}, // 字体不放大
//            {0x1d, 0x21, 0x11}, // 宽高加倍
//            {0x1b, 0x45, 0x00}, // 取消加粗模式
//            {0x1b, 0x45, 0x01}, // 选择加粗模式
//            {0x1b, 0x7b, 0x00}, // 取消倒置打印
//            {0x1b, 0x7b, 0x01}, // 选择倒置打印
//            {0x1d, 0x42, 0x00}, // 取消黑白反显
//            {0x1d, 0x42, 0x01}, // 选择黑白反显
//            {0x1b, 0x56, 0x00}, // 取消顺时针旋转90°
//            {0x1b, 0x56, 0x01},// 选择顺时针旋转90°
//    };

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
            device = bluetoothAdapter.getRemoteDevice(btAddress);
            if (connect()) {
                isConnection = true;
            }
        }
    }
    /**
     * 获取设备名称
     *
     * @return String
     */
    public static String getDeviceName() {
        return device.getName();
    }

    public static boolean isConnection() {
        return isConnection;
    }

    /**
     * 连接蓝牙设备
     */
    public static boolean connect() {
        if (!isConnection) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.isDiscovering();
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return true;
        }
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
        } catch (IOException e) {
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
                ViewUtils.showToast(XcxidApplication.getInstance(), "发送失败");
            }
        } else {
            ViewUtils.showToast(XcxidApplication.getInstance(), "设备未连接，请重新连接！");
        }
    }

}
