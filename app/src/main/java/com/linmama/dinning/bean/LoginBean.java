package com.linmama.dinning.bean;


public class LoginBean {

    private String username;
    private String user_id;
    private String token;
    private String shop_name;

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_name() {

        return shop_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "username='" + username + '\'' +
                ", user_id='" + user_id + '\'' +
                ", token='" + token + '\'' +
                ", shop_name='" + shop_name + '\'' +
                '}';
    }
}
