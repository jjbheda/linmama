package com.xcxid.dinning.bean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/10
 */

public class RemindBean {
    private int count;
    private String next;
    private String previous;
    private List<RemindResultsBean> results;

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

    public List<RemindResultsBean> getResults() {
        return results;
    }

    public void setResults(List<RemindResultsBean> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "RemindBean{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}
