package com.linmama.dinning.setting.complete;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.linmama.dinning.adapter.CompleteOrderAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.CompleteOrderBean;
import com.linmama.dinning.bean.OrderDetailBean;
import com.linmama.dinning.setting.complete.detail.CompletedOrderDetailActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ActivityUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.header.WindmillHeader;
import com.linmama.dinning.R;
import com.linmama.dinning.bean.OrderItemsBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.widget.MyAlertDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jingkang on 2017/4/1
 */

public class CompleteOrderListFragment extends BasePresenterFragment<CompleteOrderListPresenter> implements
        CompleteOrderListContract.CompleteListOrderView, GetMoreListView.OnGetMoreListener,
        CompleteOrderAdapter.IPosOrder, AdapterView.OnItemClickListener, CompleteOrderListContract.PrintView{
    @BindView(R.id.lvCompleteOrder)
    GetMoreListView lvCompleteOrder;
    @BindView(R.id.ptr_complete)
    PtrClassicFrameLayout ptrComplete;

    private CompleteOrderAdapter mAdapter;
    private int currentPage = 1;
    private List<ResultsBean> mResults;
    private ResultsBean mPrintingBean;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ResultsBean rb = mResults.get(i);
        Bundle data = new Bundle();
        data.putParcelable(Constants.ORDER_COMPLETE_DETAIL, rb);
        ActivityUtils.startActivity(mActivity, CompletedOrderDetailActivity.class, data);
    }

    @Override
    public void posOrder(final int position) {
        new MyAlertDialog(mActivity).builder()
                .setMsg("是否打印小票")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PrintDataService.isConnection()) {
                            showDialog("请稍后...");
                            mPrintingBean = mResults.get(position);
                            mPresenter.getPrintData(mPrintingBean.getId());
                        } else {
                            ViewUtils.showSnack(ptrComplete, "未连接票据打印机");
                        }
                    }
                }).show();
    }

    @Override
    protected CompleteOrderListPresenter loadPresenter() {
        return new CompleteOrderListPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_complete;
    }

    @Override
    protected void initView() {
        mResults = new ArrayList<>();
        final WindmillHeader header = new WindmillHeader(mActivity);
        ptrComplete.setHeaderView(header);
        ptrComplete.addPtrUIHandler(header);
    }

    @Override
    protected void initListener() {
        lvCompleteOrder.setOnGetMoreListener(this);
        ptrComplete.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
//                    showDialog("加载中...");
                    currentPage = 1;
                    mPresenter.getCompleteOrderList(currentPage);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            ptrComplete.autoRefresh(true);
        }
    }

    @Override
    public void getCompleteOrderListSuccess(CompleteOrderBean bean) {
        dismissDialog();
        if (currentPage == 1 && ptrComplete.isRefreshing()) {
            ptrComplete.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        if (TextUtils.isEmpty(bean.getNext())) {
            lvCompleteOrder.setNoMore();
        } else {
            lvCompleteOrder.setHasMore();
        }
        if (null != bean && null != bean.getResults()) {
            List<ResultsBean> results = bean.getResults();
            mResults.addAll(results);
            LogUtils.d("Results", results.size() + "");
            if (null == mAdapter) {
                mAdapter = new CompleteOrderAdapter(mActivity, mResults);
                mAdapter.setPosOrder(this);
                lvCompleteOrder.setAdapter(mAdapter);
                lvCompleteOrder.setOnItemClickListener(this);
            } else {
                mAdapter.notifyDataSetChanged();
                if (currentPage > 1) {
                    lvCompleteOrder.getMoreComplete();
                }
            }
        }
    }

    @Override
    public void getCompleteOrderListFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(ptrComplete, failMsg);
        }
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
            ViewUtils.showSnack(ptrComplete, failMsg);
        }
    }

    @Override
    public void onGetMore() {
        currentPage++;
        mPresenter.getCompleteOrderList(currentPage);
    }
}
