package com.linmama.dinning.bluetooth;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BaseActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/13
 */

public class CheckBTActivity extends BaseActivity {
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView status;
    @BindView(R.id.rightIcon)
    ImageView statusIcon;
    @BindView(R.id.btList)
    ListView btList;
    @BindView(R.id.search)
    Button search;
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

    private BTService btService;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_printer;
    }

    @Override
    protected void initView() {
        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        etNum.setText(String.valueOf(printNum));
        btService = new BTService(this, btList, statusIcon, status, search);
        if (btService.isOpen()) {
            statusIcon.setImageResource(R.mipmap.icon_on);
            status.setText("关闭蓝牙");
        } else {
            statusIcon.setImageResource(R.mipmap.icon_off);
            status.setText("打开蓝牙");
        }
        int num = Integer.parseInt(etNum.getText().toString());
        etNum.setText(String.valueOf(num));
        if (PrintDataService.isConnection()) {
            rlConnectBT.setVisibility(View.VISIBLE);
            btConnectName.setText(PrintDataService.getDeviceName());
        } else {
            rlConnectBT.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.search)
    public void searchDevices(View view) {
        if (btService.isOpen()) {
            btService.searchDevices();
        }
    }

    @OnClick(R.id.rightIcon)
    public void switchBT(View view) {
        if (!btService.isOpen()) {
            btService.openBluetooth(this);
            statusIcon.setImageResource(R.mipmap.icon_on);
        } else {
            btService.closeBluetooth();
            statusIcon.setImageResource(R.mipmap.icon_off);
            rlConnectBT.setVisibility(View.GONE);
            if (PrintDataService.isConnection()) {
                PrintDataService.disconnect();
            }
        }
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
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btService.unregisterReceiver();
    }

}
