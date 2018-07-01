package com.linmama.dinning.goods.onsale;

import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.goods.model.MenuCategoryListModel;
import com.linmama.dinning.goods.model.OnSellMenuItemListModel;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.goods.model.OffItemModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryPresenter extends BasePresenter<OnSaleFragment> implements
        MenuCategoryContract.MenuCategoryPresenter, MenuCategoryContract.OnSellMenuItemPresenter,
        MenuCategoryContract.OffItemPresenter{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new MenuCategoryListModel(), new OnSellMenuItemListModel(),
                new OffItemModel(), new MenuCategoryListModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("MenuCategory", models[0]);
        map.put("OnSellMenuItem", models[1]);
        map.put("OffItem", models[2]);
        map.put("OnAllSellMenuItem", models[3]);
        return map;
    }

    @Override
    public void getMenuCategory() {
        if (null == getIView())
            return;
        ((MenuCategoryListModel) getiModelMap().get("MenuCategory")).getMenuCategory(
                new MenuCategoryListModel.MenuCategoryListHint() {
                    @Override
                    public void successMenuCategoryList(List<MenuCategoryBean> beans) {
                        if (null == getIView())
                            return;
                        getIView().menuCategorySuccess(beans);
                    }

                    @Override
                    public void failMenuCategoryList(String str) {
                        if (null == getIView())
                            return;
                        getIView().menuCategoryFail(str);
                    }
                });
    }

    @Override
    public void getOnSellMenu(int page,int menuCategory) {
        if (null == getIView())
            return;
        ((OnSellMenuItemListModel) getiModelMap().get("OnSellMenuItem")).getProductlistById(page,menuCategory,
                new OnSellMenuItemListModel.OnSellMenuItemListHint() {
                    @Override
                    public void successOnSellMenuItemList(ShopTotalBean beans) {
                        if (null == getIView())
                            return;
                        getIView().sellMenuItemSuccess(beans);
                    }

                    @Override
                    public void failOnSellMenuItemList(String str) {
                        if (null == getIView())
                            return;
                        getIView().sellMenuItemFail(str);
                    }
                });
    }

    @Override
    public void offItem(final String id) {
        if (null == getIView())
            return;
        ((OffItemModel) getiModelMap().get("OffItem")).offItem(id,
                new OffItemModel.OffItemHint() {
                    @Override
                    public void successOffItem(String bean) {
                        if (null == getIView())
                            return;
                        getIView().offItemSuccess(bean);
                    }

                    @Override
                    public void failOffItem(String failMsg) {
                        if (null == getIView())
                            return;
                        getIView().offItemFail(failMsg);
                    }
                });
    }
}
