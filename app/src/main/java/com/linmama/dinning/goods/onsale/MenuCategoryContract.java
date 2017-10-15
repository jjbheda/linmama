package com.linmama.dinning.goods.onsale;

import com.linmama.dinning.goods.category.MenuCategoryBean;
import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.goods.item.MenuItemBean;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryContract {
    public interface MenuCategoryView {
        void menuCategorySuccess(MenuCategoryBean bean);

        void menuCategoryFail(String failMsg);
    }

    public interface OnSellMenuItemView {
        void sellMenuItemSuccess(MenuItemBean bean);

        void sellMenuItemFail(String failMsg);
    }

    public interface OffItemView {
        void offItemSuccess(DataBean bean, String itemId);

        void offItemFail(String failMsg);
    }

    public interface MenuCategoryPresenter {
        void getMenuCategory();
    }

    public interface OnSellMenuItemPresenter {
        void getOnSellMenu(int menuCategory);
        void getOnAllSellMenu();
    }

    public interface OffItemPresenter {
        void offItem(String op_flag, String item_id);
    }
}
