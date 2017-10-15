package com.xcxid.dinning.order.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.adapter.NonPayOrderAdapter;
import com.xcxid.dinning.base.BasePresenterFragment;
import com.xcxid.dinning.bean.CancelBean;
import com.xcxid.dinning.bean.NonPayOrderBean;
import com.xcxid.dinning.bean.ResultsBean;
import com.xcxid.dinning.order.order.OrderFragment;
import com.xcxid.dinning.order.pay.detail.NonPayOrderDetailActivity;
import com.xcxid.dinning.url.Constants;
import com.xcxid.dinning.utils.ActivityUtils;
import com.xcxid.dinning.utils.ViewUtils;
import com.xcxid.dinning.widget.MyAlertDialog;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class NonPayFragment extends BasePresenterFragment<NonPayOrderPresenter> implements
        NonPayOrderContract.NonPayOrderView, NonPayOrderAdapter.ICancelOrder, NonPayOrderAdapter.IConfirmOrder,
        NonPayOrderContract.CancelOrderView, NonPayOrderContract.ConfirmOrderView, MyAlertDialog.ICallBack,
        AdapterView.OnItemClickListener {
    @BindView(R.id.lvNonPayOrder)
    ListView mLvNonPayOrder;
    @BindView(R.id.ptr_nonpay)
    PtrClassicFrameLayout mPtrNonPay;

    private NonPayOrderAdapter mAdapter;
    private List<ResultsBean> mResults;
    private MyAlertDialog mAlert;
    private INonPayHint mNonPayHint;
    private int selectPosition;
    private static final int REQUEST_NONPAY_ORDER_DETAIL = 0x50;

    @Override
    protected NonPayOrderPresenter loadPresenter() {
        return new NonPayOrderPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_nonpay;
    }

    @Override
    protected void initView() {
        mNonPayHint = (OrderFragment) getParentFragment();
        mPtrNonPay.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
//                showDialog("加载中...");
                mPresenter.getNonPayOrder();
            }
        });
    }

    @Override
    protected void initListener() {
        mLvNonPayOrder.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            mPtrNonPay.autoRefresh(true);
//            showDialog("加载中...");
//            mPresenter.getNonPayOrder();
        }
    }

    public void refresh() {
        if (null != mPresenter) {
            mPresenter.getNonPayOrder();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getNonPayOrderSuccess(NonPayOrderBean nonPayOrderBean) {
        dismissDialog();
        mPtrNonPay.refreshComplete();
        if (null != nonPayOrderBean && null != nonPayOrderBean.getResults()) {
            mResults = nonPayOrderBean.getResults();
//            if (null != mResults && null != mNonPayHint) {
//                mNonPayHint.nonPayHint();
//            }
            mAdapter = new NonPayOrderAdapter(mActivity, mResults);
            mLvNonPayOrder.setAdapter(mAdapter);
            mAdapter.setCancelOrder(this);
            mAdapter.setConfirmOrder(this);
            mLvNonPayOrder.setOnItemClickListener(this);
        }
    }

    @Override
    public void getNonPayOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrNonPay, failMsg);
        }
    }

    @Override
    public void cancelOrder(final int position) {
        new MyAlertDialog(mActivity).builder()
                .setTitle("确认取消该订单吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog("加载中...");
                        mPresenter.cancelOrder(String.valueOf(mResults.get(position).getId()), "本店暂无法处理此订单请谅解");
                    }
                }).show();
    }

    @Override
    public void confirmOrder(int position) {
        selectPosition = position;
        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("请输入操作密码")
                .setEditHint("操作密码")
                .setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setNegativeButton("取消", null)
                .setConfirmButton("确定", NonPayFragment.this);
        mAlert.show();
    }

    @Override
    public void cancelOrderSuccess(String orderId, CancelBean result) {
        dismissDialog();
        for (ResultsBean rb : mResults) {
            if (String.valueOf(rb.getId()).equals(orderId)) {
                mResults.remove(rb);
                if (null != mResults && null != mNonPayHint) {
                    mNonPayHint.nonPayHint();
                }
                if (null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
        if (null != result && !TextUtils.isEmpty(result.getMsg())) {
            ViewUtils.showSnack(mPtrNonPay, result.getMsg());
        }
    }

    @Override
    public void cancelOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrNonPay, failMsg);
        }
    }

    @Override
    public void confirmOrderSuccess(String orderId) {
        dismissDialog();
        ViewUtils.showSnack(mPtrNonPay, "确认支付");
        for (ResultsBean rb : mResults) {
            if (String.valueOf(rb.getId()).equals(orderId)) {
                mResults.remove(rb);
                if (null != mResults && null != mNonPayHint) {
                    mNonPayHint.nonPayHint();
                }
                if (null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    @Override
    public void confirmOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrNonPay, failMsg);
        }
    }

    @Override
    public void onEditText(String text) {
        mAlert.dismiss();
        mAlert = null;
        ResultsBean rb = mResults.get(selectPosition);
        if (null != rb) {
            showDialog("加载中...");
            mPresenter.confirmOrder(String.valueOf(rb.getId()), text);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ResultsBean rb = mResults.get(i);
        Bundle data = new Bundle();
        data.putParcelable(Constants.ORDER_NONPAY_DETAIL, rb);
        ActivityUtils.startActivityForResult(this, NonPayOrderDetailActivity.class, data, REQUEST_NONPAY_ORDER_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_NONPAY_ORDER_DETAIL && null != data) {
            String orderId = data.getStringExtra(Constants.ORDER_ID);
            if (!TextUtils.isEmpty(orderId) && null != mResults) {
                for (ResultsBean rb : mResults) {
                    if (String.valueOf(rb.getId()).equals(orderId)) {
                        mResults.remove(rb);
                        if (null != mResults && null != mNonPayHint) {
                            mNonPayHint.nonPayHint();
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

    public interface INonPayHint {
        void nonPayHint();
    }
}
