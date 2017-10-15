package com.xcxid.dinning.bean;

/**
 * Created by jingkang on 2017/3/13
 */

public class StoreSettingsBean {
    private String storeName;
    private int deskNumber;
    private boolean is_closed;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(int deskNumber) {
        this.deskNumber = deskNumber;
    }

    public boolean is_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }
}
