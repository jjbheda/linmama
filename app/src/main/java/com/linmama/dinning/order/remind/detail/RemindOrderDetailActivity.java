package com.linmama.dinning.order.remind.detail;

import android.content.Intent;
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
import com.linmama.dinning.bean.RemindResultsBean;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.QuitOrderRefuseItem;
import com.linmama.dinning.R;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.url.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/13
 */

public class RemindOrderDetailActivity extends BasePresenterActivity<RemindOrderDetailPresenter> implements
        RemindDetailContract.RemindDetailView, RemindDetailContract.HandleWarnView {
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.detailTitle)
    Toolbar toolbar;
    @BindView((R.id.tvRemindTime))
    TextView tvRemindTime;
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
    @BindView(R.id.llOrderItem)
    LinearLayout llOrderItem;
    @BindView(R.id.btnHanle)
    Button btnHanle;

    private int warnId;

    @Override
    protected RemindOrderDetailPresenter loadPresenter() {
        return new RemindOrderDetailPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_remindorder_detail;
    }

    @Override
    protected void initView() {
        Bundle data = getIntent().getExtras();
        if (null != data && null != data.getParcelable(Constants.ORDER_REMIND_DETAIL)) {
            RemindResultsBean rrb = data.getParcelable(Constants.ORDER_REMIND_DETAIL);
            ResultsBean rb = null;
            if (rrb != null) {
                warnId = rrb.getId();
                String warnType = rrb.getWarn_type();
                String warnTime = rrb.getWarn_time();
                rb = rrb.getOrder();
                if (warnType.equals("2")) {
                    if (null != rb) {
                        tvRemindTime.setText(String.format(getResources().getString(R.string.remind_order_remind_time), rb.getIn_store_time()));
                        tvRemindTime.setBackgroundResource(R.drawable.login_button_enabled_bg);
                    }
                } else if (warnType.equals("1")) {
                    tvRemindTime.setText(String.format(getResources().getString(R.string.remind_order_remind), warnTime));
                    tvRemindTime.setBackgroundResource(R.drawable.btn_ok_bg);
                }
            }
            if (null != rb) {
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
                    tvDetailOther.setVisibility(View.VISIBLE);
                    String warnTime = rrb.getWarn_time();
                    tvDetailOther.setText(String.format(getResources().getString(R.string.taking_order_remind), warnTime));
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
                int orderId = rb.getId();
                showDialog("加载中...");
                mPresenter.getRemindDetail(orderId);
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

    @OnClick(R.id.btnHanle)
    public void handleWarn(View view) {
        showDialog("加载中...");
        mPresenter.handleWarn(String.valueOf(warnId));
    }

    @Override
    public void getRemindDetailSuccess(OrderDetailBean bean) {
        dismissDialog();
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
    public void getRemindDetailFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void handleWarnSuccess(String warn_id) {
        dismissDialog();
        ViewUtils.showSnack(content, "取消提醒");
        setResult(warn_id);
    }

    @Override
    public void handleWarnFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    private void setResult(String warnId) {
        Intent data = new Intent();
        data.putExtra(Constants.WARN_ID, warnId);
        setResult(RESULT_OK, data);
        finish();
    }
}
