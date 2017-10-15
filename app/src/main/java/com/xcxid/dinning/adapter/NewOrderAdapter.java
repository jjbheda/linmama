package com.xcxid.dinning.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.bean.ResultsBean;
import com.xcxid.dinning.bean.ResultsBeanNew;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class NewOrderAdapter extends BaseAdapter {
    private final static int ITEM_TYPE1 = 0;
    private final static int ITEM_TYPE2 = 1;

    private List<ResultsBeanNew> mResults;
    private LayoutInflater mInflater;
    private Context mContext;
    private ITakeOrder mTakeOrder;
    private ICancelOrder mCancelOrder;
    private IOKOrder mOkOrder;

    public NewOrderAdapter(Context context, List<ResultsBeanNew> results) {
        this.mContext = context;
        this.mResults = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        String payStatus = mResults.get(position).pay_status;
        int type = ITEM_TYPE1;
        if (payStatus.equals("1")) {
            type = ITEM_TYPE2;
        } else if (payStatus.equals("2")) {
            type = ITEM_TYPE1;
        }
        return type;
    }

    @Override
    public int getCount() {
        return mResults != null ? mResults.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(i);
        if (view == null) {
            if (type == ITEM_TYPE1) {
                holder1 = new ViewHolder1();
//                view = mInflater.inflate(R.layout.lv_item_neworder1, viewGroup, false);
                view = mInflater.inflate(R.layout.lv_item_neworder2_v2, viewGroup, false);
                holder1.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder1.order_time = (TextView) view.findViewById(R.id.order_time);
                holder1.tv_order_status = (TextView) view.findViewById(R.id.tv_order_status);
                holder1.order_end_time = (TextView) view.findViewById(R.id.order_end_time);
                holder1.tv_delivery_address = (TextView) view.findViewById(R.id.tv_delivery_address);
                holder1.tv_remark = (TextView) view.findViewById(R.id.tv_remark);
                holder1.tv_invoice_title = (TextView) view.findViewById(R.id.tv_invoice_title);
                holder1.tv_delivery_fee = (TextView) view.findViewById(R.id.tv_delivery_fee);
                holder1.tv_full_minus_amount = (TextView) view.findViewById(R.id.tv_full_minus_amount);
                holder1.tv_pay_amount = (TextView) view.findViewById(R.id.tv_pay_amount);
                holder1.tv_serial_number = (TextView) view.findViewById(R.id.tv_serial_number);

//                holder1.icon = (ImageView) view.findViewById(R.id.ivNewIcon);
//                holder1.desk = (TextView) view.findViewById(R.id.tvNewDesk);
//                holder1.person = (TextView) view.findViewById(R.id.tvNewPerson);
//                holder1.way = (TextView) view.findViewById(R.id.tvNewWay);
//                holder1.serial = (TextView) view.findViewById(R.id.tvNewSerial);
//                holder1.datetime = (TextView) view.findViewById(R.id.tvNewDatetime);
//                holder1.other = (TextView) view.findViewById(R.id.tvNewOther);
//                holder1.amount = (TextView) view.findViewById(R.id.tvNewAmount);
//                holder1.payStatus = (TextView) view.findViewById(R.id.tvNewPayStatus);
//                holder1.take = (Button) view.findViewById(R.id.btnNewTake);
//                holder1.cancel = (Button) view.findViewById(R.id.btnNewCancel);
                view.setTag(holder1);
            } else if (type == ITEM_TYPE2) {
                holder2 = new ViewHolder2();
                view = mInflater.inflate(R.layout.lv_item_neworder2, viewGroup, false);
//                view = mInflater.inflate(R.layout.lv_item_neworder2_v2, viewGroup, false);
                holder2.icon = (ImageView) view.findViewById(R.id.ivNewIcon2);
                holder2.desk = (TextView) view.findViewById(R.id.tvNewDesk2);
                holder2.person = (TextView) view.findViewById(R.id.tvNewPerson2);
                holder2.way = (TextView) view.findViewById(R.id.tvNewWay2);
                holder2.serial = (TextView) view.findViewById(R.id.tvNewSerial2);
                holder2.datetime = (TextView) view.findViewById(R.id.tvNewDatetime2);
                holder2.other = (TextView) view.findViewById(R.id.tvNewOther2);
                holder2.amount = (TextView) view.findViewById(R.id.tvNewAmount2);
                holder2.payStatus = (TextView) view.findViewById(R.id.tvNewPayStatus2);
                holder2.take = (Button) view.findViewById(R.id.btnNewTake2);
                holder2.cancel = (Button) view.findViewById(R.id.btnNewCancel2);
                holder2.ok = (Button) view.findViewById(R.id.btnNewOK2);
                view.setTag(holder2);
            }
        } else {
            if (type == ITEM_TYPE1) {
                holder1 = (ViewHolder1) view.getTag();
            } else if (type == ITEM_TYPE2) {
                holder2 = (ViewHolder2) view.getTag();
            }
        }
        ResultsBeanNew rb = mResults.get(i);
//        boolean isInStore = rb.;
//        String deskNum = rb.getDesk_num();
//        String diningWay = rb.getDining_way();
        if (type == ITEM_TYPE1) {
            Log.d("a","type=1");
            holder1.tv_name.setText(rb.receiver_name);
            holder1.order_time.setText(rb.order_datetime_bj);
            String status = "已取消";
            //订单状态，0表示已取消，1表示新订单，2表示已接单，3表示已完成
            switch (rb.order_status){
                case "0":
                    status = "已取消";
                    break;
                case "1":
                    status = "新订单";
                    break;
                case "2":
                    status = "已接单";
                    break;
                case "3":
                    status = "已完成";
                    break;
                default:
                        status = "已取消";
                        break;

            }
            holder1.tv_order_status.setText(status);
            holder1.order_end_time.setText(rb.real_estimate_delivery_time);
            holder1.tv_delivery_address.setText(rb.delivery_address);
            holder1.tv_remark.setText(rb.remark);
            holder1.tv_invoice_title.setText(rb.invoice_title);
            holder1.tv_delivery_fee.setText(rb.delivery_fee+"");
            holder1.tv_full_minus_amount.setText(rb.full_minus_amount+"");
            holder1.tv_pay_amount.setText(rb.pay_amount+"");
            holder1.tv_serial_number.setText(rb.serial_number+"");
//            if (isInStore && !TextUtils.isEmpty(deskNum)) {
//                holder1.icon.setImageResource(R.mipmap.icon_online);
//                holder1.desk.setText(deskNum);
//                holder1.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderDeskNum));
//            } else if (isInStore && TextUtils.isEmpty(deskNum)) {
//                holder1.icon.setImageResource(R.mipmap.icon_fake);
//                holder1.desk.setText("来客单");
//                holder1.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderTake));
//            } else if (!isInStore) {
//                holder1.icon.setImageResource(R.mipmap.icon_appoint);
//                holder1.desk.setText("预约单");
//                holder1.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
//                holder1.other.setVisibility(View.VISIBLE);
//                holder1.other.setText(String.format(mContext.getResources().getString(R.string.new_order_in_store_time), rb.getIn_store_time()));
//            }
//            holder1.person.setText(String.format(mContext.getResources().getString(R.string.new_order_person), rb.getDine_num()));
//            if (diningWay.equals("1")) {
//                holder1.way.setText("(堂食)");
//            } else if (diningWay.equals("2")) {
//                holder1.way.setText("(外带)");
//            }
//            holder1.serial.setText(String.format(mContext.getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
//            holder1.datetime.setText(String.format(mContext.getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
//            holder1.amount.setText(String.format(mContext.getResources().getString(R.string.order_money), rb.getTotal_amount()));
//            holder1.payStatus.setText("(已支付)");
//            holder1.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderText));
//            holder1.take.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (null != NewOrderAdapter.this.mTakeOrder) {
//                        NewOrderAdapter.this.mTakeOrder.takeOrder(i);
//                    }
//                }
//            });
//            holder1.cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (null != NewOrderAdapter.this.mCancelOrder) {
//                        NewOrderAdapter.this.mCancelOrder.cancelOrder(i);
//                    }
//                }
//            });
        }
        else if (type == ITEM_TYPE2) {
//            if (isInStore && !TextUtils.isEmpty(deskNum)) {
//                holder2.icon.setImageResource(R.mipmap.icon_online);
//                holder2.desk.setText(deskNum);
//                holder2.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderDeskNum));
//            } else if (isInStore && TextUtils.isEmpty(deskNum)) {
//                holder2.icon.setImageResource(R.mipmap.icon_fake);
//                holder2.desk.setText("来客单");
//                holder2.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderTake));
//            } else if (!isInStore) {
//                holder2.icon.setImageResource(R.mipmap.icon_appoint);
//                holder2.desk.setText("预约单");
//                holder2.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
//            }
//            holder2.person.setText(String.format(mContext.getResources().getString(R.string.new_order_person), rb.getDine_num()));
//            if (diningWay.equals("1")) {
//                holder2.way.setText("(堂食)");
//            } else if (diningWay.equals("2")) {
//                holder2.way.setText("(外带)");
//            }
//            holder2.serial.setText(String.format(mContext.getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
//            holder2.datetime.setText(String.format(mContext.getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
//            holder2.amount.setText(String.format(mContext.getResources().getString(R.string.order_money), rb.getTotal_amount()));
//            holder2.payStatus.setText("(未支付)");
//            holder2.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
//            holder2.other.setVisibility(View.VISIBLE);
//            String channel = rb.getPay_channel();
//            if (channel.equals("1")) {
//                holder2.other.setText(String.format(mContext.getResources().getString(R.string.new_order_pay_way), "在线支付"));
//            } else if (channel.equals("2")) {
//                holder2.other.setText(String.format(mContext.getResources().getString(R.string.new_order_pay_way), "吧台支付"));
//            }
            holder2.take.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (null != NewOrderAdapter.this.mTakeOrder) {
                        NewOrderAdapter.this.mTakeOrder.takeOrder(i);
                    }
                }
            });
            holder2.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != NewOrderAdapter.this.mCancelOrder) {
                        NewOrderAdapter.this.mCancelOrder.cancelOrder(i);
                    }
                }
            });
            holder2.ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mOkOrder) {
                        NewOrderAdapter.this.mOkOrder.okOrder(i);
                    }
                }
            });
        }
        return view;
    }

    private static class ViewHolder1 {
        TextView tv_name;
        TextView order_time;
        TextView tv_order_status;
        TextView order_end_time;
        TextView tv_delivery_address;
        TextView tv_remark;
        TextView tv_invoice_title;
        TextView tv_delivery_fee;
        TextView tv_full_minus_amount;
        TextView tv_pay_amount;
        TextView tv_serial_number;
        TextView serial;
        TextView datetime;
        TextView other;
        TextView amount;
        TextView payStatus;
        Button take;
        Button cancel;
    }

    private static class ViewHolder2 {
        ImageView icon;
        TextView desk;
        TextView person;
        TextView way;
        TextView serial;
        TextView datetime;
        TextView other;
        TextView amount;
        TextView payStatus;
        Button take;
        Button cancel;
        Button ok;
    }

    public void setTakeOrder(ITakeOrder takeOrder) {
        this.mTakeOrder = takeOrder;
    }

    public void setCancelOrder(ICancelOrder cancelOrder) {
        this.mCancelOrder = cancelOrder;
    }

    public void setOkOrder(IOKOrder okOrder) {
        this.mOkOrder = okOrder;
    }

    public interface ITakeOrder {
        void takeOrder(int position);
    }

    public interface ICancelOrder {
        void cancelOrder(int position);
    }

    public interface IOKOrder {
        void okOrder(int position);
    }
}
