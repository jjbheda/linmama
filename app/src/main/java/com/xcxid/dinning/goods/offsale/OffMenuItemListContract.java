package com.xcxid.dinning.goods.offsale;

import com.xcxid.dinning.bean.DataBean;
import com.xcxid.dinning.goods.item.MenuItemBean;

/**
 * Created by jingkang on 2017/3/11
 */

public class OffMenuItemListContract {
    public interface OffMenuItemView {
        void offMenuItemSuccess(MenuItemBean bean);

        void offMenuItemFail(String failMsg);
    }

    public interface OnItemView {
        void onItemSuccess(DataBean bean, String itemId);

        void onItemFail(String failMsg);
    }

    public interface OffMenuItemPresenter {
        void getOffMenu();
    }

    public interface OnItemPresenter {
        void onItem(String op_flag, String item_id);
    }
}
