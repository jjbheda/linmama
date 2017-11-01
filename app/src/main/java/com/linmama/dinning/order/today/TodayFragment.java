package com.linmama.dinning.order.today;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.header.WindmillHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class TodayFragment extends BasePresenterFragment<TodayOrderPresenter> implements
        TodayOrderContract.TodayOrderView,TodayOrderContract.PrintView,TakingOrderAdapter.IPosOrder,
        AdapterView.OnItemClickListener, GetMoreListView.OnGetMoreListener{
    @BindView(R.id.lvNewOrder)
    GetMoreListView mLvTakingOrder;
    @BindView(R.id.ptr_new)
    PtrClassicFrameLayout mPtrTaking;

    private TakingOrderAdapter mAdapter;
    private MyAlertDialog mAlert;
    private int selectPosition = -1;
    private ResultsBean mPrintingBean = null;
    private List<TakingOrderBean> mResults;
    private int currentPage = 1;
    private static final int REQUEST_TAKE_ORDER_DETAIL = 0x20;

    @Override
    protected TodayOrderPresenter loadPresenter() {
        return new TodayOrderPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_new;
    }

    @Override
    protected void initView() {
        mResults = new ArrayList<>();
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrTaking.setHeaderView(header);
        mPtrTaking.addPtrUIHandler(header);
    }

    @Override
    protected void initListener() {
        mLvTakingOrder.setOnGetMoreListener(this);
        mPtrTaking.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
//                    showDialog("加载中...");
                    currentPage = 1;
                    mPresenter.getTodayOrder(currentPage);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            mPtrTaking.autoRefresh(true);
//            showDialog("加载中...");
//            mPresenter.getTakingOrder();
        }
    }

    public void refresh() {
        if (null != mPresenter) {
            mPtrTaking.autoRefresh(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getTodayOrderSuccess(List<TakingOrderBean> resultBeans) {
        dismissDialog();
        if (currentPage == 1 && mPtrTaking.isRefreshing()) {
            mPtrTaking.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
//        if (TextUtils.isEmpty(takingOrderBean.getNext())) {
//            mLvTakingOrder.setNoMore();
//        } else {
//            mLvTakingOrder.setHasMore();
//        }
        if (null != resultBeans && null != resultBeans) {
            LogUtils.d("getTakingOrderSuccess", resultBeans.toString());
            List<TakingOrderBean> results = resultBeans;
            mResults.addAll(results);
            if (null == mAdapter) {
                mAdapter = new TakingOrderAdapter(mActivity, mResults);
//            mAdapter.setCancelOrder(this);
                mLvTakingOrder.setAdapter(mAdapter);
                mLvTakingOrder.setOnItemClickListener(this);
            } else {
                mAdapter.notifyDataSetChanged();
                if (currentPage > 1) {
                    mLvTakingOrder.getMoreComplete();
                }
            }
        }
    }

    @Override
    public void getTodayOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrTaking, failMsg);
        }
    }

//    @Override
//    public void cancelWarnSuccess(DataBean bean, String orderId) {
//        dismissDialog();
//        ViewUtils.showSnack(mPtrTaking, "取消提醒");
//        AlarmManagerUtils.cancelAlarm(mActivity, AlarmManagerUtils.ALARM_ACTION, Integer.parseInt(orderId));
//        mPtrTaking.autoRefresh(true);
//    }
//
//    @Override
//    public void cancelWarnFail(String msg) {
//        dismissDialog();
//        if (!TextUtils.isEmpty(msg)) {
//            ViewUtils.showSnack(mPtrTaking, msg);
//        }
//    }


//    @Override
//    public void cancelRingOrder(int position) {
//        ResultsBean rb = null;
//        if (null != mAdapter.getItem(position)) {
//            rb = (ResultsBean) mAdapter.getItem(position);
//        }
//        List<OrderWarnsBean> warns = null;
//        if (rb != null) {
//            warns = rb.getOrderWarms();
//        }
//        if (null != warns && warns.size() > 0) {
//            showDialog("加载中...");
//            mPresenter.cancelWarn(String.valueOf(warns.get(0).getId()));
//        }
//    }


    @Override
    public void posOrder(final int position) {
        mAlert = new MyAlertDialog(mActivity).builder()
                .setMsg("是否打印小票")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PrintDataService.isConnection()) {
                            showDialog("请稍后...");
                            mPrintingBean = (ResultsBean) mAdapter.getItem(position);
                            mPresenter.getPrintData(mPrintingBean.getId());
                        } else {
                            ViewUtils.showSnack(mPtrTaking, "未连接票据打印机");
                        }
                    }
                });
        mAlert.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ResultsBean rb = (ResultsBean) mAdapter.getItem(i);
        Bundle data = new Bundle();
        data.putParcelable(Constants.ORDER_TAKE_DETAIL, rb);
//        ActivityUtils.startActivityForResult(this, TakingOrderDetailActivity.class, data, REQUEST_TAKE_ORDER_DETAIL);
    }

    @Override
    public void getPrintDataSuccess(OrderDetailBean bean) {
        final StringBuilder builder = new StringBuilder();
        BigDecimal bd = null;
        String fullname = (String) SpUtils.get(Constants.USER_FULLNAME, "");
        if (!TextUtils.isEmpty(fullname)) {
            builder.append("      ");
            builder.append(fullname);
            builder.append("\n");
        }
        if (null != mPrintingBean) {
            builder.append("      ");
            String payStatus = mPrintingBean.getPay_status();
            String payChannel = mPrintingBean.getPay_channel();
            if (payStatus.equals("1")) {
                builder.append("未支付");
            } else if (payStatus.equals("2")) {
                if (payChannel.equals("1")) {
                    builder.append("已在线支付");
                } else if (payChannel.equals("2")) {
                    builder.append("已吧台支付");
                }
            }
            String diningWay = mPrintingBean.getDining_way();
            if (diningWay.equals("1")) {
                builder.append("(堂食)");
            } else if (diningWay.equals("2")) {
                builder.append("(外带)");
            }
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
            String serialNumber = mPrintingBean.getSerial_number();
            builder.append("  NO:");
            builder.append(serialNumber);
            builder.append("\n");
            String deskNum = mPrintingBean.getDesk_num();
            builder.append("桌号:");
            builder.append(deskNum);
            builder.append("    ");
            int diningNum = mPrintingBean.getDine_num();
            builder.append("人数:");
            builder.append(diningNum);
            builder.append("\n");
            String orderDatetimeBj = mPrintingBean.getOrder_datetime_bj();
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
        if (null != mPrintingBean) {
            String remark = mPrintingBean.getRemark();
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
        if (PrintDataService.isConnection()) {
            PrintDataService.send(builder.toString());
            dismissDialog();
        } else {
            dismissDialog();
        }
    }

    @Override
    public void getPrintDataFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrTaking, failMsg);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_ORDER_DETAIL && resultCode == Activity.RESULT_OK && null != data) {
            String orderId = data.getStringExtra(Constants.ORDER_ID);
            for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                ResultsBean rb = (ResultsBean) mAdapter.getItem(i);
                if (String.valueOf(rb.getId()).equals(orderId)) {
                    rb.setPay_status("2");
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public void onGetMore() {
        currentPage++;
        mPresenter.getTodayOrder(currentPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentPage = 1;
        mAdapter = null;
    }
}
