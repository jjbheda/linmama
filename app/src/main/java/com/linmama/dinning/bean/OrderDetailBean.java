package com.linmama.dinning.bean;

import java.util.List;

/**
 * Created by jingkang on 2017/3/11
 */

public class OrderDetailBean {
    private int id;
    private String serial_number;
    private String total_amount;
    private String pay_status;
    private String pay_channel;
    private String dining_way;
    private String order_datetime_bj;
    private String remark;
    private String desk_num;
    private int dine_num;
    private int item_count;
    private int finished_count;
    private int unfinished_count;
    private List<OrderItemsBean> orderItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getOrder_datetime_bj() {
        return order_datetime_bj;
    }

    public void setOrder_datetime_bj(String order_datetime_bj) {
        this.order_datetime_bj = order_datetime_bj;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public int getFinished_count() {
        return finished_count;
    }

    public void setFinished_count(int finished_count) {
        this.finished_count = finished_count;
    }

    public int getUnfinished_count() {
        return unfinished_count;
    }

    public void setUnfinished_count(int unfinished_count) {
        this.unfinished_count = unfinished_count;
    }

    public List<OrderItemsBean> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemsBean> orderItems) {
        this.orderItems = orderItems;
    }

    public String getDining_way() {
        return dining_way;
    }

    public void setDining_way(String dining_way) {
        this.dining_way = dining_way;
    }

    public String getDesk_num() {
        return desk_num;
    }

    public void setDesk_num(String desk_num) {
        this.desk_num = desk_num;
    }

    public int getDine_num() {
        return dine_num;
    }

    public void setDine_num(int dine_num) {
        this.dine_num = dine_num;
    }
}
