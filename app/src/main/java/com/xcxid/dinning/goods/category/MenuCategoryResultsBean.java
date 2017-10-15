package com.xcxid.dinning.goods.category;

/**
 * Created by jingkang on 2017/3/11
 */

public class MenuCategoryResultsBean {
    private int id;
    private String name;
    private String remind;
    private String sequence;
    private boolean isHide;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }
}
