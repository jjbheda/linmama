package com.linmama.dinning.order.orderundosearch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class OrderUndoSearchAdapter extends BaseAdapter {

    private List<TakingOrderBean> mResults;
    private LayoutInflater mInflater;
    private Activity mContext;
    private IRefundRetry mRefundRetry;
    private IPrintOrder mPrintOrder;
    private ICancelFinishedOrder mCancelOrder;
    private int searchType = 0;  //0 已完成订单    1  退款未成功

    public OrderUndoSearchAdapter(Activity context, int searchType, List<TakingOrderBean> results) {
        this.mContext = context;
        this.mResults = results;
        this.searchType = searchType;
        mInflater = LayoutInflater.from(context);
    }

    public void setRefundRetry(IRefundRetry refundRetry) {
        mRefundRetry = refundRetry;
    }

    public void setPrintOrder(IPrintOrder printOrder) {
        mPrintOrder = printOrder;
    }

    public void setCancelOrder(ICancelFinishedOrder cancelOrder) {
        mCancelOrder = cancelOrder;
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
            view = mInflater.inflate(R.layout.order_search_common_layout, viewGroup, false);
            holder1.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder1.order_time = (TextView) view.findViewById(R.id.order_time);
            holder1.table_num = (TextView) view.findViewById(R.id.table_num_search);
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
            holder1.btnPrint = (TextView) view.findViewById(R.id.btnPrint);
            holder1.tv_notes_order = (TextView) view.findViewById(R.id.notes_order);
            view.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) view.getTag();
        }
        final TakingOrderBean bean = (TakingOrderBean) getItem(i);
        if (bean == null || holder1 == null) {
            return view;
        }
        holder1.tv_name.setText(bean.user.user_name);
        holder1.order_time.setText(bean.order_datetime_bj);
        holder1.table_num.setText(bean.order_no + "");
        holder1.parcel_iv.setText(bean.is_for_here.equals("0") ? "自取" : "堂食");
        holder1.tv_remark.setText(bean.remark);
        holder1.haspay_tv.setText(bean.pay_amount);
        holder1.pay_tv.setText(bean.pay_amount);
        holder1.tv_serial_number.setText("单号 ： " + bean.serial_number);
        holder1.tv_delivery_address_name.setText(bean.place.place_name);
        holder1.tv_delivery_address.setText(bean.place.place_address);
        holder1.shrint_btn = (TextView) view.findViewById(R.id.shrint_tv);
        holder1.order_goods_lt = (LinearLayout) view.findViewById(R.id.order_goods_lt);
        holder1.goods_shrink_lt = (LinearLayout) view.findViewById(R.id.goods_shrink_lt);
        holder1.phone_lt = (LinearLayout) view.findViewById(R.id.phone_lt);
        holder1.complete = (TextView) view.findViewById(R.id.btnNewCancel2);
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
        shrintLt = holder1.goods_shrink_lt;
        holder1.shrint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shrintLt.getVisibility() == View.VISIBLE) {
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
                    ContectUtils.onCall(mContext, bean.user.user_tel);
            }
        });
        String type = "已完成";
        if (searchType == 0) {
            //0 可取消 1已取消 2 已退款
            if (bean.status.equals("0")) {
                type = "可取消";
                holder1.complete.setTextColor(mContext.getResources().getColor(R.color.gray_bg_color));
                holder1.complete.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_commit_bg));
            } else if (bean.status.equals("1")) {
                type = "已取消";
                holder1.complete.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_cancel_bg));
            } else if (bean.status.equals("2")){
                type = "已退款";
                holder1.complete.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_cancel_bg));
            }
            holder1.complete.setText(type);

        } else {
            type = "已完成";
            holder1.complete.setText("再次退款");
            holder1.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mRefundRetry != null) {
                        mRefundRetry.refundRetry(bean);
                    }
                }
            });
        }
        holder1.tv_order_status.setText(type);
        if (!bean.fail_reson.equals("")) {
            holder1.tv_notes_order.setVisibility(View.VISIBLE);
            holder1.tv_notes_order.setText(bean.fail_reson);
        } else {
            holder1.tv_notes_order.setVisibility(View.GONE);
        }

        holder1.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchType == 1 && mRefundRetry != null) {        //退款未成功
                    mRefundRetry.refundRetry(bean);
                } else if (searchType == 0 && bean.status.equals("0") && mCancelOrder != null) {
                    mCancelOrder.cancelOrder(bean);
                }
            }
        });

        holder1.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPrintOrder != null) {
                    mPrintOrder.printOrder(bean);
                }
            }
        });

        holder1.order_time_list.removeAllViews();
        View lt_view = mInflater.inflate(R.layout.lv_item_ordertime_single, null);
        TextView tv_order_takeoff_time = (TextView) lt_view.findViewById(R.id.order_takeoff_time);
        tv_order_takeoff_time.setText("取餐时间:" + bean.pickup.pickup_date + " " + bean.pickup.pickup_start_time + "-" + bean.pickup.pickup_end_time);
        tv_order_takeoff_time.setTextColor(mContext.getResources().getColor(R.color.menu_grey));
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
        TextView complete;
        TextView btnPrint;
        TextView tv_notes_order;

    }


    public interface IRefundRetry {
        void refundRetry(TakingOrderBean bean);
    }

    public interface IPrintOrder {
        void printOrder(TakingOrderBean bean);
    }

    public interface ICancelFinishedOrder {
        void cancelOrder(TakingOrderBean bean);
    }

}
