package com.linmama.dinning.shop.bean;

import com.linmama.dinning.base.BaseModel;

import java.io.Serializable;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class ShopBean implements Serializable{
    public String shop_name = "";   //店铺名称
    public String shop_logo = "";   //店铺LOGO
    public String income = "";   //今日营收
    public int total_ords = 0;   //总订单数
    public String is_open = "";   ////是否营业  1在营业 0打烊

}
