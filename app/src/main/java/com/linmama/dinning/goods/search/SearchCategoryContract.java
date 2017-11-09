package com.linmama.dinning.goods.search;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.SarchItemBean;
import com.linmama.dinning.bean.ShopSearchBean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/16
 */

public class SearchCategoryContract {
    public interface SearchCategoryView {
        String getSearchKeyword();

        void getSearchCategorySuccess(List<ShopSearchBean> beans);

        void getSearchCategoryFail(String failMsg);
    }

    public interface OnItemView {
        void onItemSuccess(int id,String msg);

        void onItemFail(String failMsg);
    }

    public interface OffItemView {
        void offItemSuccess(int id,String msg);

        void offItemFail(String failMsg);
    }

    public interface SearchCategoryPresenter {
        void getSearchCategory(String keyword);
    }

    public interface OnItemPresenter {
        void onItem(int item_id);
    }

    public interface OffItemPresenter {
        void offItem(int item_id);
    }
}
