package com.linmama.dinning.bean;

import com.linmama.dinning.base.BaseHttpResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/2.
 */

public class TakingOrderMenuBean  extends BaseBean{
    private String status;
    public int per_page = 0;
    public int current_page = 0;
    public int last_page = 0;
    public List<TakingOrderBean> data = new ArrayList<>();

}
