package com.linmama.dinning.bean;

/**
 * Created by jingkang on 2017/3/19
 */

public class OrderWarnsBean {
    private int id;
    private String warn_time;
    private String warn_type;
    private String create_time;
    private boolean is_processed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWarn_time() {
        return warn_time;
    }

    public void setWarn_time(String warn_time) {
        this.warn_time = warn_time;
    }

    public String getWarn_type() {
        return warn_type;
    }

    public void setWarn_type(String warn_type) {
        this.warn_type = warn_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean is_processed() {
        return is_processed;
    }

    public void setIs_processed(boolean is_processed) {
        this.is_processed = is_processed;
    }
}
