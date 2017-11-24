package com.linmama.dinning.order.taking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteFragment;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.header.WindmillHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class TakingFragment extends BasePresenterFragment<TakingOrderPresenter> implements
        TakingOrderContract.TakingOrderView,
        MyAlertDialog.ICallBack,TakingOrderAdapter.ICompleteOrder, TakingOrderAdapter.ICancelOrder,TakingOrderAdapter.IPrintOrder,
        GetMoreListView.OnGetMoreListener,
    TakingOrderContract.CompleteOrderView {
    @BindView(R.id.lvNewOrder)
    GetMoreListView mLvTakingOrder;
    @BindView(R.id.ptr_new)
    PtrClassicFrameLayout mPtrTaking;

    private TakingOrderAdapter mAdapter;
    private MyAlertDialog mAlert;
    private int selectPosition = -1;
    private List<TakingOrderBean> mResults = new ArrayList<>();
    private int currentPage = 1;
    private int last_page = 1;
    private static final int REQUEST_TAKE_ORDER_DETAIL = 0x20;
    TakingOrderPresenter presenter;

    @Override
    protected TakingOrderPresenter loadPresenter() {
        presenter = new TakingOrderPresenter();
        return presenter;
    }

    public TakingOrderPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_new;
    }

    @Override
    protected void initView() {
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrTaking.setHeaderView(header);
        mPtrTaking.addPtrUIHandler(header);
        EventBus.getDefault().register(this);//订阅
    }

    @Override
    protected void initListener() {
        mLvTakingOrder.setOnGetMoreListener(this);
        mPtrTaking.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    currentPage = 1;
                    mPresenter.getTakingOrder(currentPage,1,mRange);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            mPtrTaking.autoRefresh(true);
            mPresenter.getTakingOrder(currentPage,1,mRange);
        }
    }

    private int mRange;

    public void setRange(int range) {
        mRange = range;
    }

    public void refresh() {
        if (null != mPresenter) {
            mPtrTaking.autoRefresh(true);
            currentPage = 1;
            mPresenter.getTakingOrder(currentPage,1,mRange);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getTakingOrderSuccess(TakingOrderMenuBean resultBean) {
        dismissDialog();
        if (currentPage == 1 && mPtrTaking.isRefreshing()) {
            mPtrTaking.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        mAdapter = new TakingOrderAdapter(mActivity,1, mResults);
        last_page = resultBean.last_page;
        if (currentPage == 1 && resultBean.data.size() == 0) {
            if (mPtrTaking.getHeader()!= null)
                mPtrTaking.getHeader().setVisibility(View.GONE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            return;
        }

        if (null != resultBean) {
                LogUtils.d("getTakingOrderSuccess", resultBean.toString());
                List<TakingOrderBean> results = resultBean.data;
                mResults.addAll(results);

                mAdapter.setCommitOrder(this);
                mAdapter.setCancelOrder(this);
                mAdapter.setPrintOrder(this);
                mLvTakingOrder.setAdapter(mAdapter);

                mAdapter.notifyDataSetChanged();
                if (currentPage > 1) {
                    mLvTakingOrder.getMoreComplete();
                }

                if (currentPage == last_page) {
                    mLvTakingOrder.setNoMore();
                }
            }

    }

    @Override
    public void getTakingOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrTaking, failMsg);
        }
    }

    @Override
    public void onEditText(String text) {
        mAlert.dismiss();
        mAlert = null;
        ResultsBean rb = null;
        if (selectPosition >= 0 && null != mAdapter.getItem(selectPosition)) {
            rb = (ResultsBean) mAdapter.getItem(selectPosition);
        }
        if (null != rb) {
            showDialog("加载中...");
        }
    }

    @Override
    public void onCompleteOrder(final TakingOrderBean bean) {
        mPresenter.completeOrder(bean.id);
    }

//    @Override
//    public void posOrder(final int position) {
//        mAlert = new MyAlertDialog(mActivity).builder()
//                .setMsg("是否打印小票")
//                .setNegativeButton("取消", null)
//                .setPositiveButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (PrintDataService.isConnection()) {
//                            showDialog("请稍后...");
//                            mPrintingBean = (ResultsBean) mAdapter.getItem(position);
//                            mPresenter.getPrintData(mPrintingBean.getId());
//                        } else {
//                            ViewUtils.showSnack(mPtrTaking, "未连接票据打印机");
//                        }
//                    }
//                });
//        mAlert.show();
//    }


//    @Override
//    public void getPrintDataSuccess(OrderDetailBean bean) {
//        final StringBuilder builder = new StringBuilder();
//        if (PrintDataService.isConnection()) {
//            PrintDataService.send(builder.toString());
//            dismissDialog();
//            return;
//        } else {
//            dismissDialog();
//        }
//
//        BigDecimal bd = null;
//        String fullname = (String) SpUtils.get(Constants.USER_FULLNAME, "");
//        if (!TextUtils.isEmpty(fullname)) {
//            builder.append("      ");
//            builder.append(fullname);
//            builder.append("\n");
//        }
//        if (null != bean) {
//            builder.append("      ");
//            String payStatus = bean.getPay_status();
//            String payChannel = bean.getPay_channel();
//            if (payStatus.equals("1")) {
//                builder.append("未支付");
//            } else if (payStatus.equals("2")) {
//                if (payChannel.equals("1")) {
//                    builder.append("已在线支付");
//                } else if (payChannel.equals("2")) {
//                    builder.append("已吧台支付");
//                }
//            }
//            String diningWay = mPrintingBean.getDining_way();
//            if (diningWay.equals("1")) {
//                builder.append("(堂食)");
//            } else if (diningWay.equals("2")) {
//                builder.append("(外带)");
//            }
//            builder.append("\n");
//            builder.append("---------------------------");
//            builder.append("\n");
//            String serialNumber = mPrintingBean.getSerial_number();
//            builder.append("  NO:");
//            builder.append(serialNumber);
//            builder.append("\n");
//            String deskNum = mPrintingBean.getDesk_num();
//            builder.append("桌号:");
//            builder.append(deskNum);
//            builder.append("    ");
//            int diningNum = mPrintingBean.getDine_num();
//            builder.append("人数:");
//            builder.append(diningNum);
//            builder.append("\n");
//            String orderDatetimeBj = mPrintingBean.getOrder_datetime_bj();
//            builder.append("时间:");
//            builder.append(orderDatetimeBj);
//            builder.append("\n");
//            builder.append("---------------------------");
//            builder.append("\n");
//            builder.append("菜品");
//            builder.append("      数量");
////            builder.append("  价格");
//            builder.append("    金额");
//            builder.append("\n");
//            builder.append("\n");
//        }
//        if (null != bean && bean.getOrderItems() != null) {
//            List<OrderItemsBean> items = bean.getOrderItems();
//            for (OrderItemsBean item : items) {
//                builder.append(item.getName());
//                builder.append("    ");
//                int num = item.getNum();
//                builder.append(num);
//                builder.append("    ");
//                String cost = item.getClosing_cost();
//                BigDecimal costBd = new BigDecimal(cost);
//                if (num > 1) {
//                    costBd = costBd.multiply(new BigDecimal(num));
//                }
//                if (null == bd) {
//                    bd = new BigDecimal(0);
//                    bd = bd.add(costBd);
//                } else {
//                    bd = bd.add(costBd);
//                }
//                builder.append(costBd.toString());
//                builder.append("\n");
//            }
//            builder.append("---------------------------");
//            builder.append("\n");
//        }
//        if (null != mPrintingBean) {
//            String remark = mPrintingBean.getRemark();
//            if (!TextUtils.isEmpty(remark)) {
//                builder.append("备注:");
//                builder.append(remark);
//                builder.append("\n");
//            }
//        }
//        builder.append("---------------------------");
//        builder.append("\n");
//        builder.append("消费金额: ");
//        if (bd != null) {
//            builder.append(bd.toString());
//        }
//        builder.append("\n");
//        builder.append("应收金额: ");
//        if (bd != null) {
//            builder.append(bd.toString());
//        }
//        builder.append("\n");
//        builder.append("---------------------------");
//        builder.append("\n");
//        builder.append("---------------------------");
//        builder.append("\n");
//        builder.append("小不点点餐  www.xcxid.com");
//        builder.append("\n");
//        builder.append("      欢迎下次光临");
//        builder.append("\n");
//        builder.append("\n");
//
//        String printData = builder.toString();
//        int printNum = (int) SpUtils.get(Constants.PRINTER_NUM, 1);
//        if (printNum == 2) {
//            builder.append(printData);
//        } else if (printNum == 3) {
//            builder.append(printData);
//            builder.append(printData);
//        }
//        if (PrintDataService.isConnection()) {
//            PrintDataService.send(builder.toString());
//            dismissDialog();
//        } else {
//            dismissDialog();
//        }
//    }
//
//    @Override
//    public void getPrintDataFail(String failMsg) {
//        dismissDialog();
//        if (!TextUtils.isEmpty(failMsg)) {
//            ViewUtils.showSnack(mPtrTaking, failMsg);
//        }
//    }






    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            mLvTakingOrder.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getTakingOrder(currentPage,1,mRange);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(DataSynEvent event) {
        Log.e("TakingFragment", "event---->" + event.isShouldUpdateData());
//        Toast.makeText(mActivity,"TakingFragment 数据刷新",Toast.LENGTH_SHORT).show();
        if (event.isShouldUpdateData()) {
            refresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentPage = 1;
        mAdapter = null;
        EventBus.getDefault().unregister(this);//解除订阅
    }

    @Override
    public void completeOrderSuccess(int orderId) {
        dismissDialog();
        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
            if (rb.id ==orderId) {
                mAdapter.removeItem(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
        if (mResults.size() == 0) {
            if (mPtrTaking.getHeader()!= null)
                mPtrTaking.getHeader().setVisibility(View.GONE);
        }
        if (mResults.size() == 0) {
            if (mPtrTaking.getHeader()!= null)
                mPtrTaking.getHeader().setVisibility(View.GONE);
        }
        CommonActivity.start(mActivity,OrderCompleteFragment.class,new Bundle());
//        Intent i = new Intent(mActivity, MainActivity.class);
//        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        mActivity.startActivity(i);
    }

    @Override
    public void completeOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrTaking, failMsg);
        }
    }

    @Override
    public void onCancelOrder(final TakingOrderBean bean) {
        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("取消订单后款项将原路返回")
                .setConfirmButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseModel.httpService.cancelOrder(bean.id, 1).compose(new CommonTransformer())
                                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                                    @Override
                                    public void onNext(String msg) {
                                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                                            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
                                            if (rb.id == bean.id) {
                                                mAdapter.removeItem(i);
                                                mAdapter.notifyDataSetChanged();
                                                return;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        super.onError(e);
                                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setPositiveButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        mAlert.show();
    }

    @Override
    public void onPrintOrder(TakingOrderBean bean) {
        final StringBuilder builder = new StringBuilder();
        if (bean == null)
            return;

        if (!PrintDataService.isConnection()) {
            dismissDialog();
            return;
        }
        BigDecimal bd = null;
        builder.append("林妈妈早餐 ");
        builder.append("\n");
        // 0 取消 1已取消 2 已退款
        if (bean.status.equals(0)){
            builder.append("取消");
        } else if (bean.status.equals("1")) {
            builder.append("已取消");
        } else if (bean.status.equals("2")){
            builder.append("已退款");
        }
        if (bean.is_for_here.equals(0)){
            builder.append("   （自取）");
        } else {
            builder.append("   （堂食）");
        }
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("预约单NO:");
        builder.append(bean.serial_number);
        builder.append("\n");

        builder.append("下单时间:"+bean.order_datetime_bj);

        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        builder.append("      菜品");
        builder.append("      数量");
        builder.append("      价格");
        builder.append("      金额");
        builder.append("\n");
        builder.append("\n");

        builder.append("\n");
        builder.append("      "+bean.pickup.pickup_date);
        if (bd != null) {
            builder.append(bd.toString());
        }
        builder.append("\n");
        builder.append("---------------------------");
        builder.append("\n");
        if (!bean.remark.equals("")) {
            builder.append("    备注："+bean.remark);
            builder.append("\n");
            builder.append("---------------------------");
            builder.append("\n");
        }

        builder.append("                      消费金额："+bean.pay_amount);
        builder.append("\n");
        builder.append("\n");

        builder.append("    取餐时间：    "+bean.pickup.pickup_date + " " + bean.pickup.pickup_start_time + "-" + bean.pickup.pickup_end_time);
        builder.append("\n");
        builder.append("    "+bean.place.place_name);
        builder.append("\n");
        builder.append("    "+bean.place.place_address);
        builder.append("\n");
        builder.append("    "+bean.user.user_name);
        builder.append("\n");
        builder.append("    "+bean.user.user_tel);
        builder.append("\n");
        builder.append("\n");

        PrintDataService.send(builder.toString());
        dismissDialog();
    }
}
