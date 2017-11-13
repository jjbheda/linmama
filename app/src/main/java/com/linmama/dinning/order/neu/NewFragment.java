package com.linmama.dinning.order.neu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.OrderOrderMenuBean;
import com.linmama.dinning.bean.OrderPickupTimeBean;
import com.linmama.dinning.bean.OrderPlace;
import com.linmama.dinning.bean.OrderUser;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.order.neu.timepick.TimePickerActivity;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.NewOrderAdapter;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.order.order.OrderFragment;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ActivityUtils;
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
public class NewFragment extends BasePresenterFragment<NewOrderPresenter> implements
        NewOrderContract.NewOrderView, NewOrderContract.ReceiveOrderView
        ,MyAlertDialog.ICallBack, AdapterView.OnItemClickListener,NewOrderAdapter.ICommitOrder,NewOrderAdapter.ICancelOrder,
        NewOrderContract.PrintView, GetMoreListView.OnGetMoreListener {
    public boolean IsTest = false;
    @BindView(R.id.lvNewOrder)
    GetMoreListView mLvNewOrder;
//    @BindView(R.id.newOrderAllTv)
//    TextView mNewOrderAllTv;
//    @BindView(R.id.newOrderSaleTv)
//    TextView mNewOrderSaleTv;
    @BindView(R.id.ptr_new)
    PtrClassicFrameLayout mPtrNew;
    private NewOrderAdapter mAdapter;
    private INewHint mNewHint;
    private INewReceiveOrder mNewReceiveOrder;
    private List<LResultNewOrderBean> mResults;
    private MyAlertDialog mAlert;
    private int selectPosition = -1;
    private static final int REQUEST_SET_WARN_TIME = 0x10;
    private static final int REQUEST_NEW_ORDER_DETAIL = 0x11;
    private ResultsBean mPrintingBean = null;
    private boolean isPrinted = false;
    private boolean isPayed = true;
    private int currentPage = 1;

    @Override
    protected NewOrderPresenter loadPresenter() {
        return new NewOrderPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_new;
    }

    @Override
    protected void initView() {
        mResults = new ArrayList<>();
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrNew.setHeaderView(header);
        mPtrNew.addPtrUIHandler(header);
        EventBus.getDefault().register(this);//订阅
    }

    @Override
    protected void initListener() {
        mLvNewOrder.setOnGetMoreListener(this);
        mNewHint = (OrderFragment) getParentFragment();
        mNewReceiveOrder = (OrderFragment) getParentFragment();
        mPtrNew.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
//                    showDialog("加载中...");
//                    currentPage = 1;
                    mPresenter.getNewOrder(1);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            mPtrNew.autoRefresh(true);
//            showDialog("加载中...");
//            mPresenter.getNewOrder();
        }
    }

    public void refresh() {
        if (null != mPresenter) {
            mPtrNew.autoRefresh(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SET_WARN_TIME && null != data) {
            String orderId = data.getStringExtra(Constants.ORDER_ID);
            if (!TextUtils.isEmpty(orderId) && null != mResults) {
                for (LResultNewOrderBean rb : mResults) {
                    if (String.valueOf(rb.id).equals(orderId)) {
                        mResults.remove(rb);
                        if (null != mResults && null != mNewHint) {
                            mNewHint.newHint();
                        }
                        if (null != mNewReceiveOrder) {
                            mNewReceiveOrder.notifyReceiveOrder();
                        }
                        if (null != mAdapter) {
                            mAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                }
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_NEW_ORDER_DETAIL && null != data) {
            String orderId = data.getStringExtra(Constants.ORDER_ID);
            if (!TextUtils.isEmpty(orderId) && null != mResults) {
                for (LResultNewOrderBean rb : mResults) {
                    if (String.valueOf(rb.id).equals(orderId)) {
                        mResults.remove(rb);
                        if (null != mResults && null != mNewHint) {
                            mNewHint.newHint();
                        }
                        if (null != mNewReceiveOrder && data.getBooleanExtra(Constants.BOOLEAN_ORDER_TAKE, false)) {
                            mNewReceiveOrder.notifyReceiveOrder();
                        }
                        if (null != mAdapter) {
                            mAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onEditText(String text) {
        LogUtils.d("onEditText", text);
        mAlert.dismiss();
        mAlert = null;
        LResultNewOrderBean rb = null;
        if (null != mResults && selectPosition >= 0) {
            rb = mResults.get(selectPosition);
        }
        if (null != rb && null != mPresenter) {
            showDialog("加载中...");
//            mPresenter.okOrder(String.valueOf(rb.id), text);
        }
    }

    @Override
    public void getNewOrderSuccess(List<LResultNewOrderBean> bean) {
        dismissDialog();
        if (currentPage == 1 && mPtrNew.isRefreshing()) {
            mPtrNew.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
//        if (TextUtils.isEmpty(bean.getNext())) {
            mLvNewOrder.setNoMore();
//        } else {
//            mLvNewOrder.setHasMore();
//        }
            List<LResultNewOrderBean> results = bean;

            if (IsTest){
                LResultNewOrderBean model = new LResultNewOrderBean();
                model.remark = "少放糖";
                OrderOrderMenuBean menuBean = new OrderOrderMenuBean();
                menuBean.date = "周二";
                OrderGoodBean goodBean = new OrderGoodBean();
                goodBean.amount = 3;
                goodBean.name = "西红柿炒蛋";
                goodBean.total_price = "20";
                menuBean.goods_list .add(goodBean);
                model.order_list.add(menuBean);
                OrderPickupTimeBean pickupTimeBean = new OrderPickupTimeBean();
                pickupTimeBean.pickup_date = "2017-09-19";
                pickupTimeBean.pickup_start_time = "08:00";
                pickupTimeBean.pickup_end_time = "10:00";
                model.pickup_list.add(pickupTimeBean);
                 OrderPlace place = new OrderPlace();
                 place.place_address = "建国桥菜市场02号";
                 place.place_name = "西城区";
                 place.place_type = "0";
                 model.place = place;

                 OrderUser user = new OrderUser();
                 user.user_name = "张三";
                 user.user_tel = "13900000000";
                 model.user = user;
                model.serial_number ="No20171010224933576179"; //订单号
                model.id = "2"; //订单ID
                model.amount = 3;  //商品总数
                model.is_for_here = "1";
                model.order_type = "0"; //1预约单 0当日单
                model.is_ensure_order = "1"; //1已接单 0未接单
                model.pay_amount = "0.2"; //"0.07", //支付金额
                model.order_datetime_bj = "2017-10-10 22:49:33"; //下单日期
                results.add(model);
            }
            mResults.addAll(results);
            LogUtils.d("Results", results.size() + "");
//            if (null != mNewHint) {
//                mNewHint.newHint();
//            }
            if (null == mAdapter) {
                mAdapter = new NewOrderAdapter(mActivity, mResults);
                mLvNewOrder.setAdapter(mAdapter);
                mLvNewOrder.setOnItemClickListener(this);
                mAdapter.setCommitOrder(this);
                mAdapter.setCancelOrder(this);
            } else {
                mAdapter.notifyDataSetChanged();
                if (currentPage > 1) {
                    mLvNewOrder.getMoreComplete();
                }
            }
    }

    @Override
    public void onGetMore() {
    }

    @Override
    public void getNewOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrNew, failMsg);
        }
    }

//    @Override
//    public void takeOrder(int position) {
//        LogUtils.d("confirmOrder", position + "");
//        LResultNewOrderBean rb = mResults.get(position);
//        if (null == rb) {
//            ViewUtils.showSnack(mPtrNew, "订单不存在");
//            return;
//        }
//        if (rb.is_in_store()) {
//            mPrintingBean = rb;
//            final int orderid = mResults.get(position).id;
//            mAlert = new MyAlertDialog(mActivity).builder()
//                    .setMsg("是否打印小票")
//                    .setNegativeButton("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            isPrinted = false;
//                            mPrintingBean = null;
//                            showDialog("加载中...");
//                            mPresenter.receiveOrder(String.valueOf(orderid));
//                        }
//                    })
//                    .setPositiveButton("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            showDialog("加载中...");
//                            if (PrintDataService.isConnection()) {
//                                isPrinted = true;
//                                mPresenter.receiveOrder(String.valueOf(orderid));
//
//                            } else {
//                                isPrinted = false;
//                                mPresenter.receiveOrder(String.valueOf(orderid));
//                                ViewUtils.showSnack(mPtrNew, "未连接票据打印机");
//                            }
//                        }
//                    });
//            if (!isPayed) {
//                new MyAlertDialog(mActivity).builder()
//                        .setMsg("请尽快核实客人是否已到店")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                mAlert.show();
//                            }
//                        }).show();
//            } else {
//                mAlert.show();
//            }
//        } else {
//            Bundle data = new Bundle();
//            ActivityUtils.startActivityForResult(this, TimePickerActivity.class, data, REQUEST_SET_WARN_TIME);
////        }
//    }

    @Override
    public void receiveOrderSuccess(String orderId) {
        if (!isPrinted) {
            dismissDialog();
        } else {
            if (null != mPresenter) {
                mPresenter.getPrintData(Integer.parseInt(orderId));
            }
        }
        ViewUtils.showSnack(mPtrNew, "接单成功");
        for (LResultNewOrderBean rb : mResults) {
            if (String.valueOf(rb.id).equals(orderId)) {
                mResults.remove(rb);
                if (null != mResults && null != mNewHint) {
                    mNewHint.newHint();
                }
                if (null != mNewReceiveOrder) {
                    mNewReceiveOrder.notifyReceiveOrder();
                }
                if (null != mNewReceiveOrder && !isPayed) {
                    mNewReceiveOrder.notifyNonPayOrder();
                }
                if (null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    @Override
    public void receiveOrderFail(String failMsg) {
        if (!isPrinted) {
            dismissDialog();
        }
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrNew, failMsg);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LResultNewOrderBean rb = mResults.get(i);
        Bundle data = new Bundle();
//        data.putParcelable(Constants.ORDER_NEW_DETAIL, rb);
//        ActivityUtils.startActivityForResult(this, NewOrderDetailActivity.class, data, REQUEST_NEW_ORDER_DETAIL);
    }

    @Override
    public void getPrintDataSuccess(OrderDetailBean bean) {
        StringBuilder builder = new StringBuilder();
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
        if (null != mPrintingBean ) {
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
    }

    @Override
    public void getPrintDataFail(String failMsg) {
        dismissDialog();
    }

    @Override
    public void onCommitOrder(final LResultNewOrderBean bean) {
        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("接单并打印小票")
                .setConfirmButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseModel.httpService.commitOrder(bean.id + "").compose(new CommonTransformer())
                                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                                    @Override
                                    public void onNext(String msg) {
                                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                                            LResultNewOrderBean rb = (LResultNewOrderBean) mAdapter.getItem(i);
                                            if (rb.id .equals(bean.id)) {
                                                mAdapter.removeItem(i);
                                                mAdapter.notifyDataSetChanged();
                                                EventBus.getDefault().post(new DataSynEvent(true));
                                                return;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        super.onError(e);
                                        Toast.makeText(mActivity, "确定订单失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setPositiveButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        mAlert.show();
    }

    @Override
    public void onCancelOrder(final LResultNewOrderBean bean) {
        BaseModel.httpService.cancelOrder(bean.id+"",1). compose(new CommonTransformer())
                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(String msg) {
                        Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                            LResultNewOrderBean rb = (LResultNewOrderBean) mAdapter.getItem(i);
                            if (rb.id .equals(bean.id)) {
                                mAdapter.removeItem(i);
                                mAdapter.notifyDataSetChanged();
                                EventBus.getDefault().post(new DataSynEvent(true));
                                return;
                            }
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toast.makeText(mActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(DataSynEvent event) {
    }

    public interface INewHint {
        void newHint();
    }

    public interface INewReceiveOrder {
        void notifyReceiveOrder();

        void notifyNonPayOrder();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentPage = 1;
        mAdapter = null;
        EventBus.getDefault().unregister(this);//解除订阅
    }
}
