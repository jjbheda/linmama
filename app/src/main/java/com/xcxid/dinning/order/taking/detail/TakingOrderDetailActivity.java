package com.xcxid.dinning.order.taking.detail;

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
import com.xcxid.dinning.bean.OrderDetailBean;
import com.xcxid.dinning.bean.OrderItemsBean;
import com.xcxid.dinning.bean.ResultsBean;
import com.xcxid.dinning.bluetooth.PrintDataService;
import com.xcxid.dinning.url.Constants;
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

public class TakingOrderDetailActivity extends BasePresenterActivity<TakingOrderDetailPresenter> implements
        TakingDetailContract.TakingDetailView, TakingDetailContract.ConfirmPayView,
        MyAlertDialog.ICallBack, TakingDetailContract.CompleteOrderView {
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
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.btnComplete)
    Button btnComplete;
//    @BindView(R.id.btnCancel)
//    Button btnCancel;
    @BindView(R.id.btnPrint)
    Button btnPrint;

    private int orderId;
//    private int warnId;
    private MyAlertDialog mAlert;
    private OrderDetailBean detailBean;
    private ResultsBean resultsBean;
    private boolean isPayed = false;

    @Override
    protected TakingOrderDetailPresenter loadPresenter() {
        return new TakingOrderDetailPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_takeorder_detail;
    }

    @Override
    protected void initView() {
        Bundle data = getIntent().getExtras();
        if (null != data && null != data.getParcelable(Constants.ORDER_TAKE_DETAIL)) {
            resultsBean = data.getParcelable(Constants.ORDER_TAKE_DETAIL);
            if (null != resultsBean) {
                boolean isInStore = resultsBean.is_in_store();
                String deskNum = resultsBean.getDesk_num();
                String diningWay = resultsBean.getDining_way();
                String payStatus = resultsBean.getPay_status();
                String remark = resultsBean.getRemark();
                String payChannel = resultsBean.getPay_channel();
                boolean isInvoice = resultsBean.is_invoice();
                if (payStatus.equals("1")) {
                    tvDetailPayStatus.setText("(未支付)");
                    tvDetailPayStatus.setTextColor(getResources().getColor(R.color.colorOrderAppoint));
                    btnConfirm.setVisibility(View.VISIBLE);
                    isPayed = false;
//                    btnCancel.setVisibility(View.INVISIBLE);
                } else if (payStatus.equals("2")) {
                    tvDetailPayStatus.setText("(已支付)");
                    tvDetailPayStatus.setTextColor(getResources().getColor(R.color.colorOrderText));
                    btnConfirm.setVisibility(View.GONE);
                    isPayed = true;
//                    btnCancel.setVisibility(View.INVISIBLE);
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
                    ivDetailIcon.setImageResource(R.mipmap.icon_appoint);
                    tvDetailDesk.setText("预约单");
                    tvDetailDesk.setTextColor(getResources().getColor(R.color.colorOrderAppoint));
                    tvDetailOther.setVisibility(View.VISIBLE);
                    tvDetailOther.setText(String.format(getResources().getString(R.string.new_order_in_store_time), resultsBean.getIn_store_time()));
//                    btnCancel.setVisibility(View.VISIBLE);
                }
                tvDetailPerson.setText(String.format(getResources().getString(R.string.new_order_person), resultsBean.getDine_num()));
                if (diningWay.equals("1")) {
                    tvDetailWay.setText("(堂食)");
                } else if (diningWay.equals("2")) {
                    tvDetailWay.setText("(外带)");
                }
                tvDetailSerial.setText(String.format(getResources().getString(R.string.new_order_serial), resultsBean.getSerial_number()));
                tvDetailDatetime.setText(String.format(getResources().getString(R.string.new_order_datetime), resultsBean.getOrder_datetime_bj()));
                if ("1".equals(payChannel)) {
                    tvDetailPayChannel.setText(String.format(getResources().getString(R.string.new_order_pay_way), "在线支付"));
                } else if ("2".equals(payChannel)) {
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
//                List<OrderWarnsBean> warns = resultsBean.getOrderWarms();
//                if (null == warns || warns.size() <= 0) {
//                    btnCancel.setVisibility(View.INVISIBLE);
//                } else {
//                    warnId = warns.get(0).getId();
//                }
                orderId = resultsBean.getId();
                showDialog("加载中...");
                mPresenter.getTakingDetail(orderId);
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

    @OnClick(R.id.btnComplete)
    public void completeOrder(View view) {
        if (!isPayed) {
            new MyAlertDialog(this).builder()
                    .setMsg("顾客未支付，您确定要完成订单吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog("加载中...");
                            mPresenter.completeOrder(String.valueOf(orderId));
                        }
                    }).show();
        } else {
            showDialog("加载中...");
            mPresenter.completeOrder(String.valueOf(orderId));
        }
    }

    @OnClick(R.id.btnConfirm)
    public void confirmOrder(View view) {
        mAlert = new MyAlertDialog(this).builder()
                .setTitle("请输入操作密码")
                .setEditHint("操作密码")
                .setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setNegativeButton("取消", null)
                .setConfirmButton("确定", TakingOrderDetailActivity.this);
        mAlert.show();
    }

//    @OnClick(btnCancel)
//    public void cancelOrder(View view) {
//        showDialog("加载中...");
//        mPresenter.cancelWarn(String.valueOf(warnId));
//    }

    @OnClick(R.id.btnPrint)
    public void print(View view) {
        mAlert = new MyAlertDialog(this).builder()
                .setMsg("是否打印小票")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PrintDataService.isConnection()) {
                            showDialog("请稍后...");
                            printData(buildPrintData());
                        } else {
                            ViewUtils.showSnack(content, "未连接票据打印机");
                        }
                    }
                });
        mAlert.show();
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
        if (null != resultsBean ) {
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

    @Override
    public void getTakingDetailSuccess(OrderDetailBean bean) {
        dismissDialog();
        if (bean.getPay_status().equals("2")) {
            tvDetailPayStatus.setText("(已支付)");
            tvDetailPayStatus.setTextColor(getResources().getColor(R.color.colorOrderText));
            btnConfirm.setVisibility(View.GONE);
        }
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
    public void getTakingDetailFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

//    @Override
//    public void cancelWarnSuccess(DataBean bean, String orderId) {
//        dismissDialog();
//        ViewUtils.showSnack(content, "取消提醒");
//        AlarmManagerUtils.cancelAlarm(this, AlarmManagerUtils.ALARM_ACTION, Integer.parseInt(orderId));
//        btnCancel.setVisibility(View.INVISIBLE);
//        setResult(orderId);
//    }
//
//    @Override
//    public void cancelWarnFail(String failMsg) {
//        dismissDialog();
//        if (!TextUtils.isEmpty(failMsg)) {
//            ViewUtils.showSnack(content, failMsg);
//        }
//    }

    @Override
    public void confirmPaySuccess(String orderId) {
        dismissDialog();
        ViewUtils.showSnack(content, "确认支付");
        setResult(orderId);
    }

    @Override
    public void confirmPayFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    private void setResult(String orderId) {
        Intent data = new Intent();
        data.putExtra(Constants.ORDER_ID, orderId);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onEditText(String text) {
        mAlert.dismiss();
        mAlert = null;
        showDialog("加载中...");
        mPresenter.confirmPayment(String.valueOf(orderId), text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailBean = null;
        resultsBean = null;
    }

    @Override
    public void completeOrderSuccess(String orderId) {
        dismissDialog();
        ViewUtils.showSnack(content, "完成订单");
        setResult(orderId);
        finish();
    }

    @Override
    public void completeOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }
}
