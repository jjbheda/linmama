package com.linmama.dinning.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/10/23.
 */

public class LResultNewOrderBean extends BaseBean {
    public List<OrderOrderMenuBean> order_list = new ArrayList<>();
    public List<OrderPickupTimeBean> pickup_list = new ArrayList<>();
    public OrderPlace place = new OrderPlace();
    public OrderUser user = new OrderUser();
    public String serial_number = ""; //"No20171010224933576179", //订单号
    public int id = 0; //2, //订单ID
    public int order_no = 0; //序号
    public int amount = 0; //3, //商品总数
    public String remark = ""; ////备注
    public String is_for_here = ""; //1 堂食 0 自取
    public String order_type = ""; //1预约单 0当日单
    public String is_ensure_order = ""; //1已接单 0未接单
    public String pay_amount = ""; //"0.07", //支付金额
    public String order_datetime_bj = ""; //"2017-10-10 22:49:33" //下单日期
}
