package com.linmama.dinning.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingkang on 2017/3/5
 */

public class TakingOrderBean {
    public int amount = 0;
    public List<OrderGoodBean>  goods_list = new ArrayList<>();
    public OrderPickupTimeBean pickup = new OrderPickupTimeBean();
    public OrderPlace place = new OrderPlace();
    public OrderUser user = new OrderUser();
    public String serial_number = "";//No20171010224933576179
    public int id = 0 ;//23,
    public String remark= "";
    public String is_for_here = "";// "1"
    public String pay_amount= "";// "0.07"
    public String order_datetime_bj= "";// "2017-10-10 22:49:33"
}
