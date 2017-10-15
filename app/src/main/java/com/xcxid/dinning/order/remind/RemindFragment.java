package com.xcxid.dinning.order.remind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.adapter.RemindOrderAdapter;
import com.xcxid.dinning.base.BasePresenterFragment;
import com.xcxid.dinning.bean.RemindBean;
import com.xcxid.dinning.bean.RemindResultsBean;
import com.xcxid.dinning.order.order.OrderFragment;
import com.xcxid.dinning.order.remind.detail.RemindOrderDetailActivity;
import com.xcxid.dinning.url.Constants;
import com.xcxid.dinning.utils.ActivityUtils;
import com.xcxid.dinning.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */
public class RemindFragment extends BasePresenterFragment<RemindOrderPresenter> implements
        RemindOrderAdapter.IHandleWarn, RemindOrderContract.RemindOrderView, RemindOrderContract.HandleWarnView,
        AdapterView.OnItemClickListener {
    @BindView(R.id.lvRemindOrder)
    ListView mLvRemindOrder;
    @BindView(R.id.ptr_remind)
    PtrClassicFrameLayout mPtrRemind;

    private RemindOrderAdapter mAdapter;
    private List<RemindResultsBean> mResults;
    private IRemindHint mRemindHint;
    private static final int REQUEST_REMIND_ORDER_DETAIL = 0x30;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_remind;
    }

    @Override
    protected void initView() {
        mRemindHint = (OrderFragment) getParentFragment();
        mPtrRemind.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
//                    showDialog("加载中...");
                    mPresenter.getRemindOrder();
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (null != mPresenter) {
            mPtrRemind.autoRefresh(true);
//            showDialog("加载中...");
//            mPresenter.getRemindOrder();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected RemindOrderPresenter loadPresenter() {
        return new RemindOrderPresenter();
    }

    @Override
    public void getRemindOrderSuccess(RemindBean remindBean) {
        dismissDialog();
        mPtrRemind.refreshComplete();
        if (null != remindBean && null != remindBean.getResults()) {
            mResults = remindBean.getResults();
//            mRemindHint.remindHint();
            mAdapter = new RemindOrderAdapter(mActivity, mResults);
            mAdapter.setHandleWarn(this);
        }
        if (null != mAdapter) {
            mLvRemindOrder.setAdapter(mAdapter);
            mLvRemindOrder.setOnItemClickListener(this);
        }
    }

    @Override
    public void getRemindOrderFail(String failMsg) {
        dismissDialog();
        ViewUtils.showSnack(mPtrRemind, failMsg);
    }

    @Override
    public void handleWarn(int position) {
        showDialog("加载中...");
        RemindResultsBean bean = mResults.get(position);
        mPresenter.handleWarn(String.valueOf(bean.getId()));
    }

    @Override
    public void handleWarnSuccess(String warnId) {
        dismissDialog();
        ViewUtils.showSnack(mPtrRemind, "已知晓");
        for (RemindResultsBean rb : mResults) {
            if (String.valueOf(rb.getId()).equals(warnId)) {
                mResults.remove(rb);
                if (null != mResults && null != mRemindHint) {
                    mRemindHint.remindHint();
                }
                if (null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    @Override
    public void handleWarnFail(String failMsg) {
        dismissDialog();
        ViewUtils.showSnack(mPtrRemind, failMsg);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        RemindResultsBean rb = mResults.get(i);
        Bundle data = new Bundle();
        data.putParcelable(Constants.ORDER_REMIND_DETAIL, rb);
        ActivityUtils.startActivityForResult(this, RemindOrderDetailActivity.class, data, REQUEST_REMIND_ORDER_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REMIND_ORDER_DETAIL && resultCode == Activity.RESULT_OK && null != data) {
            String warnId = data.getStringExtra(Constants.WARN_ID);
            if (!TextUtils.isEmpty(warnId) && null != mResults) {
                for (RemindResultsBean rb : mResults) {
                    if (String.valueOf(rb.getId()).equals(warnId)) {
                        mResults.remove(rb);
                        if (null != mResults && null != mRemindHint) {
                            mRemindHint.remindHint();
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

    public interface IRemindHint {
        void remindHint();
    }
}
