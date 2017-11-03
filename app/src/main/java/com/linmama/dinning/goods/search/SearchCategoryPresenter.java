package com.linmama.dinning.goods.search;

import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.goods.model.OffItemModel;
import com.linmama.dinning.goods.model.OnItemModel;

import java.util.HashMap;

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
                    public void successSearchCategory(SarchItemBean bean) {
                        if (null == getIView())
                            return;
                        getIView().getSearchCategorySuccess(bean);
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
    public void offItem(String op_flag, String item_id) {
        if (null == getIView())
            return;
//        ((OffItemModel) getiModelMap().get("OffItem")).offItem(op_flag, item_id,
//                new OffItemModel.OffItemHint() {
//                    @Override
//                    public void successOffItem(String msg) {
//                        if (null == getIView())
//                            return;
//                        getIView().offItemSuccess(msg);
//                    }
//
//                    @Override
//                    public void failOffItem(String failMsg) {
//                        if (null == getIView())
//                            return;
//                        getIView().offItemFail(failMsg);
//                    }
//                });
    }

    @Override
    public void onItem(String op_flag, String item_id) {
        if (null == getIView())
            return;
//        ((OnItemModel) getiModelMap().get("OnItem")).onItem(op_flag, item_id,
//                new OnItemModel.OnItemHint() {
//                    @Override
//                    public void successOnItem(DataBean bean, String itemId) {
//                        if (null == getIView())
//                            return;
//                        getIView().onItemSuccess(bean, itemId);
//                    }
//
//                    @Override
//                    public void failOnItem(String failMsg) {
//                        if (null == getIView())
//                            return;
//                        getIView().onItemFail(failMsg);
//                    }
//                });
    }
}
