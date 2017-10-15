package com.linmama.dinning.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jingkang on 2017/3/10
 * warn_time为提醒RemindResultsBean时间，
 * warn_type提醒类型，1表示催单提醒，2表示备菜提醒
 */
public class RemindResultsBean implements Parcelable {
    private int id;
    private ResultsBean order;
    private String warn_time;
    private String warn_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResultsBean getOrder() {
        return order;
    }

    public void setOrder(ResultsBean order) {
        this.order = order;
    }

    public String getWarn_time() {
        return warn_time;
    }

    public void setWarn_time(String warn_time) {
        this.warn_time = warn_time;
    }

    public String getWarn_type() {
        return warn_type;
    }

    public void setWarn_type(String warn_type) {
        this.warn_type = warn_type;
    }

    @Override
    public String toString() {
        return "RemindResultsBean{" +
                "id=" + id +
                ", order=" + order +
                ", warn_time='" + warn_time + '\'' +
                ", warn_type='" + warn_type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.order, flags);
        dest.writeString(this.warn_time);
        dest.writeString(this.warn_type);
    }

    public RemindResultsBean() {
    }

    protected RemindResultsBean(Parcel in) {
        this.id = in.readInt();
        this.order = in.readParcelable(ResultsBean.class.getClassLoader());
        this.warn_time = in.readString();
        this.warn_type = in.readString();
    }

    public static final Parcelable.Creator<RemindResultsBean> CREATOR = new Parcelable.Creator<RemindResultsBean>() {
        @Override
        public RemindResultsBean createFromParcel(Parcel source) {
            return new RemindResultsBean(source);
        }

        @Override
        public RemindResultsBean[] newArray(int size) {
            return new RemindResultsBean[size];
        }
    };
}
