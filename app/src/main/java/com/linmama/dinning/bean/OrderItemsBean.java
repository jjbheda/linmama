package com.linmama.dinning.bean;

/**
 * Created by jingkang on 2017/3/11
 */
public class OrderItemsBean {
    private int id;
    private int item_id;
    private String name;
    private String closing_cost;
    private String status;
    private int num;
    private RefundInfoBean refund_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClosing_cost() {
        return closing_cost;
    }

    public void setClosing_cost(String closing_cost) {
        this.closing_cost = closing_cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public RefundInfoBean getRefund_info() {
        return refund_info;
    }

    public void setRefund_info(RefundInfoBean refund_info) {
        this.refund_info = refund_info;
    }
}
