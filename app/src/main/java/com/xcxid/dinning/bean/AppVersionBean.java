package com.xcxid.dinning.bean;

/**
 * Created by jingkang on 2017/3/29
 */

public class AppVersionBean {
    private int id;
    private String version_no;
    private int version_code;
    private String content;
    private String apk;
    private String update_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion_no() {
        return version_no;
    }

    public void setVersion_no(String version_no) {
        this.version_no = version_no;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }
}
