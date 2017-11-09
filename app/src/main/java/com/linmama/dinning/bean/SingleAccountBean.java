package com.linmama.dinning.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangjingbo on 2017/11/9.
 */

public class SingleAccountBean implements Serializable {
    public int per_page = 0;
    public int current_page = 0;
    public int last_page = 0;

    public List<SingleAccountItemBean> data = new ArrayList<>();
}
