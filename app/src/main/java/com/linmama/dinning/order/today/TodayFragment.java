package com.linmama.dinning.order.today;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteFragment;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.widget.header.WindmillHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        TodayOrderContract.TodayOrderView,TodayOrderContract.CompleteOrderView,TakingOrderAdapter.IPrintOrder,
        GetMoreListView.OnGetMoreListener, TakingOrderAdapter.ICompleteOrder, TakingOrderAdapter.ICancelOrder {
   public static String TAG = "TodayFragment";
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
    private int last_page = 1;
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
        EventBus.getDefault().register(this);//订阅
    }

    @Override
    protected void initListener() {
        mLvTakingOrder.setOnGetMoreListener(this);
        mPtrTaking.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
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
            currentPage = 1;
            mPresenter.getTodayOrder(currentPage);
        }
    }

    public void refresh() {
        if (null != mPresenter) {
            mPtrTaking.autoRefresh(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(DataSynEvent event) {
        Log.e("TodayFragment", "event---->" + event.isShouldUpdateData());
//        Toast.makeText(mActivity, "TodayFragment 数据刷新", Toast.LENGTH_SHORT).show();
        if (event.isShouldUpdateData()) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getTodayOrderSuccess(TakingOrderMenuBean resultBean) {
        dismissDialog();
        if (currentPage == 1 && mPtrTaking.isRefreshing()) {
            mPtrTaking.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        mAdapter = new TakingOrderAdapter(mActivity, 0, mResults);
        if (currentPage == 1 && resultBean.data.size() == 0) {
            if (mPtrTaking.getHeader() != null)
                mPtrTaking.getHeader().setVisibility(View.GONE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            return;
        }
        last_page = resultBean.last_page;
        if (null != resultBean.data && resultBean.data.size() > 0) {
            LogUtils.d("getTakingOrderSuccess", resultBean.data.toString());
            mResults.addAll(resultBean.data);
            last_page = resultBean.last_page;

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
    public void getTodayOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrTaking, failMsg);
        }
    }

    @Override
    public void onPrintOrder(TakingOrderBean bean) {
        bean.ordertype = 0;        //强制改为 当日单类型
//        PrintUtils.printOrder(TAG,bean);
        FeiEPrinterUtils.FeiprintOrderWithLoading(mActivity,bean);
        dismissDialog();
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
    public void onCompleteOrder(final TakingOrderBean bean) {
        mPresenter.completeOrder(bean.id);
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
                                            if (rb != null && rb.id == bean.id) {
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
    public void completeOrderSuccess(int orderId) {
        dismissDialog();
        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
            TakingOrderBean rb = (TakingOrderBean) mAdapter.getItem(i);
            if (rb != null && rb.id == orderId) {
                mAdapter.removeItem(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
        CommonActivity.start(mActivity, OrderCompleteFragment.class, new Bundle());
    }

    @Override
    public void completeOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrTaking, failMsg);
        }
    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            mLvTakingOrder.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getTodayOrder(currentPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentPage = 1;
        mAdapter = null;
        EventBus.getDefault().unregister(this);//解除订阅
    }
}
