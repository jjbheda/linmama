package com.linmama.dinning.goods.onsale;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.goods.model.OnSellMenuItemListModel;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.goods.item.MenuItemBean;
import com.linmama.dinning.goods.model.MenuCategoryListModel;
import com.linmama.dinning.goods.model.OffItemModel;
import com.linmama.dinning.goods.model.OnAllSellMenuItemListModel;

import java.util.HashMap;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryPresenter extends BasePresenter<OnSaleFragment> implements
        MenuCategoryContract.MenuCategoryPresenter, MenuCategoryContract.OnSellMenuItemPresenter,
        MenuCategoryContract.OffItemPresenter{
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new MenuCategoryListModel(), new OnSellMenuItemListModel(),
                new OffItemModel(), new OnAllSellMenuItemListModel());
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
        ((MenuCategoryListModel) getiModelMap().get("MenuCategory")).getMenuCategoryList(
                new MenuCategoryListModel.MenuCategoryListHint() {
                    @Override
                    public void successMenuCategoryList(MenuCategoryBean bean) {
                        if (null == getIView())
                            return;
                        getIView().menuCategorySuccess(bean);
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
    public void getOnSellMenu(int menuCategory) {
        if (null == getIView())
            return;
        ((OnSellMenuItemListModel) getiModelMap().get("OnSellMenuItem")).getOnSellMenu(menuCategory,
                new OnSellMenuItemListModel.OnSellMenuItemListHint() {
                    @Override
                    public void successOnSellMenuItemList(MenuItemBean bean) {
                        if (null == getIView())
                            return;
                        getIView().sellMenuItemSuccess(bean);
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
    public void getOnAllSellMenu() {
        if (null == getIView())
            return;
        ((OnAllSellMenuItemListModel) getiModelMap().get("OnAllSellMenuItem")).getOnSellMenu(
                new OnAllSellMenuItemListModel.OnAllSellMenuItemListHint() {
                    @Override
                    public void successOnAllSellMenuItemList(MenuItemBean bean) {
                        if (null == getIView())
                            return;
                        getIView().sellMenuItemSuccess(bean);
                    }

                    @Override
                    public void failOnAllSellMenuItemList(String str) {
                        if (null == getIView())
                            return;
                        getIView().sellMenuItemFail(str);
                    }
                });
    }

    @Override
    public void offItem(String op_flag, final String item_id) {
        if (null == getIView())
            return;
        ((OffItemModel) getiModelMap().get("OffItem")).offItem(op_flag, item_id,
                new OffItemModel.OffItemHint() {
                    @Override
                    public void successOffItem(DataBean bean, String itemId) {
                        if (null == getIView())
                            return;
                        getIView().offItemSuccess(bean, item_id);
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
