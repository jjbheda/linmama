package com.linmama.dinning.order.taking;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TakingOrderMenuBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.order.ordercompletesearch.OrderCompleteFragment;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.SpUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.utils.LogUtils;
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

public class TakingFragment extends BasePresenterFragment<TakingOrderPresenter> implements
        TakingOrderContract.TakingOrderView, TakingOrderAdapter.ICompleteOrder, TakingOrderAdapter.ICancelOrder, TakingOrderAdapter.IPrintOrder,
        GetMoreListView.OnGetMoreListener, TakingOrderContract.CompleteOrderView {
    public static String TAG = "TakingFragment";

    @BindView(R.id.lvNewOrder)
    GetMoreListView mLvTakingOrder;
    @BindView(R.id.ptr_new)
    PtrClassicFrameLayout mPtrTaking;

    private TakingOrderAdapter mAdapter;
    private MyAlertDialog mAlert;
    private List<TakingOrderBean> mResults = new ArrayList<>();
    private int currentPage = 1;
    private int last_page = 1;
    private static final int REQUEST_TAKE_ORDER_DETAIL = 0x20;
    private TakingOrderPresenter presenter;

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

    private boolean isPullRefresh = false;

    @Override
    protected void initListener() {
        mLvTakingOrder.setOnGetMoreListener(this);
        mPtrTaking.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    if (mLvTakingOrder!=null) {
                        mLvTakingOrder.setNoMore();
                    }
                    currentPage = 1;
                    isPullRefresh = true;
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                    mPresenter.getTakingOrder(currentPage, 1, mRange);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            mPtrTaking.autoRefresh(true);
            mPresenter.getTakingOrder(currentPage, 1, mRange);
        }
        showDialog("加载中...");
    }

    private int mRange;

    public void setRange(int range) {
        mRange = range;
    }

    public void refresh() {
        if (null != mPresenter) {
            mPtrTaking.autoRefresh(true);
            currentPage = 1;
            mPresenter.getTakingOrder(currentPage, 1, mRange);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getTakingOrderSuccess(TakingOrderMenuBean resultBean) {
        dismissDialog();
        if (mAdapter == null || currentPage == 1 || isPullRefresh) {
            mAdapter = new TakingOrderAdapter(mActivity, 1, mResults);
            mLvTakingOrder.setAdapter(mAdapter);
        }

        if (currentPage == 1 && mPtrTaking.isRefreshing()) {
            mPtrTaking.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }

        isPullRefresh = false;
        last_page = resultBean.last_page;
        if (currentPage == 1 && resultBean.data.size() == 0) {
            if (mPtrTaking.getHeader() != null)
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

            if (currentPage > 1) {
                mLvTakingOrder.getMoreComplete();
            }

            if (currentPage == last_page) {
                mLvTakingOrder.setNoMore();
            } else {
                mLvTakingOrder.setHasMore();
            }
            mAdapter.notifyDataSetChanged();
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
    public void onCompleteOrder(final TakingOrderBean bean) {
        mPresenter.completeOrder(bean.id);
    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            mLvTakingOrder.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getTakingOrder(currentPage, 1, mRange);
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
            if (rb != null && rb.id == orderId) {
                mAdapter.removeItem(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
        if (mResults.size() == 0) {
            if (mPtrTaking.getHeader() != null)
                mPtrTaking.getHeader().setVisibility(View.GONE);
        }
        if (mResults.size() == 0) {
            if (mPtrTaking.getHeader() != null)
                mPtrTaking.getHeader().setVisibility(View.GONE);
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
    public void onPrintOrder(TakingOrderBean bean) {
        bean.ordertype = 1;        //强制改为 预约单
        dismissDialog();
        FeiEPrinterUtils.FeiprintOrderWithLoading(mActivity,bean);

    }

}
