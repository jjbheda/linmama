package com.linmama.dinning.bluetooth;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BaseActivity;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.linmama.dinning.widget.ClearEditText;
import com.linmama.dinning.widget.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/13
 */

public class CheckPrinterActivity extends BaseActivity {
    public static String TAG = "CheckPrinterActivity";
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.etNum)
    TextView etNum;
    @BindView(R.id.btnReduce)
    Button btnReduce;
    @BindView(R.id.btConnectName)
    TextView btConnectName;
    @BindView(R.id.rlConnectBT)
    RelativeLayout rlConnectBT;
    @BindView(R.id.printer_sn)
    EditText snEt;

    @BindView(R.id.tv_connect_print_sn)
    TextView mConnectPrintTv;
    @BindView(R.id.connect_print_status_tv)
    TextView mConnectStatusTv;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_printer;
    }

    boolean isConnect;
    String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
    @Override
    protected void initView() {
        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        etNum.setText(String.valueOf(printNum));
        int num = Integer.parseInt(etNum.getText().toString());
        etNum.setText(String.valueOf(num));

          if (!sn.equals("")) {
              showDialog("","检测中...");
              isConnect = false;

              HandlerThread thread = new HandlerThread("checkNetWork");
              thread.start();
              Handler handler = new Handler(thread.getLooper());
              handler.postDelayed(new Runnable() {
                  public void run() {
                      Log.d(TAG, "请求打印机接口");
                      String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
                      if (FeiEPrinterUtils.queryPrinterStatus()) {
                          isConnect = true;
                      }

                      Message msg = new Message();
                      Bundle data = new Bundle();
                      data.putBoolean("IS_CONNECT",isConnect);
                      data.putString("SN",sn);
                      msg.setData(data);
                      updateHandler.sendMessage(msg);
                      dismissDialog();

                  }
              },100);

          }
    }

    //handler 处理返回的请求结果
    Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isConnect = data.getBoolean("IS_CONNECT");
            String sn = data.getString("SN","");
            SpUtils.put(Constants.PRINT_DEVEICES_SELECTED, sn);
            if (isConnect) {
                ViewUtils.showToast(CheckPrinterActivity.this, "已连接票据打印机");
                mConnectPrintTv.setText(sn);
                mConnectStatusTv.setText("已连接");
            } else {
                ViewUtils.showToast(CheckPrinterActivity.this, "票据打印机未连接");
                mConnectPrintTv.setText(sn);
                mConnectStatusTv.setText("未连接");
            }
        }
    };
    boolean isAddDeviceConnect;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @OnClick(R.id.add_print_btn)
    public void searchDevices(View view) {
        final String printSn = snEt.getText().toString().trim();
        String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
        if (!sn.equals("") && sn.equals(printSn)) {
            Toast.makeText(CheckPrinterActivity.this, "票据打印机已经存在", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog("","检测中...");
        isAddDeviceConnect = false;
        SpUtils.put(Constants.PRINT_DEVEICES_SELECTED, printSn);
        HandlerThread thread = new HandlerThread("checkNetWork");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "请求打印机接口");
                String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
                if (FeiEPrinterUtils.queryPrinterStatus()) {
                    isAddDeviceConnect = true;
                }

                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("IS_CONNECT",isAddDeviceConnect);
                data.putString("SN",sn);
                msg.setData(data);
                updateHandler.sendMessage(msg);
                dismissDialog();

            }
        },100);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @Override
    protected void initListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @OnClick(R.id.btnAdd)
    public void addNum(View view) {
        int num = Integer.parseInt(etNum.getText().toString());
        if (num >= 3) {
            SpUtils.put(Constants.PRINTER_NUM, 3);
            return;
        } else {
            SpUtils.put(Constants.PRINTER_NUM, num + 1);
            etNum.setText(String.valueOf(num + 1));
        }
    }

    @OnClick(R.id.btnReduce)
    public void reduceNum(View view) {
        int num = Integer.parseInt(etNum.getText().toString());
        if (num <= 1) {
            SpUtils.put(Constants.PRINTER_NUM, 1);
            return;
        } else {
            SpUtils.put(Constants.PRINTER_NUM, num - 1);
            etNum.setText(String.valueOf(num - 1));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
