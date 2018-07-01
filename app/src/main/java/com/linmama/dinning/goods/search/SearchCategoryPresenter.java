package com.linmama.dinning.goods.search;

import com.linmama.dinning.bean.ShopSearchBean;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.goods.model.OffItemModel;
import com.linmama.dinning.goods.model.OnItemModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/16
 */

public class SearchCategoryPresenter extends BasePresenter<SearchCategoryActivity> implements
        SearchCategoryContract.SearchCategoryPresenter, SearchCategoryContract.OffItemPresenter,
        SearchCategoryContract.OnItemPresenter{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new SearchCategoryModel(), new OnItemModel(), new OffItemModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("SearchCategory", models[0]);
        map.put("OnItem", models[1]);
        map.put("OffItem", models[2]);
        return map;
    }

    @Override
    public void getSearchCategory(String keyword) {
        if (getIView() == null)
            return;
        if (getIView().checkNull())
            return;
        ((SearchCategoryModel) getiModelMap().get("SearchCategory")).searchItem(keyword,
                new SearchCategoryModel.SearchCategoryHint() {
                    @Override
                    public void successSearchCategory(List<ShopSearchBean> beans) {
                        if (null == getIView())
                            return;
                        getIView().getSearchCategorySuccess(beans);
                    }

                    @Override
                    public void failSearchCategory(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().getSearchCategoryFail(failMsg);
                    }
                });
    }

    @Override
    public void offItem(final String item_id) {
        if (null == getIView())
            return;
        ((OffItemModel) getiModelMap().get("OffItem")).offItem( item_id,
                new OffItemModel.OffItemHint() {
                    @Override
                    public void successOffItem(String msg) {
                        if (null == getIView())
                            return;
                        getIView().offItemSuccess(item_id,msg);
                    }

                    @Override
                    public void failOffItem(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().offItemFail(failMsg);
                    }
                });
    }

    @Override
    public void onItem(final String item_id) {
        if (null == getIView())
            return;
        ((OnItemModel) getiModelMap().get("OnItem")).onItem(item_id,
                new OnItemModel.OnItemHint() {
                    @Override
                    public void successOnItem(String msg) {
                        if (null == getIView())
                            return;
                        getIView().onItemSuccess(item_id,msg);
                    }

                    @Override
                    public void failOnItem(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().onItemFail(failMsg);
                    }
                });
    }
}
