package com.linmama.dinning.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/10/22.
 */

public class OrderOrderMenuBean implements Serializable {
    public String date = "";
    public List<OrderGoodBean> goods_list = new ArrayList<>();

}

