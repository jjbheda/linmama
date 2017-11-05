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
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.utils.ContectUtils;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class NewOrderAdapter extends BaseAdapter {
    private final static int ITEM_TYPE1 = 0;
    private final static int ITEM_TYPE2 = 1;

    private List<LResultNewOrderBean> mResults;
    private LayoutInflater mInflater;
    private Activity mContext;
    private ITakeOrder mTakeOrder;

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
                holder1.phone_lt = (LinearLayout) view.findViewById(R.id.phone_lt);
                holder1.ok = (TextView) view.findViewById(R.id.btnOrderCommit);
                holder1.cancel = (TextView) view.findViewById(R.id.btnNewCancel2);
                holder1.shrint_btn = (TextView) view.findViewById(R.id.shrint_tv);
                holder1.goods_shrink_lt = (LinearLayout) view.findViewById(R.id.goods_shrink_lt);
                view.setTag(holder1);
        } else {
                holder1 = (ViewHolder1) view.getTag();
        }
        final LResultNewOrderBean bean = (LResultNewOrderBean)getItem(i);
        if (bean == null || holder1 == null){
            return view;
        }
        holder1.tv_name.setText(bean.user.user_name);
        holder1.table_num.setText(bean.order_no+"");
        holder1.order_time.setText(bean.order_datetime_bj);
        holder1.tv_order_status.setText(bean.is_ensure_order.equals("0")?"已接单":"未接单");
        holder1.parcel_iv.setText(bean.is_for_here.equals("0")?"自取":"堂食");
        if (bean.remark.equals("")) {
            holder1.tv_remark.setVisibility(View.GONE);
        } else {
            holder1.tv_remark.setVisibility(View.VISIBLE);
            holder1.tv_remark.setText(bean.remark);
        }

        holder1.haspay_tv.setText(bean.pay_amount);
        holder1.pay_tv.setText(bean.pay_amount);
        holder1.tv_serial_number.setText(bean.serial_number);
        holder1.tv_delivery_address_name.setText(bean.place.place_name);
        holder1.tv_delivery_address.setText(bean.place.place_address);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (OrderOrderMenuBean bean1:bean.order_list){
            View lt_view = layoutInflater.inflate(R.layout.lv_item_goods_single,null);
            TextView tv_name = (TextView) lt_view.findViewById(R.id.goods_name);
//            TextView tv_num = (TextView) lt_view.findViewById(R.id.goods_number);
            TextView tv_price = (TextView) lt_view.findViewById(R.id.goods_price);
            tv_name.setText(bean1.goods_list.get(0).name);
//            tv_num.setText("X"+bean1.goods_list.get(0).amount);
            tv_price.setText(bean1.goods_list.get(0).total_price);
            holder1.order_goods_lt.addView(lt_view);
        }

        for (OrderPickupTimeBean bean1:bean.pickup_list) {
            View lt_view = layoutInflater.inflate(R.layout.lv_item_ordertime_single,null);
            TextView tv_order_takeoff_time = (TextView) lt_view.findViewById(R.id.order_takeoff_time);
            tv_order_takeoff_time.setText("取餐时间:"+bean1.pickup_date+ " "+bean1.pickup_start_time+"-"+bean1.pickup_end_time);
            holder1.order_time_list.addView(lt_view);
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
                    BaseModel.httpService.cancelOrder(bean.id,0). compose(new CommonTransformer())
                            .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                                @Override
                                public void onNext(String bean) {
                                    Toast.makeText(mContext,bean,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(ApiException e) {
                                    super.onError(e);
                                    Toast.makeText(mContext,"取消订单失败",Toast.LENGTH_SHORT).show();
                                }
                            });;
                }
            });
            holder1.ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseModel.httpService.commitOrder(bean.id). compose(new CommonTransformer())
                            .subscribe(new CommonSubscriber<String>(LmamaApplication.getInstance()) {
                                @Override
                                public void onNext(String bean) {
                                    Toast.makeText(mContext,bean,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(ApiException e) {
                                    super.onError(e);
                                    Toast.makeText(mContext,"确定订单失败",Toast.LENGTH_SHORT).show();
                                }
                            });;
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

    public void setTakeOrder(ITakeOrder takeOrder) {
        this.mTakeOrder = takeOrder;
    }

    public interface ITakeOrder {
        void takeOrder(int position);
    }

}
