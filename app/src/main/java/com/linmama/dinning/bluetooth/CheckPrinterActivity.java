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

    @Override
    protected void initView() {
        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        etNum.setText(String.valueOf(printNum));
//        btService = new BTService(this, btList, statusIcon, status, search);
//        if (btService.isOpen()) {
//            statusIcon.setImageResource(R.mipmap.icon_on);
//            status.setText("关闭蓝牙");
//        } else {
//            statusIcon.setImageResource(R.mipmap.icon_off);
//            status.setText("打开蓝牙");
//        }
        int num = Integer.parseInt(etNum.getText().toString());
        etNum.setText(String.valueOf(num));
        String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
          if (!sn.equals("")) {
              showDialog("","检测中...");
              boolean isConnect = false;
              if (FeiEPrinterUtils.queryPrinterStatus()) {
                  isConnect = true;
//                    Toast.makeText(MainActivity.this, "票据打印机连接成功", Toast.LENGTH_SHORT).show();
              }
              Message msg = new Message();
              Bundle data = new Bundle();
              data.putBoolean("IS_CONNECT",isConnect);
              data.putString("SN",sn);
              msg.setData(data);
              updateHandler.sendMessage(msg);
              dismissDialog();
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
        Handler handler = new Handler(CheckPrinterActivity.this.getMainLooper());
        //延迟一秒后进行
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "请求打印机接口");
                String sn = (String) SpUtils.get(Constants.PRINT_DEVEICES_SELECTED, "");
                boolean isConnect = false;
                if (FeiEPrinterUtils.queryPrinterStatus()) {
                    isConnect = true;
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("IS_CONNECT",isConnect);
                data.putString("SN",printSn);
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
//        btList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                       final PrintBean bean = (PrintBean)parent.getItemAtPosition(position);
//                        CheckPrinterActivity.this.showDialog("","检测中");
//                        HandlerThread thread = new HandlerThread("NetWork2");
//                        thread.start();
//                        Handler handler = new Handler(thread.getLooper());
//                        //延迟一秒后进行
//                        handler.postDelayed(new Runnable() {
//                            public void run() {
//                                Log.d(TAG, "请求打印机接口");
//                                if (FeiEPrinterUtils.queryPrinterStatus()) {
//                                    bean.conStatus = true;
//                                    SpUtils.put(Constants.PRINT_DEVEICES_SELECTED, bean.printSn);
//                                } else {
//                                    Log.d(TAG, "票据打印机连接失败");
//                                    bean.conStatus = false;
//                                }
//
//                                new Handler(getMainLooper()).postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (adapter != null) {
//                                            adapter.notifyDataSetChanged();
//                                        }
//                                    }
//                                }, 10);
//
//                                CheckPrinterActivity.this.dismissDialog();
//                            }
//                        },100);
//
//            }
//        });


//        btList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                final PrintBean bean = (PrintBean) parent.getItemAtPosition(position);
//                MyAlertDialog mAlert = new MyAlertDialog(CheckPrinterActivity.this).builder()
//                        .setTitle("是否删除")
//                        .setConfirmButton("是", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (arrayList.contains(bean)) {
//                                    arrayList.remove(bean);
//                                }
//                               if (adapter != null){
//                                   adapter.notifyDataSetChanged();
//                               }
//
//                                try {
//                                    ArrayList<PrintBean> arrayList = SpUtils.getObject(Constants.PRINT_DEVEICES);
//                                    if (arrayList == null){
//                                        arrayList = new ArrayList<>();
//                                    } else {
//                                        arrayList.remove(bean);
//                                    }
//                                    SpUtils.setObject(Constants.PRINT_DEVEICES,arrayList);
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }).setPositiveButton("否", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        });
//                mAlert.show();
//                return true;
//            }
//        });

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
//        btService.unregisterReceiver();
    }

}
