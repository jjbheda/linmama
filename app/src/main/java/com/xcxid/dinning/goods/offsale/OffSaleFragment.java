package com.xcxid.dinning.goods.offsale;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.adapter.OffSaleItemAdapter;
import com.xcxid.dinning.base.BasePresenterFragment;
import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.goods.item.MenuItemBean;
import com.xcxid.dinning.goods.item.MenuItemResultsBean;
import com.xcxid.dinning.utils.LogUtils;
import com.xcxid.dinning.utils.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class OffSaleFragment extends BasePresenterFragment<OffMenuItemListPresenter> implements
        OffMenuItemListContract.OffMenuItemView, OffSaleItemAdapter.OnOnItem,
        OffMenuItemListContract.OnItemView {
    @BindView(R.id.btnOnAll)
    Button mBtnOnSale;
    @BindView(R.id.offListView)
    ListView mOffListView;
    @BindView(R.id.content)
    LinearLayout mContent;

    private OffSaleItemAdapter mAdapter;
    private IOnSaleNotify mOnSaleNotify;
    private boolean isOnAll = false;

    @Override
    protected OffMenuItemListPresenter loadPresenter() {
        return new OffMenuItemListPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_offsale;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        mPresenter.getOffMenu();
    }

    public void refresh() {
        mPresenter.getOffMenu();
    }

    @Override
    public void offMenuItemSuccess(MenuItemBean bean) {
        if (null != bean && null != bean.getResults()) {
            mAdapter = new OffSaleItemAdapter(mActivity, bean.getResults());
            mOffListView.setAdapter(mAdapter);
            mAdapter.setOnOnItem(this);
        }
    }

    @Override
    public void offMenuItemFail(String failMsg) {
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mContent, failMsg);
        }
    }

    @OnClick(R.id.btnOnAll)
    public void onAllItems(View view) {
        LogUtils.d("onAllItems", "onAllItems");
        showDialog("加载中...");
        isOnAll = true;
        mPresenter.onItem("1", "0");
    }

    @Override
    public void onItem(int position) {
        MenuItemResultsBean bean = (MenuItemResultsBean) mAdapter.getItem(position);
        if (null != bean) {
            showDialog("加载中...");
            isOnAll = false;
            mPresenter.onItem("1", String.valueOf(bean.getId()));
        }
    }

    @Override
    public void onItemSuccess(DataBean bean, String itemId) {
        dismissDialog();
        if (isOnAll) {
            mAdapter.clearItems();
            if (null != mOnSaleNotify) {
                mOnSaleNotify.onSaleNotify();
            }
        } else {
            ViewUtils.showSnack(mContent, "上架成功");
            for (int i = 0; i < mAdapter.getCount(); i++) {
                MenuItemResultsBean item = (MenuItemResultsBean) mAdapter.getItem(i);
                if (itemId.equals(String.valueOf(item.getId()))) {
                    mAdapter.removeItem(i);
                    if (null != mOnSaleNotify) {
                        mOnSaleNotify.onSaleNotify();
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onItemFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mContent, failMsg);
        }
    }

    public void setOnSaleNotify(IOnSaleNotify onSaleNotify) {
        this.mOnSaleNotify = onSaleNotify;
    }

    public interface IOnSaleNotify {
        void onSaleNotify();
    }
}
