package com.linmama.dinning.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by jingkang on 2017/3/5
 */

public class ResultsBean implements Parcelable {

    private int id;
    private String url;
    private String serial_number;
    private String total_amount;
    private String pay_status;
    private String pay_channel;
    private String order_status;
    private String dining_way = "";
    private boolean is_in_store;
    private String in_store_time;
    private String desk_num;
    private int dine_num;
    private String order_datetime_bj;
    private String remark;
    private boolean is_invoice;
    private String invoice_title;
    private int item_count;
    private int finished_count;
    private int unfinished_count;
    private String wx_nickname;
    private String avatar_url;
    private List<OrderWarnsBean> orderWarms;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getDining_way() {
        return dining_way;
    }

    public void setDining_way(String dining_way) {
        this.dining_way = dining_way;
    }

    public boolean is_in_store() {
        return is_in_store;
    }

    public void setIs_in_store(boolean is_in_store) {
        this.is_in_store = is_in_store;
    }

    public String getIn_store_time() {
        return in_store_time;
    }

    public void setIn_store_time(String in_store_time) {
        this.in_store_time = in_store_time;
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

    public boolean is_invoice() {
        return is_invoice;
    }

    public void setIs_invoice(boolean is_invoice) {
        this.is_invoice = is_invoice;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
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

    public String getWx_nickname() {
        return wx_nickname;
    }

    public void setWx_nickname(String wx_nickname) {
        this.wx_nickname = wx_nickname;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public List<OrderWarnsBean> getOrderWarms() {
        return orderWarms;
    }

    public void setOrderWarms(List<OrderWarnsBean> orderWarms) {
        this.orderWarms = orderWarms;
    }

    @Override
    public String toString() {
        return "ResultsBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", serial_number='" + serial_number + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", pay_status='" + pay_status + '\'' +
                ", pay_channel='" + pay_channel + '\'' +
                ", order_status='" + order_status + '\'' +
                ", dining_way='" + dining_way + '\'' +
                ", is_in_store=" + is_in_store +
                ", in_store_time='" + in_store_time + '\'' +
                ", desk_num='" + desk_num + '\'' +
                ", dine_num=" + dine_num +
                ", order_datetime_bj='" + order_datetime_bj + '\'' +
                ", remark='" + remark + '\'' +
                ", is_invoice=" + is_invoice +
                ", invoice_title='" + invoice_title + '\'' +
                ", item_count=" + item_count +
                ", finished_count=" + finished_count +
                ", unfinished_count=" + unfinished_count +
                ", wx_nickname='" + wx_nickname + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.url);
        dest.writeString(this.serial_number);
        dest.writeString(this.total_amount);
        dest.writeString(this.pay_status);
        dest.writeString(this.pay_channel);
        dest.writeString(this.order_status);
        dest.writeString(this.dining_way);
        dest.writeByte(is_in_store ? (byte) 1 : (byte) 0);
        dest.writeString(this.in_store_time);
        dest.writeString(this.desk_num);
        dest.writeInt(this.dine_num);
        dest.writeString(this.order_datetime_bj);
        dest.writeString(this.remark);
        dest.writeByte(is_invoice ? (byte) 1 : (byte) 0);
        dest.writeString(this.invoice_title);
        dest.writeInt(this.item_count);
        dest.writeInt(this.finished_count);
        dest.writeInt(this.unfinished_count);
        dest.writeString(this.wx_nickname);
        dest.writeString(this.avatar_url);
    }

    public ResultsBean() {
    }

    protected ResultsBean(Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
        this.serial_number = in.readString();
        this.total_amount = in.readString();
        this.pay_status = in.readString();
        this.pay_channel = in.readString();
        this.order_status = in.readString();
        this.dining_way = in.readString();
        this.is_in_store = in.readByte() != 0;
        this.in_store_time = in.readString();
        this.desk_num = in.readString();
        this.dine_num = in.readInt();
        this.order_datetime_bj = in.readString();
        this.remark = in.readString();
        this.is_invoice = in.readByte() != 0;
        this.invoice_title = in.readString();
        this.item_count = in.readInt();
        this.finished_count = in.readInt();
        this.unfinished_count = in.readInt();
        this.wx_nickname = in.readString();
        this.avatar_url = in.readString();
    }

    public static final Parcelable.Creator<ResultsBean> CREATOR = new Parcelable.Creator<ResultsBean>() {
        @Override
        public ResultsBean createFromParcel(Parcel source) {
            return new ResultsBean(source);
        }

        @Override
        public ResultsBean[] newArray(int size) {
            return new ResultsBean[size];
        }
    };
}
