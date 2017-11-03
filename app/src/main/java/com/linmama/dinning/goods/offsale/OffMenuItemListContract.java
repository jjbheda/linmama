package com.linmama.dinning.goods.offsale;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.goods.item.MenuItemBean;
import com.linmama.dinning.goods.item.MenuItemResultsBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class OffMenuItemListContract {
    public interface OffMenuItemView {
        void offMenuItemSuccess(List<MenuItemResultsBean> bean);

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
