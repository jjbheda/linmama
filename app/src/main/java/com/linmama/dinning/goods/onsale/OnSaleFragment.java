package com.linmama.dinning.goods.onsale;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linmama.dinning.base.BasePresenterFragment;
import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.goods.item.MenuItemResultsBean;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.R;
import com.linmama.dinning.adapter.MenuCategoryAdapter;
import com.linmama.dinning.adapter.OnSaleItemAdapter;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.goods.category.MenuCategoryResultsBean;
import com.linmama.dinning.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jingkang on 2017/2/25
 * for company xcxid
 */

public class OnSaleFragment extends BasePresenterFragment<MenuCategoryPresenter> implements
        MenuCategoryContract.MenuCategoryView, MenuCategoryContract.OnSellMenuItemView,
        OnSaleItemAdapter.OnOffItem, MenuCategoryContract.OffItemView {
    @BindView(R.id.tabWidget)
    ListView mTabWidget;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.content)
    RelativeLayout mContent;
    private OnItemClickListener mTabClickListener;
    private MenuCategoryAdapter mCategorydapter;
    private OnSaleItemAdapter mSaleAdapter;
    private TextView lastSelectView;
    private IOffSaleNotify mOffSaleNotify;

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
                if (i == 0) {
                    mPresenter.getOnAllSellMenu();
                } else {
                    mPresenter.getOnSellMenu(((MenuCategoryResultsBean) mCategorydapter.getItem(i)).getId());
                }
                itemBackChanged(view);
            }
        };
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
        if (null != beans && null != beans) {
            List<MenuCategoryBean> results = new ArrayList<>();
            results.addAll(beans);
            mCategorydapter = new MenuCategoryAdapter(mActivity, results);
            mTabWidget.setAdapter(mCategorydapter);
            mTabWidget.setOnItemClickListener(mTabClickListener);
            mPresenter.getOnAllSellMenu();
        }
    }

    @Override
    public void menuCategoryFail(String failMsg) {
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mContent, failMsg);
        }
    }

    @Override
    public void sellMenuItemSuccess(List<MenuItemResultsBean> beans) {
        dismissDialog();
        if (null != beans ) {
            mSaleAdapter = new OnSaleItemAdapter(mActivity, beans);
            mListView.setAdapter(mSaleAdapter);
            mSaleAdapter.setOnOffItem(this);
        }
    }

    @Override
    public void sellMenuItemFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(mContent, failMsg);
        }
    }

    @Override
    public void offItem(int position) {
        MenuItemResultsBean bean = (MenuItemResultsBean) mSaleAdapter.getItem(position);
        if (null != bean) {
            showDialog("加载中...");
            mPresenter.offItem("2", String.valueOf(bean.getId()));
        }
    }

    @Override
    public void offItemSuccess(DataBean bean, String itemId) {
        dismissDialog();
        ViewUtils.showSnack(mContent, "下架成功");
        for (int i = 0; i < mSaleAdapter.getCount(); i++) {
            MenuItemResultsBean item = (MenuItemResultsBean)mSaleAdapter.getItem(i);
            if (itemId.equals(String.valueOf(item.getId()))) {
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

}
