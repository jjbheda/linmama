package com.linmama.dinning.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class NewOrderMenuBean extends BaseBean{
    private String status;
    public int per_page = 0;
    public int current_page = 0;
    public int last_page = 0;
    public List<LResultNewOrderBean> data = new ArrayList<>();

}
