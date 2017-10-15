package com.xcxid.dinning.bean;

/**
 * Created by jingkang on 2017/3/16
 */

public class SearchItemResultsBean {
    private int id;
    private String url;
    private String name;
    private String price;
    private String brief;
    private String remind;
    private Object tag_list;
    private boolean is_on_sell;
    private boolean isSpeciality;
    private boolean isHotSelling;
    private boolean isDiscount;
    private String discount;
    private String discount_price;
    private String small_image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public Object getTag_list() {
        return tag_list;
    }

    public void setTag_list(Object tag_list) {
        this.tag_list = tag_list;
    }

    public boolean is_on_sell() {
        return is_on_sell;
    }

    public void setIs_on_sell(boolean is_on_sell) {
        this.is_on_sell = is_on_sell;
    }

    public boolean isSpeciality() {
        return isSpeciality;
    }

    public void setSpeciality(boolean speciality) {
        isSpeciality = speciality;
    }

    public boolean isHotSelling() {
        return isHotSelling;
    }

    public void setHotSelling(boolean hotSelling) {
        isHotSelling = hotSelling;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getSmall_image() {
        return small_image;
    }

    public void setSmall_image(String small_image) {
        this.small_image = small_image;
    }
}
