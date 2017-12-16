package com.linmama.dinning.bluetooth;

import java.io.Serializable;

/**
 * Created by jiangjingbo on 2017/12/13.
 */

public class PrintBean implements Serializable {
    public String printSn = "";     //打印机识别码
    public int id = 0;              //保存在本地一份 客户端定义的ID 用于UI显示
    public boolean conStatus = false;      //已连接

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PrintBean) {
            if (((PrintBean) obj).printSn.equals(this.printSn))
                return true;
        }
        return false;
    }
}
