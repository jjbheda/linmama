package com.linmama.dinning.goods.onsale;

import com.linmama.dinning.goods.category.MenuCategoryBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryContract {
    public interface MenuCategoryView {
        void menuCategorySuccess(List<MenuCategoryBean> beans);

        void menuCategoryFail(String failMsg);
    }

    public interface OnSellMenuItemView {
        void sellMenuItemSuccess(ShopTotalBean bean);

        void sellMenuItemFail(String failMsg);
    }

    public interface OffItemView {
        void offItemSuccess(String msg);

        void offItemFail(String failMsg);
    }

    public interface MenuCategoryPresenter {
        void getMenuCategory();
    }

    public interface OnSellMenuItemPresenter {
        void getOnSellMenu(int page,int menuCategory);
    }

    public interface OffItemPresenter {
        void offItem(int id);
    }
}
