package com.linmama.dinning.order.neu.timepick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.AlarmManagerUtils;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.TimeUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.TimePicker;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/14
 */

public class TimePickerActivity extends BasePresenterActivity<TimePickerPresenter> implements
        TimePicker.ICallBack, TimePickerContract.TimePickerView, TimePickerContract.ReceiveOrderView,
        TimePickerContract.PrintView {

    @BindView(R.id.toolTitle)
    Toolbar toolbar;
    @BindView(R.id.content)
    RelativeLayout content;
    @BindView(R.id.tvInStore)
    TextView tvInStore;
    @BindView(R.id.tvInStoreTime)
    TextView tvInStoreTime;
    @BindView(R.id.btnSetWarnTime)
    Button btnSetWarnTime;
    @BindView(R.id.btnSkip)
    Button btnSkip;
    @BindView(R.id.btnOk)
    Button btnOk;

    private int orderId;
    private String warnTime = "";
    private MyAlertDialog mAlert;
    private boolean isSkiped = false;
    private boolean isPrinted = false;
    private boolean isAppointed = false;
    private ResultsBean resultsBean;
    private static final int RESULT_ORDERDETAIL_APPOINT = 12;

    @Override
    protected TimePickerPresenter loadPresenter() {
        return new TimePickerPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_set_warntime;
    }

    @Override
    protected void initView() {
        Bundle data = getIntent().getExtras();
        if (null != data && null != data.getParcelable(Constants.APPOINT_ORDER)) {
            resultsBean = data.getParcelable(Constants.APPOINT_ORDER);
            isAppointed = data.getBoolean(Constants.NEW_DETAIL_APPOINT_ORDER, false);
            if (resultsBean != null) {
                orderId = resultsBean.getId();
                String inStoreTime = resultsBean.getIn_store_time();
                tvInStoreTime.setText(inStoreTime);
            }
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
    protected void initData() {

    }


    @OnClick(R.id.btnSetWarnTime)
    public void selectWarnTime(View view) {
        String[] currentTime = TimeUtils.getCurrentTimeInString(TimeUtils.TIME_FROMAT).split(":");
        TimePicker tp;
        if (null != currentTime && currentTime.length == 2) {
            tp = new TimePicker(this, Integer.parseInt(currentTime[0]), Integer.parseInt(currentTime[1]));
        } else {
            tp = new TimePicker(this, 0, 0);
        }
        tp.showTimePicker(view);
        tp.setCallBack(this);
    }

    @OnClick(R.id.btnOk)
    public void setWarnTime(View view) {
        if (TextUtils.isEmpty(warnTime)) {
            ViewUtils.showSnack(content, "未设置提醒时间");
            return;
        }
        isSkiped = false;
//        String time = getCurrentTimeInString(TimeUtils.DATE_FROMAT) + warnTime;
        showDialog("加载中...");
        mPresenter.setWarn(String.valueOf(orderId), warnTime, "2");
//        mAlert = new MyAlertDialog(this).builder()
//                .setMsg("是否打印小票")
//                .setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        showDialog("请稍后...");
//                        isPrinted = false;
//                        mPresenter.receiveOrder(String.valueOf(orderId));
//                    }
//                })
//                .setPositiveButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        showDialog("请稍后...");
//                        if (null != printDataAction && printDataAction.isConnected()) {
//                            isPrinted = true;
//                            mPresenter.receiveOrder(String.valueOf(orderId));
//                            mPresenter.getPrintData(orderId);
//                        } else {
//                            isPrinted = false;
//                            mPresenter.receiveOrder(String.valueOf(orderId));
//                            ViewUtils.showSnack(content, "未连接票据打印机");
//                        }
//                    }
//                });
//        mAlert.show();
    }

    @OnClick(R.id.btnSkip)
    public void skipSetWarnTime(View view) {
        isPrinted = false;
        isSkiped = true;
        showDialog("加载中...");
        mPresenter.receiveOrder(String.valueOf(orderId));
    }

    @Override
    public void pickTime(int d, String h, String m) {
        if (d == 0) {
            warnTime = TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FROMAT);
        } else if (d == 1) {
            warnTime = TimeUtils.getTomorowTimeInString() + " ";
        }
        LogUtils.d("pickTime", "h:" + h + ",m:" + m);
        warnTime += h + m;
        LogUtils.d("pickTime", warnTime);
        btnSetWarnTime.setText(String.format(getResources().getString(R.string.order_order_time_set), warnTime));
    }

    @Override
    public String getWarnTime() {
        return warnTime;
    }

    public boolean checkNull() {
        if (!TextUtils.isEmpty(getWarnTime())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void timePickerSuccess(DataBean bean) {
        dismissDialog();
        ViewUtils.showSnack(content, "设置备菜提醒成功");
        boolean isVoiceWarn = (boolean) SpUtils.get(Constants.VOICE_WARN, true);
        if (isVoiceWarn && null != resultsBean) {
            String[] dates = warnTime.split(" ");
            AlarmManagerUtils.setAlarm(this, 1, dates[0], dates[1], resultsBean.getId(), 0, "订单" + resultsBean.getSerial_number() + "备菜提醒", 0);
        }
        mAlert = new MyAlertDialog(this).builder()
                .setMsg("是否打印小票")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog("请稍后...");
                        isPrinted = false;
                        mPresenter.receiveOrder(String.valueOf(orderId));
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog("请稍后...");
                        if (PrintDataService.isConnection()) {
                            isPrinted = true;
                            mPresenter.receiveOrder(String.valueOf(orderId));
                        } else {
                            isPrinted = false;
                            mPresenter.receiveOrder(String.valueOf(orderId));
                            ViewUtils.showSnack(content, "未连接票据打印机");
                        }
                    }
                });
        mAlert.show();
    }

    @Override
    public void timePickerFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void receiveOrderSuccess(String order_id) {
        ViewUtils.showSnack(content, "接单成功");
        if (isSkiped) {
            dismissDialog();
            MyAlertDialog alert = new MyAlertDialog(this).builder()
                    .setTitle("接单成功")
                    .setMsg("未设置备菜提醒\n请自行注意备菜时间!")
                    .setPositiveButton("确定", printListener);
            alert.show();
        } else if (!isSkiped && isPrinted) {
            mPresenter.getPrintData(orderId);
        } else if (!isSkiped && !isPrinted) {
            dismissDialog();
            setResult();
            finish();
        }
    }

    private View.OnClickListener printListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAlert = new MyAlertDialog(TimePickerActivity.this).builder()
                    .setMsg("是否打印小票")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setResult();
                            TimePickerActivity.this.finish();
                        }
                    })
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (PrintDataService.isConnection()) {
                                showDialog("请稍候...");
                                isPrinted = true;
                                mPresenter.getPrintData(orderId);
                            } else {
                                ViewUtils.showSnack(content, "未连接票据打印机");
                            }
                        }
                    });
            mAlert.show();
        }
    };

    @Override
    public void receiveOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void getPrintDataSuccess(OrderDetailBean bean) {
        mAlert.dismiss();
        mAlert = null;
        StringBuilder builder = new StringBuilder();
        BigDecimal bd = null;
        String fullname = (String) SpUtils.get(Constants.USER_FULLNAME, "");
        if (!TextUtils.isEmpty(fullname)) {
            builder.append("      ");
            builder.append(fullname);
            builder.append("\n");
        }
        if (null != resultsBean) {
            builder.append("      ");
            String payStatus = resultsBean.getPay_status();
            String payChannel = resultsBean.getPay_channel();
            if (payStatus.equals("1")) {
                builder.append("未支付");
            } else if (payStatus.equals("2")) {
                if (payChannel.equals("1")) {
                    builder.append("已在线支付");
                } else if (payChannel.equals("2")) {
                    builder.append("已吧台支付");
                }
            }
            String diningWay = resultsBean.getDining_way();
            if (diningWay.equals("1")) {
                builder.append("(堂食)");
            } else if (diningWay.equals("2")) {
                builder.append("(外带)");
            }
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
            String serialNumber = resultsBean.getSerial_number();
            builder.append("  NO:");
            builder.append(serialNumber);
            builder.append("\n");
            String deskNum = resultsBean.getDesk_num();
            builder.append("桌号:");
            builder.append(deskNum);
            builder.append("    ");
            int diningNum = resultsBean.getDine_num();
            builder.append("人数:");
            builder.append(diningNum);
            builder.append("\n");
            String orderDatetimeBj = resultsBean.getOrder_datetime_bj();
            builder.append("时间:");
            builder.append(orderDatetimeBj);
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
            builder.append("菜品");
            builder.append("      数量");
//            builder.append("  价格");
            builder.append("    金额");
            builder.append("\n");
            builder.append("\n");
        }
        if (null != bean && bean.getOrderItems() != null) {
            List<OrderItemsBean> items = bean.getOrderItems();
            for (OrderItemsBean item : items) {
                builder.append(item.getName());
                builder.append("    ");
                int num = item.getNum();
                builder.append(num);
                builder.append("    ");
                String cost = item.getClosing_cost();
                BigDecimal costBd = new BigDecimal(cost);
                if (num > 1) {
                    costBd = costBd.multiply(new BigDecimal(num));
                }
                if (null == bd) {
                    bd = new BigDecimal(0);
                    bd = bd.add(costBd);
                } else {
                    bd = bd.add(costBd);
                }
                builder.append(costBd.toString());
                builder.append("\n");
            }
            builder.append("---------------------------");
            builder.append("\n");
        }
        if (null != resultsBean) {
            String remark = resultsBean.getRemark();
            if (!TextUtils.isEmpty(remark)) {
                builder.append("备注:");
                builder.append(remark);
                builder.append("\n");
            }
        }
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("消费金额: ");
        if (bd != null) {
            builder.append(bd.toString());
        }
        builder.append("\n");
        builder.append("应收金额: ");
        if (bd != null) {
            builder.append(bd.toString());
        }
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("小不点点餐  www.xcxid.com");
        builder.append("\n");
        builder.append("      欢迎下次光临");
        builder.append("\n");
        builder.append("\n");
        if (PrintDataService.isConnection()) {
            String printData = builder.toString();
            int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
            if (printNum == 2) {
                builder.append(printData);
            } else if (printNum == 3) {
                builder.append(printData);
                builder.append(printData);
            }
            PrintDataService.send(builder.toString());
            dismissDialog();
        } else {
            dismissDialog();
        }
        setResult();
    }

    private void setResult() {
        if (!isAppointed) {
            Intent i = new Intent();
            i.putExtra(Constants.ORDER_ID, String.valueOf(orderId));
            setResult(RESULT_OK, i);
        } else {
            Intent i = new Intent();
            i.putExtra(Constants.ORDER_APPOINT_ID, String.valueOf(orderId));
            setResult(RESULT_ORDERDETAIL_APPOINT, i);
        }
    }

    @Override
    public void getPrintDataFail(String failMsg) {
        dismissDialog();
        mAlert.dismiss();
        mAlert = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultsBean = null;
    }

}
