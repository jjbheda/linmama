package com.linmama.dinning.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jingkang on 2017/3/12
 */

public class RefundBean implements Parcelable {
    private int id;
    private String menu_item_name;
    private int num;
    private String refund_amount;
    private String process_state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenu_item_name() {
        return menu_item_name;
    }

    public void setMenu_item_name(String menu_item_name) {
        this.menu_item_name = menu_item_name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getProcess_state() {
        return process_state;
    }

    public void setProcess_state(String process_state) {
        this.process_state = process_state;
    }

    @Override
    public String toString() {
        return "RefundBean{" +
                "id=" + id +
                ", menu_item_name='" + menu_item_name + '\'' +
                ", num=" + num +
                ", refund_amount='" + refund_amount + '\'' +
                ", process_state='" + process_state + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.menu_item_name);
        dest.writeInt(this.num);
        dest.writeString(this.refund_amount);
        dest.writeString(this.process_state);
    }

    public RefundBean() {
    }

    protected RefundBean(Parcel in) {
        this.id = in.readInt();
        this.menu_item_name = in.readString();
        this.num = in.readInt();
        this.refund_amount = in.readString();
        this.process_state = in.readString();
    }

    public static final Parcelable.Creator<RefundBean> CREATOR = new Parcelable.Creator<RefundBean>() {
        @Override
        public RefundBean createFromParcel(Parcel source) {
            return new RefundBean(source);
        }

        @Override
        public RefundBean[] newArray(int size) {
            return new RefundBean[size];
        }
    };
}
