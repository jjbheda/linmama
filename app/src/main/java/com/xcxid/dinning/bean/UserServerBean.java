package com.xcxid.dinning.bean;

/**
 * Created by jingkang on 2017/3/31
 */

public class UserServerBean {
    private int id;
    private String user_code;
    private String user_fullname;
    private String server_address;
    private String create_datetime;
    private boolean is_active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getServer_address() {
        return server_address;
    }

    public void setServer_address(String server_address) {
        this.server_address = server_address;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "UserServerBean{" +
                "id=" + id +
                ", user_code='" + user_code + '\'' +
                ", user_fullname='" + user_fullname + '\'' +
                ", server_address='" + server_address + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", is_active=" + is_active +
                '}';
    }
}
