package com.linmama.dinning.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingkang on 2017/3/12
 */

public class QuitOrderBean implements Parcelable {
    private QuitOrderInfoBean order_info;
    private List<RefundBean> refund_applications;

    public QuitOrderInfoBean getOrder_info() {
        return order_info;
    }

    public void setOrder_info(QuitOrderInfoBean order_info) {
        this.order_info = order_info;
    }

    public List<RefundBean> getRefund_applications() {
        return refund_applications;
    }

    public void setRefund_applications(List<RefundBean> refund_applications) {
        this.refund_applications = refund_applications;
    }

    @Override
    public String toString() {
        return "QuitOrderBean{" +
                "order_info=" + order_info +
                ", refund_applications=" + refund_applications +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.order_info, flags);
        dest.writeList(this.refund_applications);
    }

    public QuitOrderBean() {
    }

    protected QuitOrderBean(Parcel in) {
        this.order_info = in.readParcelable(QuitOrderInfoBean.class.getClassLoader());
        this.refund_applications = new ArrayList<RefundBean>();
        in.readList(this.refund_applications, RefundBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<QuitOrderBean> CREATOR = new Parcelable.Creator<QuitOrderBean>() {
        @Override
        public QuitOrderBean createFromParcel(Parcel source) {
            return new QuitOrderBean(source);
        }

        @Override
        public QuitOrderBean[] newArray(int size) {
            return new QuitOrderBean[size];
        }
    };
}
