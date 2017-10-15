package com.xcxid.dinning.goods.category;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryBean {
    private int count;
    private String next;
    private String previous;
    private List<MenuCategoryResultsBean> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<MenuCategoryResultsBean> getResults() {
        return results;
    }

    public void setResults(List<MenuCategoryResultsBean> results) {
        this.results = results;
    }
}
