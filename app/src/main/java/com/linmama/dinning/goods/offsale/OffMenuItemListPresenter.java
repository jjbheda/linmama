package com.linmama.dinning.goods.offsale;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.goods.item.MenuItemBean;
import com.linmama.dinning.goods.item.MenuItemResultsBean;
import com.linmama.dinning.goods.model.OnItemModel;
import com.linmama.dinning.mvp.IModel;
import com.linmama.dinning.base.BasePresenter;
import com.linmama.dinning.goods.model.OffMenuItemListModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class OffMenuItemListPresenter extends BasePresenter<OffSaleFragment> implements
        OffMenuItemListContract.OffMenuItemPresenter, OffMenuItemListContract.OnItemPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new OffMenuItemListModel(), new OnItemModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("OffMenuItemList", models[0]);
        map.put("OnItem", models[1]);
        return map;
    }

    @Override
    public void getOffMenu() {
        if (null == getIView())
            return;
        ((OffMenuItemListModel) getiModelMap().get("OffMenuItemList")).getOffMenu(new OffMenuItemListModel.OffMenuItemListHint() {
            @Override
            public void successOffMenuItemList(List<MenuItemResultsBean> beans) {
                if (null == getIView())
                    return;
                getIView().offMenuItemSuccess(beans);
            }

            @Override
            public void failOffMenuItemList(String str) {
                if (null == getIView())
                    return;
                getIView().offMenuItemFail(str);
            }
        });
    }

    @Override
    public void onItem(String op_flag, String item_id) {
        if (null == getIView())
            return;
        ((OnItemModel) getiModelMap().get("OnItem")).onItem(op_flag, item_id,
                new OnItemModel.OnItemHint() {
                    @Override
                    public void successOnItem(DataBean bean, String itemId) {
                        if (null == getIView())
                            return;
                        getIView().onItemSuccess(bean, itemId);
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
