package com.linmama.dinning.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.bean.OrderGoodBean;
import com.linmama.dinning.bean.TakingOrderBean;
import com.linmama.dinning.utils.ContectUtils;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class TakingOrderAdapter extends BaseAdapter {
    private final static int ITEM_TYPE1 = 0;

    private List<TakingOrderBean> mResults;
    private LayoutInflater mInflater;
    private Activity mContext;
    private ICompleteOrder mCommitOrder;
    private ICancelOrder mCancelOrder;
    private IPrintOrder mPrintOrder;
    private int mOrderStyle;
    //orderStyle  1 预约单 0 当日单
    public TakingOrderAdapter(Activity context, int orderStyle,List<TakingOrderBean> results) {
        this.mContext = context;
        this.mResults = results;
        this.mOrderStyle = orderStyle;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        TakingOrderBean rb = mResults.get(position);
        int type = ITEM_TYPE1;
        return type;
    }

    @Override
    public int getCount() {
        return mResults != null ? mResults.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        if (mResults != null && mResults.size() > i) {
            return mResults.get(i);
        } else {
            return null;
        }

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
            holder1.address_icon = (ImageView) view.findViewById(R.id.address_iv);
            holder1.goods_shrink_lt = (LinearLayout) view.findViewById(R.id.goods_shrink_lt);
            holder1.phone_lt = (LinearLayout) view.findViewById(R.id.phone_lt);
            holder1.ok = (TextView) view.findViewById(R.id.btnOrderCommit);
            holder1.cancel = (TextView) view.findViewById(R.id.btnNewCancel2);
            holder1.btnPrint = (TextView) view.findViewById(R.id.btnPrint);
            holder1.checkbox = (CheckBox) view.findViewById(R.id.checkbox);

            view.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) view.getTag();
        }
        final  LinearLayout shrintLt;
        final TextView shrint_btn;
        final TakingOrderBean bean = (TakingOrderBean) getItem(i);
        if (bean == null || holder1 == null) {
            return view;
        }
        holder1.tv_name.setText(bean.user.user_name);
        holder1.table_num.setText(bean.order_no+"");
        holder1.order_time.setText("下单时间 : "+bean.order_datetime_bj);
        holder1.parcel_iv.setText(bean.is_for_here.equals("0") ? "自取" : "堂食");
        if (mOrderStyle == 1){
            holder1.table_num.setBackgroundColor(mContext.getResources().getColor(R.color.colorOrderTake));
        } else {
            holder1.table_num.setBackgroundColor(mContext.getResources().getColor(R.color.actionsheet_red));
        }

        if (bean.is_for_here.equals("0")){
            holder1.address_icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.address_iv));
        } else {
            holder1.address_icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.address_logo));
        }

        holder1.tv_order_status.setText(bean.is_ensure_order.equals("0")?"未接单":"已接单");
        holder1.tv_remark.setText(bean.remark);
        holder1.haspay_tv.setText(bean.pay_amount);
        holder1.pay_tv.setText(bean.pay_amount);
        holder1.tv_serial_number.setText("单号: "+bean.serial_number);
        holder1.tv_delivery_address_name.setText(bean.place.place_name);
        holder1.tv_delivery_address.setText(bean.place.place_address);
        holder1.shrint_btn = (TextView) view.findViewById(R.id.shrint_tv);
        holder1.order_goods_lt.removeAllViews();
        for (OrderGoodBean bean1 : bean.goods_list) {
            View lt_view = mInflater.inflate(R.layout.lv_item_goods_single, null);
            TextView tv_name = (TextView) lt_view.findViewById(R.id.goods_name);
            TextView tv_num = (TextView) lt_view.findViewById(R.id.goods_number);
            TextView tv_price = (TextView) lt_view.findViewById(R.id.goods_price);
            tv_name.setText(bean1.name);
            tv_num.setText("X" + bean1.amount);
            tv_price.setText(bean1.total_price);
            holder1.order_goods_lt.addView(lt_view);
        }

        if (bean.remark.equals("")) {
            holder1.tv_remark.setVisibility(View.GONE);
        } else {
            holder1.tv_remark.setVisibility(View.VISIBLE);
            holder1.tv_remark.setText(bean.remark);
        }

        holder1.order_time_list.removeAllViews();
        View lt_view = mInflater.inflate(R.layout.lv_item_ordertime_single,null);
        TextView tv_order_takeoff_time = (TextView) lt_view.findViewById(R.id.order_takeoff_time);
        tv_order_takeoff_time.setText("取餐时间:" + bean.pickup.pickup_date + " " + bean.pickup.pickup_start_time + "-" + bean.pickup.pickup_end_time);
        holder1.order_time_list.addView(lt_view);

        shrintLt = holder1.goods_shrink_lt;
        shrint_btn = holder1.shrint_btn;
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


//        holder1.checkbox.setVisibility(mOrderStyle == 1 ? View.VISIBLE : View.GONE);
//        holder1.checkbox.setChecked(bean.checkBoxFlag);

        holder1.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (mCommitOrder!=null){
                   mCommitOrder.onCompleteOrder(bean);
               }
            }
        });

        holder1.ok.setText("完成");
        holder1.cancel.setText("取消");
        holder1.btnPrint.setVisibility(View.VISIBLE);
        holder1.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPrintOrder != null) {
                    mPrintOrder.onPrintOrder(bean);
                }
            }
        });

        return view;
    }

    private static class ViewHolder1 {
        TextView tv_name;
        ImageView address_icon;
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
        TextView cancel;
        TextView ok;
        TextView btnPrint;

        CheckBox checkbox;
    }

    public void setCommitOrder(ICompleteOrder commitOrder){
        mCommitOrder = commitOrder;
    }

    public void setCancelOrder(ICancelOrder cancelOrder){
        mCancelOrder = cancelOrder;
    }

    public void setPrintOrder(IPrintOrder printOrder){
        mPrintOrder = printOrder;
    }

    public interface ICompleteOrder {
        void onCompleteOrder(TakingOrderBean bean);
    }

    public interface ICancelOrder {
        void onCancelOrder(TakingOrderBean bean);
    }

    public interface IPrintOrder{
        void onPrintOrder(TakingOrderBean bean);
    }
}
