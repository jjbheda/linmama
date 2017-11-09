package com.linmama.dinning.shop.account.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.text.TextUtils;
import android.widget.TextView;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.bean.SingleAccountBean;
import com.linmama.dinning.bean.SingleAccountItemBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.shop.account.AccountAdapter;
import com.linmama.dinning.shop.account.AccountContract;
import com.linmama.dinning.shop.account.AccountPresenter;
import com.linmama.dinning.shop.bean.ShopBean;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.widget.GetMoreListView;
import com.linmama.dinning.widget.header.WindmillHeader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.linmama.dinning.base.BaseModel.httpService;

/**
 * Created by jiangjingbo on 2017/11/8.
 */

public class AccountDetailFragment extends BasePresenterFragment<AccountPresenter> implements AccountContract.AccountView, GetMoreListView.OnGetMoreListener {
    private List<SingleAccountItemBean> mResults = new ArrayList<>();
    private String date = "";
    private int currentPage = 1;
    private int last_page = 1;
    SingleAccountAdapter mAdapter;

    @BindView(R.id.turnover_tag)
    TextView turnover_tag;
    @BindView(R.id.today_turnover)
    TextView today_turnover;
    @BindView(R.id.flag_turnover)
    TextView flag_turnover;
    @BindView(R.id.normal_item)
    TextView normal_item;
    @BindView(R.id.redress_item)
    TextView redress_item;

    @BindView(R.id.single_account_lv)
    GetMoreListView mLvAccount;
    @BindView(R.id.single_account_ft)
    PtrClassicFrameLayout mPtrAccount;

    int type;

    @Override
    public void AccountGetSuccess(List<AccountBeanItem> beans) {
//        dismissDialog();
//        if (currentPage == 1 && mPtrAccount.isRefreshing()) {
//            mPtrAccount.refreshComplete();
//        }
//        if (currentPage == 1) {
//            mResults.clear();
//        }
//
//        LogUtils.d("getAccountSuccess", beans.toString());
//        List<AccountBeanItem> results = beans;
//        mResults.addAll(results);
//        if (null == mAdapter) {
//            mAdapter = new AccountAdapter(mActivity, mResults);
//            mLvAccount.setAdapter(mAdapter);
//        } else {
//            mAdapter.notifyDataSetChanged();
//            if (currentPage > 1) {
//                mLvAccount.getMoreComplete();
//            }
//        }
//        if (beans.size() <= 20) {
//            mLvAccount.setNoMore();
//            last_page = currentPage;
//        }
    }

    @Override
    public void AccountGetFail(String failMsg) {
//        dismissDialog();
//        if (!TextUtils.isEmpty(failMsg)) {
//            ViewUtils.showSnack(mPtrAccount, failMsg);
//        }
    }

    @Override
    protected AccountPresenter loadPresenter() {
        return new AccountPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.order_account_detail;
    }

    @Override
    protected void initView() {
//        final WindmillHeader header = new WindmillHeader(mActivity);
//        mPtrAccount.setHeaderView(header);
//        mPtrAccount.addPtrUIHandler(header);
        Bundle bundle = getArguments();
        if (bundle!=null){
            date = bundle.getString("Date","");
        }

        showDialog("加载中...");
        httpService.getBillDetailData(date)
                .compose(new CommonTransformer<AccountBeanItem>())
                .subscribe(new CommonSubscriber<AccountBeanItem>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(AccountBeanItem bean) {
                        dismissDialog();
                        if (bean != null){
                            showUI(bean);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        dismissDialog();
                    }
                });

        getNormalData();

        final WindmillHeader header = new WindmillHeader(mActivity);
        mPtrAccount.setHeaderView(header);
        mPtrAccount.addPtrUIHandler(header);
    }

    @OnClick(R.id.normal_item)
    protected void getNormalData(){
        httpService.getBillDetailListData(currentPage,date,0)
                .compose(new CommonTransformer<SingleAccountBean>())
                .subscribe(new CommonSubscriber<SingleAccountBean>(LmamaApplication.getInstance()) {
                    @Override
                    public void onNext(SingleAccountBean bean) {
                        dismissDialog();
                        if (bean != null){
                            showLtUI(bean);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        dismissDialog();
                    }
                });
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
                    getNormalData();
                }
            }
        });
    }

    @Override
    protected void initData() {
//        mPresenter.getHistoryBillQueryData(1, 0);
    }

    @Override
    public void onGetMore() {
//        if (currentPage == last_page) {
//            mLvAccount.setNoMore();
//            return;
//        }
//        currentPage++;
//        mPresenter.getHistoryBillQueryData(currentPage, 0);
    }

    private void showLtUI(SingleAccountBean bean) {
        if (currentPage == 1 && mPtrAccount.isRefreshing()) {
            mPtrAccount.refreshComplete();
        }
        if (currentPage == 1) {
            mResults.clear();
        }

        LogUtils.d("getAccountSuccess", bean.data.toString());
        List<SingleAccountItemBean> results =  bean.data;
        mResults.addAll(results);
        if (null == mAdapter) {
            mAdapter = new SingleAccountAdapter(mActivity, mResults);
            mLvAccount.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
            if (currentPage > 1) {
                mLvAccount.getMoreComplete();
            }
        }
    }


    public void showUI(AccountBeanItem bean){
        turnover_tag.setText(bean.date);
        today_turnover.setText(bean.income);
        flag_turnover.setText(bean.text);
        normal_item.setText("正常单\n"+bean.income);
        redress_item.setText("调整单\n"+bean.income_invalid);
    }
}
