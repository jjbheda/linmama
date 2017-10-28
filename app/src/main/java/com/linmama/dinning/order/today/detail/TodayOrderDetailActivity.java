package com.linmama.dinning.order.today.detail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.QuitOrderBean;
import com.linmama.dinning.bean.QuitOrderInfoBean;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.QuitOrderRefundItem;

import java.util.List;

import butterknife.BindView;

/**
 * Created by jingkang on 2017/3/13
 */

public class TodayOrderDetailActivity extends BasePresenterActivity<TodayOrderDetailPresenter> implements
        TodayDetailContract.QuitDetailView, TodayDetailContract.RefundView,
        TodayDetailContract.RefuseRefundView, MyAlertDialog.ICallBack {
    @BindView(R.id.content)
    RelativeLayout content;
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
    @BindView(R.id.llOrderItem)
    LinearLayout llOrderItem;

    private MyAlertDialog mAlert;
    private int mRedundId;
    private boolean isRefunded;

    @Override
    protected TodayOrderDetailPresenter loadPresenter() {
        return new TodayOrderDetailPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_quitorder_detail;
    }

    @Override
    protected void initView() {
        Bundle data = getIntent().getExtras();
        if (null != data && null != data.getParcelable(Constants.ORDER_QUIT_DETAIL)) {
            QuitOrderBean qrb = data.getParcelable(Constants.ORDER_QUIT_DETAIL);
            QuitOrderInfoBean rb = null;
            if (qrb != null) {
                rb = qrb.getOrder_info();
            }
            if (rb != null) {
                boolean isInStore = rb.is_in_store();
                String deskNum = rb.getDesk_num();
                String diningWay = rb.getDining_way();
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
                tvDetailPerson.setText(String.format(getResources().getString(R.string.new_order_person), rb.getDine_num()));
                if (diningWay.equals("1")) {
                    tvDetailWay.setText("(堂食)");
                } else if (diningWay.equals("2")) {
                    tvDetailWay.setText("(外带)");
                }
                tvDetailSerial.setText(String.format(getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
                tvDetailDatetime.setText(String.format(getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
                String payChannel = rb.getPay_channel();
                if (payChannel.equals("1")) {
                    tvDetailPayChannel.setText(String.format(getResources().getString(R.string.new_order_pay_way), "在线支付"));
                } else if (payChannel.equals("2")) {
                    tvDetailPayChannel.setText(String.format(getResources().getString(R.string.new_order_pay_way), "吧台支付"));
                }
                showDialog("加载中...");
                mPresenter.getQuitDetail(rb.getId());
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

    @Override
    public void refundSuccess(DataBean bean) {
        dismissDialog();
        ViewUtils.showSnack(content, "退款成功");
        setResult(RESULT_OK);
    }

    @Override
    public void refundFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void refuseRefundSuccess(DataBean bean) {
        dismissDialog();
        ViewUtils.showSnack(content, "拒绝退款");
        setResult(RESULT_OK);
    }

    @Override
    public void refuseRefundFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void onEditText(String text) {
        mAlert.dismiss();
        mAlert = null;
        if (isRefunded) {
            showDialog("加载中...");
            mPresenter.refund(String.valueOf(mRedundId), text);
        } else {
            showDialog("加载中...");
            mPresenter.refuseRefund(String.valueOf(mRedundId), text);
        }
    }

    private class OrderItemListener implements View.OnClickListener {
        private int redundId;
        private boolean isrefunded;

        public OrderItemListener(int redundId, boolean isRefunded) {
            this.redundId = redundId;
            this.isrefunded = isRefunded;
        }

        @Override
        public void onClick(View view) {
            if (isrefunded) {
                mRedundId = redundId;
                isRefunded = true;
                mAlert = new MyAlertDialog(TodayOrderDetailActivity.this).builder()
                        .setTitle("请输入操作密码")
                        .setEditHint("操作密码")
                        .setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .setNegativeButton("取消", null)
                        .setConfirmButton("确定", TodayOrderDetailActivity.this);
                mAlert.show();
            } else {
                mRedundId = redundId;
                isRefunded = false;
                mAlert = new MyAlertDialog(TodayOrderDetailActivity.this).builder()
                        .setTitle("请输入忽略理由")
                        .setEditHint("忽略理由")
                        .setNegativeButton("取消", null)
                        .setConfirmButton("确定", TodayOrderDetailActivity.this);
                mAlert.show();
            }
        }
    }

    @Override
    public void getQuitDetailSuccess(OrderDetailBean bean) {
        dismissDialog();
        if (null != bean && null != bean.getOrderItems()) {
            List<OrderItemsBean> orderItems = bean.getOrderItems();
            for (OrderItemsBean orderItem : orderItems) {
                QuitOrderRefundItem item = new QuitOrderRefundItem(this);
                item.setName(orderItem.getName());
                item.setNum(String.format(getString(R.string.quit_order_num), orderItem.getNum()));
                item.setAmount(String.format(getString(R.string.order_money), orderItem.getClosing_cost()));
                item.getIgnore().setOnClickListener(new OrderItemListener(orderItem.getId(), false));
                item.getRefund().setOnClickListener(new OrderItemListener(orderItem.getId(), true));
                llOrderItem.addView(item);
            }
        }
    }

    @Override
    public void getQuitDetailFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }
}
