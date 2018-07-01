package com.linmama.dinning.goods.onsale;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.adapter.TakingOrderAdapter;
import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.MenuCategoryAdapter;
import com.linmama.dinning.adapter.OnSaleItemAdapter;
import com.linmama.dinning.utils.LogUtils;
import com.linmama.dinning.widget.GetMoreListView;

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

public class OnSaleFragment extends BasePresenterFragment<MenuCategoryPresenter> implements
        MenuCategoryContract.MenuCategoryView, MenuCategoryContract.OnSellMenuItemView,GetMoreListView.OnGetMoreListener,
        OnSaleItemAdapter.OnOffItem, MenuCategoryContract.OffItemView {
    @BindView(R.id.tabWidget)
    ListView mTabWidget;

    @BindView(R.id.ptr_onsale)
    PtrClassicFrameLayout preOnSale;
    @BindView(R.id.listView)
    GetMoreListView mListView;
    @BindView(R.id.content)
    RelativeLayout mContent;
    private OnItemClickListener mTabClickListener;
    private MenuCategoryAdapter mCategorydapter;
    private OnSaleItemAdapter mSaleAdapter;
    private TextView lastSelectView;
    private IOffSaleNotify mOffSaleNotify;

    private List<ShopItemBean> mResults = new ArrayList<>();
    private int currentPage = 1;
    private int last_page = 1;
    private int currentMenuId = 0;

    @Override
    protected MenuCategoryPresenter loadPresenter() {
        return new MenuCategoryPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_onsale;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        mTabClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtils.d("onItemSelected", String.valueOf(i));
                showDialog("加载中...");
                MenuCategoryBean bean = (MenuCategoryBean)mCategorydapter.getItem(i);
                mPresenter.getOnSellMenu(1,bean.id);
                currentMenuId = bean.id;
                itemBackChanged(view);
            }
        };


        mListView.setOnGetMoreListener(this);
        preOnSale.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (null != mPresenter) {
                    mResults.clear();
                    currentPage = 1;
                    mPresenter.getOnSellMenu(1,currentMenuId);
                }
            }
        });
    }

    private void itemBackChanged(View view) {
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        if (null == tvName || tvName == lastSelectView) {
            return;
        }
        if (null != lastSelectView) {
            lastSelectView.setBackgroundColor(getResources().getColor(R.color.colorMenuTabBg));
        }
        tvName.setBackgroundColor(Color.WHITE);
        lastSelectView = tvName;
    }

    @Override
    protected void initData() {
        mPresenter.getMenuCategory();
    }

    public void refresh() {
        mPresenter.getMenuCategory();
    }

    @Override
    public void menuCategorySuccess(List<MenuCategoryBean> beans) {
        if (null != beans && beans.size() > 0) {
            List<MenuCategoryBean> results = new ArrayList<>();
            results.addAll(beans);
            mCategorydapter = new MenuCategoryAdapter(mActivity, results);
            mTabWidget.setAdapter(mCategorydapter);
            mTabWidget.setOnItemClickListener(mTabClickListener);
            mPresenter.getOnSellMenu(currentPage,currentMenuId);
        }
    }

    @Override
    public void menuCategoryFail(String failMsg) {
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mContent, failMsg);
        }
    }

    @Override
    public void sellMenuItemSuccess(ShopTotalBean bean) {
        dismissDialog();
        if (currentPage == 1 && preOnSale.isRefreshing()) {
            preOnSale.refreshComplete();
        }
        if (currentPage == 1 && !ViewUtils.isListEmpty(mResults)) {
            mResults.clear();
        }
        last_page = bean.last_page;

        if (null != bean) {
            LogUtils.d("getTakingOrderSuccess", bean.toString());
            List<ShopItemBean> results = bean.data;
            mResults.addAll(results);
            if (currentPage == 1 && results.size() == 0) {
                preOnSale.getHeader().setVisibility(View.GONE);
                if (mSaleAdapter != null) {
                    mSaleAdapter.notifyDataSetChanged();
                }
                return;
            }
            if (null == mSaleAdapter) {
                mSaleAdapter = new OnSaleItemAdapter(mActivity, mResults);
                mSaleAdapter.setOnOffItem(this);
                mListView.setAdapter(mSaleAdapter);
            } else {
                mSaleAdapter.notifyDataSetChanged();
                if (currentPage > 1) {
                    mListView.getMoreComplete();
                }

                if (currentPage == last_page) {
                    mListView.setNoMore();
                }
            }
        }
    }

    @Override
    public void sellMenuItemFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mContent, failMsg);
        }
    }
    String currentOffItemId = "";
    @Override
    public void offItem(ShopItemBean bean) {
        if (null != bean) {
            showDialog("加载中...");
            mPresenter.offItem(bean.getId());
            currentOffItemId = bean.getId();
        }
    }

    @Override
    public void offItemSuccess(String bean) {
        dismissDialog();
        ViewUtils.showSnack(mContent, bean);
        for (int i = 0; i < mSaleAdapter.getCount(); i++) {
            ShopItemBean item = (ShopItemBean)mSaleAdapter.getItem(i);
            if (currentOffItemId .equals(item.getId())) {
                mSaleAdapter.removeItem(i);
                if (null != mOffSaleNotify) {
                    mOffSaleNotify.offSaleNotify();
                }
                return;
            }
        }
    }

    @Override
    public void offItemFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mContent, failMsg);
        }
    }

    public void setOffSaleNotify(IOffSaleNotify offSaleNotify) {
        this.mOffSaleNotify = offSaleNotify;
    }

    public interface IOffSaleNotify {
        void offSaleNotify();
    }
    @Override
    public void onGetMore() {
        if (currentPage == last_page) {
            mListView.setNoMore();
            return;
        }
        currentPage++;
        mPresenter.getOnSellMenu(currentPage,currentMenuId);
    }
}
