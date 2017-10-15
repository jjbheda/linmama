package com.linmama.dinning.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jingkang on 2017/3/5
 */

public class ResultsBeanNew implements Parcelable {

    public int id = 0;
    public String diliveryman_tel = ""; //配送员电话
    public long serial_number = 0; //订单号
    public float dishes_amount = 0;  //菜品金额
    public float boxes_amount = 0;  //餐盒费金额
    public float delivery_fee = 0;  //配送费
    public float order_amount = 0; //订单总金额
    public float new_user_discount_amount = 0; //新用户立减金额
    public float full_minus_amount = 0; //满减优惠金额
    public float pay_amount = 0; //实际支付金额
    public String pay_status = ""; //支付状态，1表示未支付，2表示已支付
    public String order_status = ""; //订单状态，0表示已取消，1表示新订单，2表示已接单，3表示已完成
    public String order_datetime_bj = ""; //下单时间 "2017/07/28 16:06:25"
    public String real_estimate_delivery_time = ""; //2017/07/28 16:36:25", //预计送达时间
    public String remark = ""; //备注
    public boolean is_invoice = false; //是否需要开发票
    public String invoice_title = ""; //发票抬头
    public boolean is_commented = false; //是否已评价，true已评价，false未评价
    public String receiver_name = "";  //收货人姓名
    public String delivery_address = "";//xx路2号yy小区5号楼502", //收货地址
    public String phone = "";//18910325361", //收货人电话

    @Override
    public String toString() {
        return "ResultsBean{" +
                "id=" + id +
                "diliveryman_tel" + diliveryman_tel +
                "serial_number" + serial_number +  //订单号
                "dishes_amount" + dishes_amount +  //菜品金额
                "boxes_amount" + boxes_amount + //餐盒费金额
                "delivery_fee" + delivery_fee +  //配送费
                "order_amount" + order_amount + //订单总金额
                "new_user_discount_amount" + new_user_discount_amount + //新用户立减金额
                "full_minus_amount" + full_minus_amount + //满减优惠金额
                "pay_amount" + pay_amount + //实际支付金额
                "pay_status" + pay_status + //支付状态，1表示未支付，2表示已支付
                "order_status" + order_status + //订单状态，0表示已取消，1表示新订单，2表示已接单，3表示已完成
                "order_datetime_bj" + order_datetime_bj +//下单时间 "2017/07/28 16:06:25"
                "real_estimate_delivery_time" + real_estimate_delivery_time +//2017/07/28 16:36:25", //预计送达时间
                "remark" + remark + //备注
                "is_invoice" + is_invoice + //是否需要开发票
                "invoice_title" + invoice_title + //发票抬头
                "is_commented" + is_commented +//是否已评价，true已评价，false未评价
                "receiver_name" + receiver_name +  //收货人姓名
                "delivery_address" + delivery_address +//xx路2号yy小区5号楼502", //收货地址
                "phone" + phone +//18910325361", //收货人电话
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.diliveryman_tel);
        dest.writeLong(this.serial_number);
        dest.writeFloat(this.dishes_amount);
        dest.writeFloat(this.boxes_amount);
        dest.writeFloat(this.delivery_fee);
        dest.writeFloat(this.order_amount);
        dest.writeFloat(this.new_user_discount_amount);
        dest.writeFloat(this.full_minus_amount);
        dest.writeFloat(this.pay_amount);
        dest.writeString(this.pay_status);
        dest.writeString(this.order_status);
        dest.writeString(this.order_datetime_bj);
        dest.writeString(this.real_estimate_delivery_time);
        dest.writeString(this.remark);
        dest.writeByte(is_invoice ? (byte) 1 : (byte) 0);
        dest.writeString(this.invoice_title);
        dest.writeByte(is_commented ? (byte) 1 : (byte) 0);
        dest.writeString(this.receiver_name);
        dest.writeString(this.delivery_address);
        dest.writeString(this.phone);
    }

    public ResultsBeanNew() {
    }

    protected ResultsBeanNew(Parcel in) {
        this.id = in.readInt();
        this.diliveryman_tel = in.readString();
        this.serial_number = in.readLong();
        this.dishes_amount = in.readFloat();
        this.boxes_amount = in.readFloat();
        this.delivery_fee = in.readFloat();
        this.order_amount = in.readFloat();
        this.new_user_discount_amount = in.readFloat();
        this.full_minus_amount = in.readFloat();
        this.pay_amount= in.readFloat();
        this.pay_status= in.readString();
        this.order_status= in.readString();
        this.order_datetime_bj= in.readString();
       this.real_estimate_delivery_time= in.readString();
        this.remark= in.readString();
        this.is_invoice = in.readByte() != 0;
        this.invoice_title= in.readString();
        is_commented = in.readByte() != 0;
        this.receiver_name= in.readString();
        this.delivery_address= in.readString();
        this.phone= in.readString();
    }

    public static final Creator<ResultsBeanNew> CREATOR = new Creator<ResultsBeanNew>() {
        @Override
        public ResultsBeanNew createFromParcel(Parcel source) {
            return new ResultsBeanNew(source);
        }

        @Override
        public ResultsBeanNew[] newArray(int size) {
            return new ResultsBeanNew[size];
        }
    };
}
