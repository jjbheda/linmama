package com.linmama.dinning.bean;

import java.io.Serializable;

/**
 * Created by jiangjingbo on 2017/10/22.
 */

public class OrderPlace implements Serializable {
    public String place_name = "";  //    "place_name": "测试自取点",
    public String place_address = "";   //"成都市锦江区永兴佳苑",
    public String place_type = "";  //1 店铺自取点 null或者0 为其他自取点
}
