package com.linmama.dinning.shop.bean;

import java.io.Serializable;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class ShopSaleParseBean implements Serializable {
    public String income_now = "";//当天营业额
    public String income_yesterday = "";//昨日营业额
    public int valid_order = 0;//有效订单数
    public String per_price = "";//客单价
    public int income_invalid = 0;//预计损失
    public int count_invalid = 0;//无效订单数

}
