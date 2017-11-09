package com.linmama.dinning.shop.account;

import android.text.TextUtils;

import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.header.WindmillHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jiangjingbo on 2017/11/8.
 */

public class AccountFragment extends BasePresenterFragment<AccountPresenter> implements AccountContract.AccountView, GetMoreListView.OnGetMoreListener {
    private List<AccountBeanItem> mResults = new ArrayList<>();
    private int currentPage = 1;
    private int last_page = 1;
    AccountAdapter mAdapter;

    @BindView(R.id.account_lv)
    GetMoreListView mLvAccount;
    @BindView(R.id.account_ft)
    PtrClassicFrameLayout mPtrAccount;

    int type;

    @Override
    public void AccountGetSuccess(List<AccountBeanItem> beans) {
        dismissDialog();
        if (currentPage == 1 && mPtrAccount.isRefreshing()) {
            mPtrAccount.refreshComplete();
        }
        if (currentPage == 1) {
            mResults.clear();
        }

        LogUtils.d("getAccountSuccess", beans.toString());
        List<AccountBeanItem> results = beans;
        mResults.addAll(results);
        if (null == mAdapter) {
            mAdapter = new AccountAdapter(mActivity, mResults);
            mLvAccount.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
            if (currentPage > 1) {
                mLvAccount.getMoreComplete();
            }
        }
        if (beans.size() <= 20) {
            mLvAccount.setNoMore();
            last_page = currentPage;
        }
    }

    @Override
    public void AccountGetFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mPtrAccount, failMsg);
        }
    }

    @Override
    protected AccountPresenter loadPresenter() {
        return new AccountPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.my_bill_v2;
    }

    @Override
    protected void initView() {
        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrAccount.setHeaderView(header);
        mPtrAccount.addPtrUIHandler(header);
    }

    @Override
    protected void initListener() {
        mLvAccount.setOnGetMoreListener(this);
        mPtrAccount.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    currentPage = 1;
                    mPresenter.getHistoryBillQueryData(currentPage, 0);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getHistoryBillQueryData(1, 0);
    }

    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            mLvAccount.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getHistoryBillQueryData(currentPage, 0);
    }
}
