package com.linmama.dinning.bean;

import java.io.Serializable;

/**
 * Created by jiangjingbo on 2017/10/22.
 */

public class OrderPickupTimeBean implements Serializable {

    public String pickup_date = "";      //  "10-10 (周二)", //取餐日期 格式化的
    public String pickup_start_time = "";   // "08:00" 取餐开始时间
    public String pickup_end_time = "";   //"08:15", //取餐结束时间
    public String pickup_date1 = "";   //"2017-10-10" //取餐日期 原始日期

}
