package com.xcxid.dinning.order.neu.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.base.BasePresenterActivity;
import com.xcxid.dinning.bean.CancelBean;
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.bean.OrderItemsBean;
import com.xcxid.dinning.bean.ResultsBean;
import com.xcxid.dinning.bluetooth.PrintDataService;
import com.xcxid.dinning.order.neu.timepick.TimePickerActivity;
import com.xcxid.dinning.url.Constants;
import com.xcxid.dinning.utils.ActivityUtils;
import com.xcxid.dinning.utils.SpUtils;
import com.xcxid.dinning.utils.ViewUtils;
import com.xcxid.dinning.widget.MyAlertDialog;
import com.xcxid.dinning.widget.QuitOrderRefuseItem;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/13
 */

public class NewOrderDetailActivity extends BasePresenterActivity<NewOrderDetailPresenter> implements
        NewDetailContract.NewDetailView, NewDetailContract.ReceiveOrderView, NewDetailContract.CancelOrderView,
        NewDetailContract.OKOrderView, MyAlertDialog.ICallBack {
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.detailTitle)
    Toolbar toolbar;
    @BindView(R.id.ivDetailIcon)
    ImageView ivDetailIcon;
    @BindView(R.id.tvDetailDesk)
    TextView tvDetailDesk;
    @BindView(R.id.tvDetailPerson)
    TextView tvDetailPerson;
    @BindView(R.id.tvDetailWay)
    TextView tvDetailWay;
    @BindView(R.id.tvDetailSerial)
    TextView tvDetailSerial;
    @BindView(R.id.tvDetailDatetime)
    TextView tvDetailDatetime;
    @BindView(R.id.tvDetailPayChannel)
    TextView tvDetailPayChannel;
    @BindView(R.id.tvDetailOther)
    TextView tvDetailOther;
    @BindView(R.id.tvDetailAmount)
    TextView tvDetailAmount;
    @BindView(R.id.tvDetailPayStatus)
    TextView tvDetailPayStatus;
    @BindView(R.id.tvDetailRemark)
    TextView tvDetailRemark;
    @BindView(R.id.tvDetailInvoice)
    TextView tvDetailInvoice;
    @BindView(R.id.llOrderItem)
    LinearLayout llOrderItem;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnTake)
    Button btnTake;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.btnReceive)
    Button btnReceive;
    @BindView(R.id.btnQuit)
    Button btnQuit;
    @BindView(R.id.llNonPay)
    LinearLayout llNonPay;

    private int orderId;
    private boolean isPrinted = false;
    private boolean isAppointedOrder = false;
    private boolean isPayed = false;
    private MyAlertDialog mAlert;
    private ResultsBean resultsBean;
    private OrderDetailBean detailBean;
    private static final int REQUEST_ORDERDETAIL_APPOINT = 0x12;
    private static final int RESULT_ORDERDETAIL_APPOINT = 12;

    @Override
    protected NewOrderDetailPresenter loadPresenter() {
        return new NewOrderDetailPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_neworder_detail;
    }

    @Override
    protected void initView() {
        Bundle data = getIntent().getExtras();
        if (null != data && null != data.getParcelable(Constants.ORDER_NEW_DETAIL)) {
            resultsBean = data.getParcelable(Constants.ORDER_NEW_DETAIL);
            if (null != resultsBean) {
                boolean isInStore = resultsBean.is_in_store();
                String deskNum = resultsBean.getDesk_num();
                String diningWay = resultsBean.getDining_way();
                String payStatus = resultsBean.getPay_status();
                String payChannel = resultsBean.getPay_channel();
                String remark = resultsBean.getRemark();
                boolean isInvoice = resultsBean.is_invoice();
                if (payStatus.equals("1")) {
                    isPayed = false;
                    tvDetailPayStatus.setText("(未支付)");
                    tvDetailPayStatus.setTextColor(getResources().getColor(R.color.colorOrderAppoint));
                } else if (payStatus.equals("2")) {
                    isPayed = true;
                    tvDetailPayStatus.setText("(已支付)");
                    tvDetailPayStatus.setTextColor(getResources().getColor(R.color.colorOrderText));
                    llNonPay.setVisibility(View.INVISIBLE);
                    btnConfirm.setVisibility(View.INVISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnTake.setVisibility(View.VISIBLE);
                }
                if (isInStore && !TextUtils.isEmpty(deskNum)) {
                    ivDetailIcon.setImageResource(R.mipmap.icon_online);
                    tvDetailDesk.setText(deskNum);
                    tvDetailDesk.setTextColor(getResources().getColor(R.color.colorOrderDeskNum));
                } else if (isInStore && TextUtils.isEmpty(deskNum)) {
                    ivDetailIcon.setImageResource(R.mipmap.icon_fake);
                    tvDetailDesk.setText("来客单");
                    tvDetailDesk.setTextColor(getResources().getColor(R.color.colorOrderTake));
                } else if (!isInStore) {
                    isAppointedOrder = true;
                    ivDetailIcon.setImageResource(R.mipmap.icon_appoint);
                    tvDetailDesk.setText("预约单");
                    tvDetailDesk.setTextColor(getResources().getColor(R.color.colorOrderAppoint));
                    tvDetailOther.setVisibility(View.VISIBLE);
                    tvDetailOther.setText(String.format(getResources().getString(R.string.new_order_in_store_time), resultsBean.getIn_store_time()));
                }
                tvDetailPerson.setText(String.format(getResources().getString(R.string.new_order_person), resultsBean.getDine_num()));
                if (diningWay.equals("1")) {
                    tvDetailWay.setText("(堂食)");
                } else if (diningWay.equals("2")) {
                    tvDetailWay.setText("(外带)");
                }
                tvDetailSerial.setText(String.format(getResources().getString(R.string.new_order_serial), resultsBean.getSerial_number()));
                tvDetailDatetime.setText(String.format(getResources().getString(R.string.new_order_datetime), resultsBean.getOrder_datetime_bj()));
                if (payChannel.equals("1")) {
                    tvDetailPayChannel.setText(String.format(getResources().getString(R.string.new_order_pay_way), "在线支付"));
                } else if (payChannel.equals("2")) {
                    tvDetailPayChannel.setText(String.format(getResources().getString(R.string.new_order_pay_way), "吧台支付"));
                }
                tvDetailAmount.setText(String.format(getResources().getString(R.string.order_money), resultsBean.getTotal_amount()));
                if (!TextUtils.isEmpty(remark)) {
                    tvDetailRemark.setVisibility(View.VISIBLE);
                    tvDetailRemark.setText(String.format(getString(R.string.order_remark), remark));
                }
                if (isInvoice) {
                    tvDetailInvoice.setVisibility(View.VISIBLE);
                    tvDetailInvoice.setText(String.format(getString(R.string.order_invoice), resultsBean.getInvoice_title()));
                }
                orderId = resultsBean.getId();
                showDialog("加载中...");
                mPresenter.getNewDetail(orderId);
            }
        }
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btnCancel)
    public void cancelOrder(View view) {
        new MyAlertDialog(this).builder()
                .setTitle("确认取消该订单吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog("加载中...");
                        mPresenter.cancelOrder(String.valueOf(orderId), "本店暂无法处理此订单请谅解");
                    }
                }).show();
    }

    @OnClick(R.id.btnTake)
    public void takeOrder(View view) {
        if (isAppointedOrder) {
            moveToTimePicker();
        } else {
            mAlert = new MyAlertDialog(this).builder()
                    .setMsg("是否打印小票")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isPrinted = false;
                            mPresenter.receiveOrder(String.valueOf(orderId));
                        }
                    })
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (PrintDataService.isConnection()) {
                                isPrinted = true;
                                mPresenter.receiveOrder(String.valueOf(orderId));
                                printData(buildPrintData());
                            } else {
                                isPrinted = false;
                                mPresenter.receiveOrder(String.valueOf(orderId));
                                ViewUtils.showSnack(content, "未连接票据打印机");
                            }
                        }
                    });
            if (!isPayed) {
                new MyAlertDialog(this).builder()
                        .setMsg("请尽快核实客人是否已到店")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAlert.show();
                            }
                        }).show();
            } else {
                mAlert.show();
            }
        }
    }

    private void moveToTimePicker() {
        Bundle data = new Bundle();
        data.putParcelable(Constants.APPOINT_ORDER, resultsBean);
        data.putBoolean(Constants.NEW_DETAIL_APPOINT_ORDER, true);
        ActivityUtils.startActivityForResult(this, TimePickerActivity.class, data, REQUEST_ORDERDETAIL_APPOINT);
    }

    @OnClick(R.id.btnConfirm)
    public void confirmOrder(View view) {
        mAlert = new MyAlertDialog(this).builder()
                .setTitle("请输入操作密码")
                .setEditHint("操作密码")
                .setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setNegativeButton("取消", null)
                .setConfirmButton("确定", NewOrderDetailActivity.this);
        mAlert.show();
    }

    @OnClick(R.id.btnReceive)
    public void receiveOrder(View view) {
        if (isAppointedOrder) {
            moveToTimePicker();
        } else {
            mAlert = new MyAlertDialog(this).builder()
                    .setMsg("是否打印小票")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog("请稍候...");
                            isPrinted = false;
                            mPresenter.receiveOrder(String.valueOf(orderId));
                        }
                    })
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog("请稍候...");
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
            if (!isPayed) {
                new MyAlertDialog(this).builder()
                        .setMsg("请尽快核实客人是否已到店")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAlert.show();
                            }
                        }).show();
            } else {
                mAlert.show();
            }
        }
    }

    private void printData(String data) {
        mAlert.dismiss();
        mAlert = null;
        if (PrintDataService.isConnection()) {
            PrintDataService.send(data);
            dismissDialog();
        } else {
            dismissDialog();
        }
    }

    private String buildPrintData() {
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
        if (null != detailBean && detailBean.getOrderItems() != null) {
            List<OrderItemsBean> items = detailBean.getOrderItems();
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

        String printData = builder.toString();
        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
        if (printNum == 2) {
            builder.append(printData);
        } else if (printNum == 3) {
            builder.append(printData);
            builder.append(printData);
        }
        return builder.toString();
    }

    @OnClick(R.id.btnQuit)
    public void quitOrder(View view) {
        new MyAlertDialog(this).builder()
                .setTitle("确认取消该订单吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog("加载中...");
                        mPresenter.cancelOrder(String.valueOf(orderId), "本店暂无法处理此订单请谅解");
                    }
                }).show();
    }

    @Override
    public void getNewDetailSuccess(OrderDetailBean bean) {
        dismissDialog();
        if (null != bean && null != bean.getOrderItems()) {
            detailBean = bean;
            List<OrderItemsBean> orderItems = bean.getOrderItems();
            for (OrderItemsBean orderItem : orderItems) {
                QuitOrderRefuseItem item = new QuitOrderRefuseItem(this);
                item.setName(orderItem.getName());
                item.setNum(String.format(getString(R.string.quit_order_num), orderItem.getNum()));
                item.setAmount(String.format(getString(R.string.order_money), orderItem.getClosing_cost()));
                llOrderItem.addView(item);
            }
        }
    }

    @Override
    public void getNewDetailFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ORDERDETAIL_APPOINT && resultCode == RESULT_ORDERDETAIL_APPOINT && data != null) {
            String orderId = data.getStringExtra(Constants.ORDER_APPOINT_ID);
            Intent intent = new Intent();
            data.putExtra(Constants.ORDER_ID, orderId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void cancelOrderSuccess(String orderId, CancelBean bean) {
        dismissDialog();
        if (null != bean && !TextUtils.isEmpty(bean.getMsg())) {
            ViewUtils.showSnack(content, bean.getMsg());
        }
        setResult(orderId);
    }

    private void setResult(String orderId) {
        Intent data = new Intent();
        data.putExtra(Constants.ORDER_ID, orderId);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void cancelOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void okOrderSuccess(String orderId) {
        dismissDialog();
        ViewUtils.showSnack(content, "确认支付");
        setResult(orderId);
    }

    @Override
    public void okOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void receiveOrderSuccess(String orderId) {
        if (!isPrinted) {
            dismissDialog();
        } else {
            printData(buildPrintData());
        }
        ViewUtils.showSnack(content, "接单成功");
        Intent data = new Intent();
        data.putExtra(Constants.ORDER_ID, orderId);
        data.putExtra(Constants.BOOLEAN_ORDER_TAKE, true);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void receiveOrderFail(String failMsg) {
        if (!isPrinted) {
            dismissDialog();
        }
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void onEditText(String text) {
        mAlert.dismiss();
        mAlert = null;
        showDialog("加载中...");
        mPresenter.okOrder(String.valueOf(orderId), text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultsBean = null;
        detailBean = null;
    }
}
