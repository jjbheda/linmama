package com.linmama.dinning.bean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleRankBean {
    private int count;
    private String next;
    private String previous;
    private List<SaleRankResultsBean> results;

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

    public List<SaleRankResultsBean> getResults() {
        return results;
    }

    public void setResults(List<SaleRankResultsBean> results) {
        this.results = results;
    }
}
