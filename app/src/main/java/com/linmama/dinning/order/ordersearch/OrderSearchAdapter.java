package com.linmama.dinning.order.ordersearch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linmama.dinning.LmamaApplication;
import com.linmama.dinning.R;
import com.linmama.dinning.base.BaseModel;
import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.except.ApiException;
import com.linmama.dinning.subscriber.CommonSubscriber;
import com.linmama.dinning.transformer.CommonTransformer;
import com.linmama.dinning.utils.ContectUtils;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class OrderSearchAdapter extends BaseAdapter {

    private List<TakingOrderBean> mResults;
    private LayoutInflater mInflater;
    private Activity mContext;
    private IPosOrder mPosOrder;
    //    private ICancelRingOrder mCancelRingOrder;
    private IOKOrder mOkOrder;
    private IComplete mCompleteOrder;

    public OrderSearchAdapter(Activity context, List<TakingOrderBean> results) {
        this.mContext = context;
        this.mResults = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
        TakingOrderBean rb = mResults.remove(i);
        if (null != rb) {
            this.notifyDataSetChanged();
        }
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
            holder1.notes_msg_lt = (LinearLayout) view.findViewById(R.id.notes_msg_lt);
            view.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) view.getTag();
        }
        final TakingOrderBean bean = (TakingOrderBean) getItem(i);
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
        holder1.phone_lt = (LinearLayout) view.findViewById(R.id.phone_lt);
        holder1.ok = (TextView) view.findViewById(R.id.btnOrderCommit);
        holder1.cancel = (TextView) view.findViewById(R.id.btnNewCancel2);
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

        if (holder1.tv_remark.equals("")) {
            holder1.tv_remark.setVisibility(View.GONE);
        } else {
            holder1.tv_remark.setVisibility(View.VISIBLE);
            holder1.tv_remark.setText(bean.remark);
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
                BaseModel.httpService.cancelOrder(bean.id+"",1). compose(new CommonTransformer())
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
                BaseModel.httpService.commitOrder(bean.id+""). compose(new CommonTransformer())
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
        LinearLayout phone_lt;
        TextView tv_serial_number;
        LinearLayout notes_msg_lt;
        TextView cancel;
        TextView ok;
    }

    public void setPosOrder(IPosOrder posOrder) {
        this.mPosOrder = posOrder;
    }

    public void setOkOrder(IOKOrder okOrder) {
        this.mOkOrder = okOrder;
    }

    public void setCompleteOrder(IComplete completeOrder) {
        this.mCompleteOrder = completeOrder;
    }

    public interface IPosOrder {
        void posOrder(int position);
    }

    public interface IOKOrder {
        void okOrder(int position);
    }

    public interface IComplete {
        void completeOrder(int position);
    }
}