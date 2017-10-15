package com.linmama.dinning.goods.search;

import com.linmama.dinning.bean.DataBean;
import com.linmama.dinning.bean.SarchItemBean;

/**
 * Created by jingkang on 2017/3/16
 */

public class SearchCategoryContract {
    public interface SearchCategoryView {
        String getSearchKeyword();

        void getSearchCategorySuccess(SarchItemBean bean);

        void getSearchCategoryFail(String failMsg);
    }

    public interface OnItemView {
        void onItemSuccess(DataBean bean, String itemId);

        void onItemFail(String failMsg);
    }

    public interface OffItemView {
        void offItemSuccess(DataBean bean, String itemId);

        void offItemFail(String failMsg);
    }

    public interface SearchCategoryPresenter {
        void getSearchCategory(String keyword);
    }

    public interface OnItemPresenter {
        void onItem(String op_flag, String item_id);
    }

    public interface OffItemPresenter {
        void offItem(String op_flag, String item_id);
    }
}
