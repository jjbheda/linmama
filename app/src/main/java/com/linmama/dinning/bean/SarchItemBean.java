package com.linmama.dinning.bean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/16
 */

public class SarchItemBean {
    private int count;
    private String next;
    private String previous;
    private List<SearchItemResultsBean> results;

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

    public List<SearchItemResultsBean> getResults() {
        return results;
    }

    public void setResults(List<SearchItemResultsBean> results) {
        this.results = results;
    }
}
