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
    public String status = "";//0 可取消 1已取消 2 已退款
    public int order_no= 0;// 序号
    public String is_ensure_order="1"; //1已接单 0未接单
    public String fail_reson = "";  //订单金额或退款金额与之前请求不一致，请核实后再试"
}
