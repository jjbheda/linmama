package com.linmama.dinning.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.QuitOrderInfoBean;
import com.linmama.dinning.bean.RefundBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.bean.TodayBean;
import com.linmama.dinning.widget.QuitOrderRefundItem;
import com.linmama.dinning.R;
import com.linmama.dinning.bean.QuitOrderBean;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class TodayOrderAdapter extends BaseAdapter {

    private List<TakingOrderBean> mResults;
    private LayoutInflater mInflater;
    private Context mContext;
    private IRefund mRefund;
    private IRefuse mRefuse;

    public TodayOrderAdapter(Context context, List<TakingOrderBean> results) {
        this.mContext = context;
        this.mResults = results;
        mInflater = LayoutInflater.from(context);
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
    LinearLayout shrintLt;
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder1 holder1 = null;
        if (view == null) {
            holder1 = new ViewHolder1();
            view = mInflater.inflate(R.layout.order_common_layout, viewGroup, false);
            holder1.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder1.order_time = (TextView) view.findViewById(R.id.order_time);
            holder1.table_num = (TextView) view.findViewById(R.id.table_num);
            holder1.tv_order_status = (TextView) view.findViewById(R.id.tv_order_status);
            holder1.parcel_iv = (TextView) view.findViewById(R.id.parcel_iv);
            holder1.tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            holder1.haspay_tv = (TextView) view.findViewById(R.id.haspay_tv);
            holder1.pay_tv = (TextView) view.findViewById(R.id.pay_tv);
            holder1.tv_serial_number = (TextView) view.findViewById(R.id.tv_serial_number);
            holder1.tv_delivery_address_name = (TextView) view.findViewById(R.id.tv_delivery_address_name);
            holder1.tv_delivery_address = (TextView) view.findViewById(R.id.tv_delivery_address);
            holder1.order_goods_lt = (LinearLayout) view.findViewById(R.id.order_goods_lt);
            holder1.order_time_list = (LinearLayout) view.findViewById(R.id.order_time_list);
            view.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) view.getTag();
        }
        TakingOrderBean bean = (TakingOrderBean) getItem(i);
        if (bean == null || holder1 == null) {
            return view;
        }
        holder1.tv_name.setText(bean.user.user_name);
//        holder1.table_num.setText(bean.id);
        holder1.order_time.setText(bean.order_datetime_bj);
//        holder1.tv_order_status.setText(bean.is_ensure_order.equals("0") ? "已接单" : "未接单");
        holder1.parcel_iv.setText(bean.is_for_here.equals("0") ? "自取" : "堂食");
        holder1.tv_remark.setText(bean.remark);
        holder1.haspay_tv.setText(bean.pay_amount);
        holder1.pay_tv.setText(bean.pay_amount);
        holder1.tv_serial_number.setText(bean.serial_number);
        holder1.tv_delivery_address_name.setText(bean.place.place_name);
        holder1.tv_delivery_address.setText(bean.place.place_address);
        holder1.shrint_btn = (TextView) view.findViewById(R.id.shrint_tv);
        holder1.order_goods_lt = (LinearLayout) view.findViewById(R.id.order_goods_lt);
        holder1.goods_shrink_lt = (LinearLayout) view.findViewById(R.id.goods_shrink_lt);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (OrderGoodBean bean1 : bean.goods_list) {
            View lt_view = layoutInflater.inflate(R.layout.lv_item_goods_single, null);
            TextView tv_name = (TextView) lt_view.findViewById(R.id.goods_name);
            TextView tv_num = (TextView) lt_view.findViewById(R.id.goods_number);
            TextView tv_price = (TextView) lt_view.findViewById(R.id.goods_price);
            tv_name.setText(bean1.name);
            tv_num.setText("X" + bean1.amount);
            tv_price.setText(bean1.total_price);
            holder1.order_goods_lt.addView(lt_view);
        }
        shrintLt = holder1.goods_shrink_lt;
        holder1.shrint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shrintLt.getVisibility() == View.VISIBLE){
                    shrintLt.setVisibility(View.GONE);
                } else {
                    shrintLt.setVisibility(View.VISIBLE);
                }
            }
        });
        View lt_view = layoutInflater.inflate(R.layout.lv_item_ordertime_single, null);
        TextView tv_order_takeoff_time = (TextView) lt_view.findViewById(R.id.order_takeoff_time);
        tv_order_takeoff_time.setText("取餐时间:" + bean.pickup.pickup_date + " " + bean.pickup.pickup_start_time + "-" + bean.pickup.pickup_end_time);
        holder1.order_time_list.addView(lt_view);

        return view;
    }

    private static class ViewHolder1 {
        TextView tv_name;
        TextView order_time;
        TextView tv_order_status;
        TextView table_num;
        TextView parcel_iv;
        TextView haspay_tv;
        TextView pay_tv;
        TextView shrint_btn;
        TextView tv_remark;
        TextView tv_delivery_address_name;
        TextView tv_delivery_address;
        LinearLayout order_goods_lt;
        LinearLayout order_time_list;
        LinearLayout goods_shrink_lt;
        TextView tv_serial_number;
        Button take;
        Button cancel;
        Button ok;
    }


    public void setRefund(IRefund refund) {
        this.mRefund = refund;
    }

    public void setRefuse(IRefuse refuse) {
        this.mRefuse = refuse;
    }

    public interface IRefund {
        void refund(int position, int item);
    }

    public interface IRefuse {
        void refuseRefund(int position, int item);
    }
}
