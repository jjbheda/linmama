package com.linmama.dinning.order.neu;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.DataSynEvent;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.OrderOrderMenuBean;
import com.linmama.dinning.bean.OrderPickupTimeBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.home.MainActivity;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.utils.printer.FeiEPrinterUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.NewOrderAdapter;
import com.linmama.dinning.bean.ResultsBean;
import com.linmama.dinning.bluetooth.PrintDataService;
import com.linmama.dinning.order.order.OrderFragment;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.SpUtils;
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
public class NewFragment extends BasePresenterFragment<NewOrderPresenter> implements
        NewOrderContract.NewOrderView, MyAlertDialog.ICallBack, NewOrderAdapter.ICommitOrder, NewOrderAdapter.ICancelOrder,GetMoreListView.OnGetMoreListener {
    private static String TAG = "NewFragment";
    @BindView(R.id.lvNewOrder)
    GetMoreListView mLvNewOrder;
    @BindView(R.id.ptr_new)
    PtrClassicFrameLayout mPtrNew;
    private NewOrderAdapter mAdapter;
    private List<LResultNewOrderBean> mResults = new ArrayList<>();

    private MyAlertDialog mAlert;
    private int selectPosition = -1;
    private ResultsBean mPrintingBean = null;
    private boolean isPrinted = false;
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
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrNew.setHeaderView(header);
        mPtrNew.addPtrUIHandler(header);
        EventBus.getDefault().register(this);//订阅
    }

    @Override
    protected void initListener() {
        mLvNewOrder.setOnGetMoreListener(this);
        mPtrNew.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    currentPage = 1;
                    mPresenter.getNewOrder(1);
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
        if (null != mPresenter) {
            mPtrNew.autoRefresh(true);
        }
        showDialog("加载中...");
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
        mLvNewOrder.setNoMore();
        mResults.addAll(bean);
        LogUtils.d("Results", bean.size() + "");

        mAdapter = new NewOrderAdapter(mActivity, mResults);
        mLvNewOrder.setAdapter(mAdapter);
        mAdapter.setCommitOrder(this);
        mAdapter.setCancelOrder(this);

        boolean isAutoReceiveOrder = (boolean) SpUtils.get(Constants.AUTO_RECEIVE_ORDER, false);
        if (isAutoReceiveOrder && mId != 0) {
            if (mResults.size() > 0 && mAdapter != null) {
                for (LResultNewOrderBean model : mResults) {
                    if (model.id == mId) {
                        autoCompleteOrder(model.id);
                        boolean isAutoPrint = (boolean) SpUtils.get(Constants.AUTO_PRINT, false);
                        if (isAutoPrint) {               //自动打印
                            printOrderWithCheck(model);
                        }
                        break;
                    }
                }
                EventBus.getDefault().post(new DataSynEvent(true));     //通知预约单、当日单刷新
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
                                    @Override
                                    public void onNext(String msg) {
                                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                                            LResultNewOrderBean rb = (LResultNewOrderBean) mAdapter.getItem(i);
                                            if (rb != null && rb.id == bean.id) {
                                                printOrderWithCheck(rb);
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

    public void autoCompleteOrder(final int id) {
        BaseModel.httpService.commitOrder(id + "").compose(new CommonTransformer())
                .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(String msg) {
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                        for (int i = 0, size = mAdapter.getCount(); i < size; i++) {
                            LResultNewOrderBean rb = (LResultNewOrderBean) mAdapter.getItem(i);
                            if (rb != null && rb.id == id) {
                                mAdapter.removeItem(i);
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new DataSynEvent(true));     //通知当日单，预约单刷新数据
                                Log.d(TAG, "今日订单,订单序号" + id + "确认成功!");
                                boolean isAutoReceiveOrder = (boolean) SpUtils.get(Constants.AUTO_RECEIVE_ORDER, false);        //再判断一次
                                if (isAutoReceiveOrder && mCompleteOrderCallback != null) {       //自动接单情况下，自动确认该订单成功后，自动跳转
                                    mCompleteOrderCallback.success(rb.order_type); //1预约单 0当日单
                                    mId = 0;
                                }

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


    private void printOrderWithCheck(final LResultNewOrderBean bean){
//        if (bean == null)
//            return;
//
//        if (!PrintDataService.getInstance().isConnection()) {
//            showDialog("正在连接打印机");
//            PrintDataService.getInstance().connect(new PrintDataService.ConnectCallback() {
//                @Override
//                public void connectSucess() {
//                    dismissDialog();
//                    printOrder2(bean);
//                    ViewUtils.showToast(mActivity, "已连接票据打印机");
//                }
//
//                @Override
//                public void connectFailed() {
//                    dismissDialog();
//                    ViewUtils.showToast(mActivity, "票据打印机连接失败");
//                }
//            });
//
//        } else {
//            printOrder2(bean);
//        }
        printOrder2(bean);

        dismissDialog();
    }


    private void printOrder2(LResultNewOrderBean bean){
        FeiEPrinterUtils.FeiprintNewOrderWithLoading(mActivity,bean);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currentPage = 1;
        mAdapter = null;
        EventBus.getDefault().unregister(this);//解除订阅
    }
}
