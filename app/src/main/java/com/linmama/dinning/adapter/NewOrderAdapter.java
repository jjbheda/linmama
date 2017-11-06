package com.linmama.dinning.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.R;
import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.base.BaseHttpResult;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.LResultNewOrderBean;
import com.linmama.dinning.bean.OrderOrderMenuBean;
import com.linmama.dinning.bean.OrderPickupTimeBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.utils.ContectUtils;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class NewOrderAdapter extends BaseAdapter {
    private List<LResultNewOrderBean> mResults;
    private LayoutInflater mInflater;
    private Activity mContext;
    private ICommitOrder mCommitOrder;
    private ICancelOrder mCancelOrder;

    public NewOrderAdapter(Activity context, List<LResultNewOrderBean> results) {
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
        return 0;
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

    public void removeItem(int i) {
        LResultNewOrderBean rb = mResults.remove(i);
        if (null != rb) {
            this.notifyDataSetChanged();
        }
    }
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
                holder1.phone_lt = (LinearLayout) view.findViewById(R.id.phone_lt);
                holder1.ok = (TextView) view.findViewById(R.id.btnOrderCommit);
                holder1.cancel = (TextView) view.findViewById(R.id.btnNewCancel2);
                holder1.shrint_btn = (TextView) view.findViewById(R.id.shrint_tv);
                holder1.goods_shrink_lt = (LinearLayout) view.findViewById(R.id.goods_shrink_lt);
                view.setTag(holder1);
        } else {
                holder1 = (ViewHolder1) view.getTag();
        }
        final  LinearLayout shrintLt;
        final TextView shrint_btn;
        final LResultNewOrderBean bean = (LResultNewOrderBean)getItem(i);
        if (bean == null || holder1 == null){
            return view;
        }
        holder1.tv_name.setText(bean.user.user_name);
        holder1.table_num.setText(bean.order_no+"");
        holder1.order_time.setText(bean.order_datetime_bj);
        holder1.tv_order_status.setText("等待处理");
        holder1.parcel_iv.setText(bean.is_for_here.equals("0")?"自取":"堂食");
        if (bean.remark.equals("")) {
            holder1.tv_remark.setVisibility(View.GONE);
        } else {
            holder1.tv_remark.setVisibility(View.VISIBLE);
            holder1.tv_remark.setText(bean.remark);
        }

        holder1.haspay_tv.setText(bean.pay_amount);
        holder1.pay_tv.setText(bean.pay_amount);
        holder1.tv_serial_number.setText("单号： "+bean.serial_number);
        holder1.tv_delivery_address_name.setText(bean.place.place_name);
        holder1.tv_delivery_address.setText(bean.place.place_address);
        holder1.order_goods_lt.removeAllViews();
        for (OrderOrderMenuBean bean1:bean.order_list){
            View lt_view = mInflater.inflate(R.layout.lv_item_goods_single,null);
            TextView tv_name = (TextView) lt_view.findViewById(R.id.goods_name);
            TextView tv_price = (TextView) lt_view.findViewById(R.id.goods_price);
            tv_name.setText(bean1.goods_list.get(0).name);
            tv_price.setText(bean1.goods_list.get(0).total_price);
            holder1.order_goods_lt.addView(lt_view);
        }
        holder1.order_time_list.removeAllViews();
        for (OrderPickupTimeBean bean1:bean.pickup_list) {
            View lt_view = mInflater.inflate(R.layout.lv_item_ordertime_single,null);
            TextView tv_order_takeoff_time = (TextView) lt_view.findViewById(R.id.order_takeoff_time);
            tv_order_takeoff_time.setText("取餐时间:"+bean1.pickup_date+ " "+bean1.pickup_start_time+"-"+bean1.pickup_end_time);
            holder1.order_time_list.addView(lt_view);
        }
        shrint_btn = holder1.shrint_btn;
        shrintLt = holder1.goods_shrink_lt;
        shrint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shrintLt.getVisibility() == View.VISIBLE){
                    shrintLt.setVisibility(View.GONE);
                    shrint_btn.setText("展开");
                } else {
                    shrintLt.setVisibility(View.VISIBLE);
                    shrint_btn.setText("收起");
                }
            }
        });

        holder1.phone_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bean.user.user_tel.equals(""))
                    ContectUtils.onCall(mContext,bean.user.user_tel);
            }
        });
        holder1.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCancelOrder!=null){
                    mCancelOrder.onCancelOrder(bean);
                }
            }
        });
        holder1.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCommitOrder!=null){
                    mCommitOrder.onCommitOrder(bean);
                }
            }
        });
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
        TextView tv_remark;
        TextView shrint_btn;
        TextView tv_delivery_address_name;
        TextView tv_delivery_address;
        LinearLayout order_goods_lt;
        LinearLayout  phone_lt;;;
        LinearLayout goods_shrink_lt;
        LinearLayout order_time_list;
        TextView tv_serial_number;
        TextView cancel;
        TextView ok;
    }

    public void setCommitOrder(ICommitOrder commitOrder){
        mCommitOrder = commitOrder;
    }

    public void setCancelOrder(ICancelOrder cancelOrder){
        mCancelOrder = cancelOrder;
    }

    public interface ICommitOrder {
        void onCommitOrder(LResultNewOrderBean bean);
    }

    public interface ICancelOrder {
        void onCancelOrder(LResultNewOrderBean bean);
    }

}
