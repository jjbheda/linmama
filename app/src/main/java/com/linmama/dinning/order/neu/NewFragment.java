package com.linmama.dinning.order.neu;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.NewOrderAdapter;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.NewOrderMenuBean;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bluetooth.CheckPrinterActivity;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.order.order.OrderFragment;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */
public class NewFragment extends BasePresenterFragment<NewOrderPresenter> implements
        NewOrderContract.NewOrderView, MyAlertDialog.ICallBack, NewOrderAdapter.ICommitOrder, NewOrderAdapter.ICancelOrder,GetMoreListView.OnGetMoreListener {
    private static String TAG = "NewFragment";
    private long period = 1000*30;
    private Timer timer = new Timer();

    @BindView(R.id.lvNewOrder)
    GetMoreListView mLvNewOrder;
    @BindView(R.id.ptr_new)
    PtrClassicFrameLayout mPtrNew;
    private NewOrderAdapter mAdapter;
    private List<LResultNewOrderBean> mResults = new ArrayList<>();

    private MyAlertDialog mAlert;
    private int selectPosition = -1;
    private int currentPage = 1;
    private int last_page = 1;
    private boolean isLoading = false;

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
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrNew.setHeaderView(header);
        mPtrNew.addPtrUIHandler(header);
        EventBus.getDefault().register(this);//订阅
        RefreshTask localMyTask = new RefreshTask();
        this.timer.schedule(localMyTask, 100, period);
    }

    @Override
    protected void initListener() {
        mLvNewOrder.setOnGetMoreListener(this);
        mPtrNew.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter && !isLoading) {
                    currentPage = 1;
                    mPresenter.getNewOrder(1);
                    isLoading = true;
                }
            }
        });

    }

    private int mId;    //推送订单的ID值

    public void setId(int id) {
        mId = id;
    }

    private String mOrdertype;      // 订单类型  0 当日单 1预约单

    public void setOrderType(String orderType){
        mOrdertype = orderType;
    }

    private OrderFragment.CompleteOrderCallback mCompleteOrderCallback;

    public void setCompleteOrderCallback(OrderFragment.CompleteOrderCallback callback) {
        mCompleteOrderCallback = callback;
    }

    @Override
    protected void initData() {
//        if (null != mPresenter) {
//            mPtrNew.autoRefresh(true);
//        }
            showDialog("加载中...");
            Log.d("http","加载中  initData  ..........。。。。。。。。。。。。。。。。。。。。。。");
            currentPage = 1;
            mPresenter.getNewOrder(1);
            isLoading = true;
    }

    public void refresh() {
        if (null != mPresenter && !isLoading) {
            if (isVisible()) {
                showDialog("加载中...");
            }
            Log.d("http","加载中  newFragment refresh ..........。。。。。。。。。。。。。。。。。。。。。。");
            currentPage = 1;
            mPresenter.getNewOrder(1);
            isLoading = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
//            showDialog("加载中...");
        }
    }

    @Override
    public void getNewOrderSuccess(NewOrderMenuBean bean) {
        dismissDialog();
        if (currentPage == 1 && mPtrNew.isRefreshing()) {
            mPtrNew.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
//        mLvNewOrder.setNoMore();
        last_page = bean.last_page;
        mResults.addAll(bean.data);
        LogUtils.d("Results", bean.data.size() + "");

//        mAdapter = new NewOrderAdapter(mActivity, mResults);
//        mLvNewOrder.setAdapter(mAdapter);
//        mAdapter.setCommitOrder(this);
//        mAdapter.setCancelOrder(this);

//        boolean isAutoReceiveOrder = (boolean) SpUtils.get(Constants.AUTO_RECEIVE_ORDER, false);
//        if (isAutoReceiveOrder && mId != 0) {
        updateNextPage();
    }

    @Override
    public void onGetMore() {
    }

    public void updateNextPage() {

        if (currentPage >= last_page) {
            commitAndPrint();
            isLoading = false;
            return;
        }
        if (last_page == 0) {
            return;
        }
        showDialog("加载中...");
        Log.d("http","加载中  newFragment updateNextPage ..........。。。。。。。。。。。。。。。。。。。。。。");
        currentPage++;
        mPresenter.getNewOrder(currentPage);
    }

    private void commitAndPrint() {
        if (mResults.size() > 0) {
            HandlerThread thread = new HandlerThread("NetWork");
            thread.start();
            Handler handler = new Handler(thread.getLooper());
            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void run() {
                    for (LResultNewOrderBean model : mResults) {
                        autoCompleteOrder(model.id,model.order_type);
                        boolean isAutoPrint = (boolean) SpUtils.get(Constants.AUTO_PRINT, false);
                        if (isAutoPrint) {               //自动打印
                            printOrderWithFeiE(model);
                            Log.e(TAG,model.order_no+"");
                        }
                    }
                    EventBus.getDefault().post(new DataSynEvent(true));     //通知预约单、当日单刷新
                }
            },100);

        }
    }

    @Override
    public void getNewOrderFail(String failMsg) {
        dismissDialog();
        isLoading = false;
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrNew, failMsg);
        }
    }

    /**
     * 非自动接单的情况
     *
     * @param bean
     */
    @Override
    public void onCommitOrder(final LResultNewOrderBean bean) {
        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("接单并打印小票")
                .setConfirmButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseModel.httpService.commitOrder(bean.id + "").compose(new CommonTransformer())
                                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                                    @Override
                                    public void onNext(String msg) {
                                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                                            LResultNewOrderBean rb = (LResultNewOrderBean) mAdapter.getItem(i);
                                            if (rb != null && rb.id == bean.id) {
                                                printOrderWithFeiE(rb);
                                                mAdapter.removeItem(i);
                                                mAdapter.notifyDataSetChanged();
                                                EventBus.getDefault().post(new DataSynEvent(true));
                                                break;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        super.onError(e);
                                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        if (null != mPresenter) {
                                            currentPage = 1;
                                            mPresenter.getNewOrder(1);
                                        }
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

        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("取消订单，款项将原路返回")
                .setConfirmButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseModel.httpService.cancelOrder(bean.id, 0).compose(new CommonTransformer())
                                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                                    @Override
                                    public void onNext(String msg) {
                                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                                            LResultNewOrderBean rb = (LResultNewOrderBean) mAdapter.getItem(i);
                                            if (rb != null && rb.id == bean.id) {
                                                mAdapter.removeItem(i);
                                                mAdapter.notifyDataSetChanged();
                                                EventBus.getDefault().post(new DataSynEvent(true));
                                                break;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        super.onError(e);
                                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        if (null != mPresenter) {
                                            currentPage = 1;
                                            mPresenter.getNewOrder(1);
                                        }
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

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(DataSynEvent event) {

    }

    /**
     * 自动接单的情况
     *
     * @param id
     */

    public void autoCompleteOrder(final int id,final String order_type) {
        BaseModel.httpService.commitOrder(id + "").compose(new CommonTransformer())
                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(String msg) {
                        ViewUtils.showToast(mActivity, msg);
                        dismissDialog();
                            EventBus.getDefault().post(new DataSynEvent(true));     //通知当日单，预约单刷新数据
                            Log.d(TAG, "今日订单,订单序号" + id + "确认成功!");
                            if (mCompleteOrderCallback != null) {       //自动接单情况下，自动确认该订单成功后，自动跳转上方menu
                            mCompleteOrderCallback.success(order_type); //1预约单 0当日单
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        dismissDialog();
                        super.onError(e);
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                        if (null != mPresenter) {
                            currentPage = 1;
                            mPresenter.getNewOrder(1);
                        }
                    }
                });
    }

    private void printOrderWithFeiE(final LResultNewOrderBean bean){
        FeiEPrinterUtils.FeiprintNewOrderWithLoading(mActivity,bean);
        dismissDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentPage = 1;
        mAdapter = null;
        EventBus.getDefault().unregister(this);//解除订阅
    }


    //handler 处理返回的请求结果
    Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean refesh_now = data.getBoolean("Refesh",false);
            if (refesh_now && !isLoading) {
                if (null != mPresenter) {
                    isLoading = true;
                    currentPage = 1;
                    mPresenter.getNewOrder(1);
                }
            }

        }
    };
    class RefreshTask extends TimerTask {
        RefreshTask() {
        }

        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putBoolean("Refesh",true);
            msg.setData(data);
            if (mResults.size() == 0 && !isDialogShowing()) {
                updateHandler.sendMessage(msg);
            }

        }
    }
}
