package com.linmama.dinning.bean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/5
 */

public class CompleteOrderBean {
    private int count;
    private String next;
    private int previous_page_number;
    private int current_number;
    private String previous;
    private int next_page_number;
    private int page_size;
    private List<ResultsBean> results;

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

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public int getPrevious_page_number() {
        return previous_page_number;
    }

    public void setPrevious_page_number(int previous_page_number) {
        this.previous_page_number = previous_page_number;
    }

    public int getCurrent_number() {
        return current_number;
    }

    public void setCurrent_number(int current_number) {
        this.current_number = current_number;
    }

    public int getNext_page_number() {
        return next_page_number;
    }

    public void setNext_page_number(int next_page_number) {
        this.next_page_number = next_page_number;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    @Override
    public String toString() {
        return "NewOrderBean{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous_page_number=" + previous_page_number +
                ", current_number=" + current_number +
                ", previous='" + previous + '\'' +
                ", next_page_number=" + next_page_number +
                ", page_size=" + page_size +
                ", results=" + results +
                '}';
    }
}
