package com.xcxid.dinning.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.bean.QuitOrderBean;
import com.xcxid.dinning.bean.QuitOrderInfoBean;
import com.xcxid.dinning.bean.RefundBean;
import com.xcxid.dinning.widget.QuitOrderRefundItem;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class QuitOrderAdapter extends BaseAdapter {

    private List<QuitOrderBean> mResults;
    private LayoutInflater mInflater;
    private Context mContext;
    private IRefund mRefund;
    private IRefuse mRefuse;

    public QuitOrderAdapter(Context context, List<QuitOrderBean> results) {
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

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.lv_item_quitorder, viewGroup, false);
            holder.icon = (ImageView) view.findViewById(R.id.ivQuitIcon);
            holder.desk = (TextView) view.findViewById(R.id.tvQuitDesk);
            holder.person = (TextView) view.findViewById(R.id.tvQuitPerson);
            holder.way = (TextView) view.findViewById(R.id.tvQuitWay);
            holder.serial = (TextView) view.findViewById(R.id.tvQuitSerial);
            holder.datetime = (TextView) view.findViewById(R.id.tvQuitDatetime);
            holder.refunds = (LinearLayout) view.findViewById(R.id.llRefund);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        QuitOrderBean bean = mResults.get(i);
        QuitOrderInfoBean orderInfo = bean.getOrder_info();
        List<RefundBean> refunds = bean.getRefund_applications();
        boolean isInStore = orderInfo.is_in_store();
        String deskNum = orderInfo.getDesk_num();
        String diningWay = orderInfo.getDining_way();
        if (isInStore && !TextUtils.isEmpty(deskNum)) {
            holder.icon.setImageResource(R.mipmap.icon_online);
            holder.desk.setText(deskNum);
            holder.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderDeskNum));
        } else if (isInStore && TextUtils.isEmpty(deskNum)) {
            holder.icon.setImageResource(R.mipmap.icon_fake);
            holder.desk.setText("来客单");
            holder.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderTake));
        } else if (!isInStore) {
            holder.icon.setImageResource(R.mipmap.icon_appoint);
            holder.desk.setText("预约单");
            holder.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
        }
        holder.person.setText(String.format(mContext.getResources().getString(R.string.new_order_person), orderInfo.getDine_num()));
        if (diningWay.equals("1")) {
            holder.way.setText("(堂食)");
        } else if (diningWay.equals("2")) {
            holder.way.setText("(外带)");
        }
        holder.serial.setText(String.format(mContext.getResources().getString(R.string.new_order_serial), orderInfo.getSerial_number()));
        holder.datetime.setText(String.format(mContext.getResources().getString(R.string.new_order_datetime), orderInfo.getOrder_datetime_bj()));
        holder.refunds.removeAllViews();
        for (int k = 0, size = refunds.size(); k < size; k++) {
            RefundBean refund = refunds.get(k);
            QuitOrderRefundItem item = new QuitOrderRefundItem(mContext);
            item.setName(refund.getMenu_item_name());
            item.setNum(String.format(mContext.getResources().getString(R.string.quit_order_num), refund.getNum()));
            item.setAmount(String.format(mContext.getResources().getString(R.string.order_money), refund.getRefund_amount()));
            final int finalK = k;
            item.getIgnore().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != QuitOrderAdapter.this.mRefuse) {
                        QuitOrderAdapter.this.mRefuse.refuseRefund(i, finalK);
                    }
                }
            });
            item.getRefund().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != QuitOrderAdapter.this.mRefund) {
                        QuitOrderAdapter.this.mRefund.refund(i, finalK);
                    }
                }
            });
            holder.refunds.addView(item);
        }
        return view;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView desk;
        TextView person;
        TextView way;
        TextView serial;
        TextView datetime;
        LinearLayout refunds;
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
