package com.linmama.dinning.goods.search;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linmama.dinning.bean.SearchItemResultsBean;
import com.linmama.dinning.bean.ShopSearchBean;
import com.linmama.dinning.utils.ViewUtils;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BasePresenterActivity;
import com.linmama.dinning.url.Constants;
import com.linmama.dinning.widget.ClearEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingkang on 2017/3/16
 */

public class SearchCategoryActivity extends BasePresenterActivity<SearchCategoryPresenter> implements
        SearchCategoryContract.SearchCategoryView, SearchCategoryAdapter.IOnItem, SearchCategoryAdapter.IOffItem,
        SearchCategoryContract.OffItemView, SearchCategoryContract.OnItemView{
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.etSearch)
    ClearEditText etSearch;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(android.R.id.list)
    ListView list;
    @BindView(android.R.id.empty)
    TextView empty;

    private boolean isOn = false;
    private boolean isOff = false;
    private SearchCategoryAdapter mAdapter;

    @Override
    protected SearchCategoryPresenter loadPresenter() {
        return new SearchCategoryPresenter();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_search_item;
    }

    @Override
    protected void initView() {
//        list.setEmptyView(empty);
    }

    @Override
    protected void initListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editable.toString().length();
                if (len > 0) {
                    tvSearch.setEnabled(true);
                } else {
                    tvSearch.setEnabled(false);
                }
            }
        });
    }

    @OnClick(R.id.ivBack)
    public void back(View view) {
        setResult();
    }

    @OnClick(R.id.tvSearch)
    public void search(View view) {
        showDialog("搜索中...");
        mPresenter.getSearchCategory(getSearchKeyword());
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getSearchKeyword() {
        return etSearch.getText().toString();
    }

    public boolean checkNull() {
        if (TextUtils.isEmpty(getSearchKeyword().trim())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getSearchCategorySuccess(List<ShopSearchBean> beans) {
        dismissDialog();
        if (beans.size()>0) {
            mAdapter = new SearchCategoryAdapter(this,beans);
            list.setAdapter(mAdapter);
            mAdapter.setIOffItem(this);
            mAdapter.setIOnItem(this);
        } else {
            list.setEmptyView(empty);
        }
    }

    @Override
    public void getSearchCategoryFail(String failMsg) {
        dismissDialog();
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void offItem(int position) {
        ShopSearchBean bean = (ShopSearchBean) mAdapter.getItem(position);
        mPresenter.offItem(bean.id);
    }

    @Override
    public void onItem(int position) {
        ShopSearchBean bean = (ShopSearchBean) mAdapter.getItem(position);
        mPresenter.onItem(bean.id);
    }

    @Override
    public void offItemSuccess(String id,String msg) {
        ViewUtils.showSnack(content, "下架成功");
        isOff = true;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            ShopSearchBean item = (ShopSearchBean)mAdapter.getItem(i);
            if (id .equals(item.id)) {
                mAdapter.updateItem(i);
                return;
            }
        }
    }


    @Override
    public void offItemFail(String failMsg) {
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public void onItemSuccess(String id,String msg) {
        isOn = true;
        ViewUtils.showSnack(content, "上架成功");
        for (int i = 0; i < mAdapter.getCount(); i++) {
            ShopSearchBean item = (ShopSearchBean)mAdapter.getItem(i);
            if (id .equals(item.id) ) {
                mAdapter.updateItem(i);
                return;
            }
        }
    }


    @Override
    public void onItemFail(String failMsg) {
        if (!TextUtils.isEmpty(failMsg)) {
            ViewUtils.showSnack(content, failMsg);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            setResult();
        }
        return false;
    }

    private void setResult() {
        if (isOn || isOff) {
            Intent data = new Intent();
            data.putExtra(Constants.CATEGORY_SEARCH_ON, isOn);
            data.putExtra(Constants.CATEGORY_SEARCH_OFF, isOff);
            setResult(RESULT_OK, data);
        }
        finish();
    }
}
