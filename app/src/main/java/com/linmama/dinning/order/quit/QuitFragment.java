package com.linmama.dinning.order.quit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.RefundBean;
import com.linmama.dinning.order.order.OrderFragment;
import com.linmama.dinning.order.quit.detail.QuitOrderDetailActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.utils.ActivityUtils;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.MyAlertDialog;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.QuitOrderAdapter;
import com.linmama.dinning.bean.QuitOrderBean;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class QuitFragment extends BasePresenterFragment<QuitOrderPresenter> implements
        QuitOrderContract.QuitOrderView, QuitOrderAdapter.IRefund, QuitOrderAdapter.IRefuse,
        QuitOrderContract.RefundView, QuitOrderContract.RefuseRefundView, MyAlertDialog.ICallBack,
        AdapterView.OnItemClickListener {
    @BindView(R.id.lvQuitOrder)
    ListView mLvQuitOrder;
    @BindView(R.id.ptr_quit)
    PtrClassicFrameLayout mPtrQuit;

    private IRefundHint mRefundHint;
    private QuitOrderAdapter mAdapter;
    private MyAlertDialog mAlert;
    private int groupId;
    private int itemId;
    private static final int REQUEST_QUIT_ORDER_DETAIL = 0x40;
    private boolean isRefunded = false;

    @Override
    protected QuitOrderPresenter loadPresenter() {
        return new QuitOrderPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_quit;
    }

    @Override
    protected void initView() {
        mRefundHint = (OrderFragment) getParentFragment();
        mPtrQuit.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
//                    showDialog("加载中...");
                    mPresenter.getQuitOrder();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            mPtrQuit.autoRefresh(true);
//            showDialog("加载中...");
//            mPresenter.getQuitOrder();
        }
    }

    @Override
    public void getQuitOrderSuccess(List<QuitOrderBean> list) {
        dismissDialog();
        mPtrQuit.refreshComplete();
        if (null != list) {
            mAdapter = new QuitOrderAdapter(mActivity, list);
            mLvQuitOrder.setAdapter(mAdapter);
            mAdapter.setRefund(this);
            mAdapter.setRefuse(this);
            // mLvQuitOrder.setOnItemClickListener(this);
            if (null != mRefundHint) {
                mRefundHint.refundHint(list.size());
            }
        }
    }

    @Override
    public void getQuitOrderFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrQuit, failMsg);
        }
    }

    @Override
    public void refund(int position, int item) {
        LogUtils.d("refund", position + "---" + item);
        isRefunded = true;
        groupId = position;
        itemId = item;
        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("请输入操作密码")
                .setEditHint("操作密码")
                .setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setNegativeButton("取消", null)
                .setConfirmButton("确定", QuitFragment.this);
        mAlert.show();
    }

    @Override
    public void refuseRefund(int position, int item) {
        isRefunded = false;
        groupId = position;
        itemId = item;
        mAlert = new MyAlertDialog(mActivity).builder()
                .setTitle("请输入忽略理由")
                .setEditHint("忽略理由")
                .setNegativeButton("取消", null)
                .setConfirmButton("确定", QuitFragment.this);
        mAlert.show();
        LogUtils.d("refuseRefund", position + "---" + item);
    }

    @Override
    public void refundSuccess(DataBean bean) {
        dismissDialog();
        ViewUtils.showSnack(mPtrQuit, "退款成功");
        mPtrQuit.autoRefresh(true);
    }

    @Override
    public void refundFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrQuit, failMsg);
        }
    }

    @Override
    public void refuseRefundSuccess(DataBean bean) {
        dismissDialog();
        ViewUtils.showSnack(mPtrQuit, "拒绝退款");
        mPtrQuit.autoRefresh(true);
    }

    @Override
    public void refuseRefundFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrQuit, failMsg);
        }
    }

    @Override
    public void onEditText(String text) {
        mAlert.dismiss();
        mAlert = null;
        QuitOrderBean orderBean = (QuitOrderBean) mAdapter.getItem(groupId);
        RefundBean refund = null;
        if (null != orderBean) {
            refund = orderBean.getRefund_applications().get(itemId);
        }
        if (isRefunded) {
            if (null != refund) {
                showDialog("加载中...");
                mPresenter.refund(String.valueOf(refund.getId()), text);
            }
        } else {
            if (null != refund) {
                showDialog("加载中...");
                mPresenter.refuseRefund(String.valueOf(refund.getId()), text);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        QuitOrderBean rb = (QuitOrderBean) mAdapter.getItem(i);
        Bundle data = new Bundle();
        data.putParcelable(Constants.ORDER_QUIT_DETAIL, rb);
        ActivityUtils.startActivityForResult(this, QuitOrderDetailActivity.class, data, REQUEST_QUIT_ORDER_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QUIT_ORDER_DETAIL && resultCode == Activity.RESULT_OK) {
            mPtrQuit.autoRefresh(true);
        }
    }

    public interface IRefundHint {
        void refundHint(int count);
    }
}
