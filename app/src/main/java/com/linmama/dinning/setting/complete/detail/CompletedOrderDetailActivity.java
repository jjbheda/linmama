package com.linmama.dinning.setting.complete.detail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.QuitOrderRefuseItem;
import com.linmama.dinning.R;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.widget.MyAlertDialog;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompletedOrderDetailActivity extends BasePresenterActivity<CompleteOrderDetailPresenter>
implements CompleteOrderDetailContract.CompleteDetailView{
    @BindView(R.id.ivDetailIcon)
    ImageView ivDetailIcon;
    @BindView(R.id.tvDetailDesk)
    TextView tvDetailDesk;
    @BindView(R.id.tvDetailPerson)
    TextView tvDetailPerson;
    @BindView(R.id.tvDetailWay)
    TextView tvDetailWay;
    @BindView(R.id.tvDetailType)
    TextView tvDetailType;
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
    @BindView(R.id.sepline)
    View sepline;
    @BindView(R.id.llOrderItem)
    LinearLayout llOrderItem;
    @BindView(R.id.btnPrint)
    Button btnPrint;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.detailTitle)
    Toolbar toolbar;

    private int orderId;
    private MyAlertDialog mAlert;
    private OrderDetailBean detailBean;
    private ResultsBean resultsBean;

    @Override
    protected CompleteOrderDetailPresenter loadPresenter() {
        return new CompleteOrderDetailPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_completeorder_detail;
    }

    @Override
    protected void initView() {
        Bundle data = getIntent().getExtras();
        if (null != data && null != data.getParcelable(Constants.ORDER_COMPLETE_DETAIL)) {
            resultsBean = data.getParcelable(Constants.ORDER_COMPLETE_DETAIL);
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
                } else if (payStatus.equals("2")) {
                    tvDetailPayStatus.setText("(已支付)");
                    tvDetailPayStatus.setTextColor(getResources().getColor(R.color.colorOrderText));
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
                mPresenter.getCompleteDetail(orderId);
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
    public void getCompleteDetailSuccess(OrderDetailBean bean) {
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
    public void getCompleteDetailFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailBean = null;
        resultsBean = null;
    }
}
