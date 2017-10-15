package com.xcxid.dinning.goods.item;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuItemBean {
    private int count;
    private String next;
    private String previous;
    private List<MenuItemResultsBean> results;

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

    public List<MenuItemResultsBean> getResults() {
        return results;
    }

    public void setResults(List<MenuItemResultsBean> results) {
        this.results = results;
    }
}
