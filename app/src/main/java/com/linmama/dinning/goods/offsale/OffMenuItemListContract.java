package com.linmama.dinning.goods.offsale;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.goods.onsale.ShopItemBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class OffMenuItemListContract {
    public interface OffMenuItemView {
        void offMenuItemSuccess(List<ShopItemBean> bean);

        void offMenuItemFail(String failMsg);
    }

    public interface OnItemView {
        void onItemSuccess(String msg);

        void onItemFail(String failMsg);
    }

    public interface OffMenuItemPresenter {
        void getOffMenu();
    }

    public interface OnItemPresenter {
        void onItem(String item_id);
    }
}
