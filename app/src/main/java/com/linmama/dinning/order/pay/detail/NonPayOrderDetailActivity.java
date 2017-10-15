package com.linmama.dinning.order.pay.detail;

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

import com.linmama.dinning.bean.CancelBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.QuitOrderRefuseItem;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/13
 */

public class NonPayOrderDetailActivity extends BasePresenterActivity<NonPayOrderDetailPresenter> implements
        NonPayDetailContract.NonPayDetailView, NonPayDetailContract.CancelOrderView,
        NonPayDetailContract.ConfirmOrderView, MyAlertDialog.ICallBack {
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
    @BindView(R.id.tvDetailOther)
    TextView tvDetailOther;
    @BindView(R.id.tvDetailAmount)
    TextView tvDetailAmount;
    @BindView(R.id.tvDetailPayStatus)
    TextView tvDetailPayStatus;
    @BindView(R.id.llOrderItem)
    LinearLayout llOrderItem;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnTake)
    Button btnTake;

    private int orderId;
    private MyAlertDialog mAlert;

    @Override
    protected NonPayOrderDetailPresenter loadPresenter() {
        return new NonPayOrderDetailPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_nonpayorder_detail;
    }

    @Override
    protected void initView() {
        Bundle data = getIntent().getExtras();
        if (null != data && null != data.getParcelable(Constants.ORDER_NONPAY_DETAIL)) {
            ResultsBean rb = data.getParcelable(Constants.ORDER_NONPAY_DETAIL);
            if (null != rb) {
                boolean isInStore = rb.is_in_store();
                String deskNum = rb.getDesk_num();
                String diningWay = rb.getDining_way();
                String payChannel = rb.getPay_channel();
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
                }
                tvDetailOther.setVisibility(View.VISIBLE);
                if (payChannel.equals("1")) {
                    tvDetailOther.setText(String.format(getResources().getString(R.string.new_order_pay_way), "在线支付"));
                } else if (payChannel.equals("2")) {
                    tvDetailOther.setText(String.format(getResources().getString(R.string.new_order_pay_way), "吧台支付"));
                }
                tvDetailPerson.setText(String.format(getResources().getString(R.string.new_order_person), rb.getDine_num()));
                if (diningWay.equals("1")) {
                    tvDetailWay.setText("(堂食)");
                } else if (diningWay.equals("2")) {
                    tvDetailWay.setText("(外带)");
                }
                tvDetailSerial.setText(String.format(getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
                tvDetailDatetime.setText(String.format(getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
                tvDetailAmount.setText(String.format(getResources().getString(R.string.order_money), rb.getTotal_amount()));
                orderId = rb.getId();
                mPresenter.getNonPayDetail(orderId);
            }
        }
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
        mAlert = new MyAlertDialog(this).builder()
                .setTitle("请输入操作密码")
                .setEditHint("操作密码")
                .setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setNegativeButton("取消", null)
                .setConfirmButton("确定", NonPayOrderDetailActivity.this);
        mAlert.show();
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

    @Override
    public void getNonPayDetailSuccess(OrderDetailBean bean) {
        if (null != bean && null != bean.getOrderItems()) {
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
    public void getNonPayDetailFail(String failMsg) {
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
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

    @Override
    public void cancelOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void confirmOrderSuccess(String orderId) {
        dismissDialog();
        ViewUtils.showSnack(content, "确认支付");
        setResult(orderId);
    }

    @Override
    public void confirmOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    private void setResult(String orderId) {
        Intent data = new Intent();
        data.putExtra(Constants.ORDER_ID, orderId);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onEditText(String text) {
        mAlert.dismiss();
        mAlert = null;
        showDialog("加载中...");
        mPresenter.confirmOrder(String.valueOf(orderId), text);
    }
}
