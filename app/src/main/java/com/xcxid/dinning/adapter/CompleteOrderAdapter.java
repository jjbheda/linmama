package com.xcxid.dinning.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.bean.ResultsBean;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class CompleteOrderAdapter extends BaseAdapter {

    private List<ResultsBean> mResults;
    private LayoutInflater mInflater;
    private Context mContext;
    private IPosOrder mPosOrder;

    public CompleteOrderAdapter(Context context, List<ResultsBean> results) {
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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.lv_item_completeorder, viewGroup, false);
            holder.icon = (ImageView) view.findViewById(R.id.ivCompleteIcon);
            holder.desk = (TextView) view.findViewById(R.id.tvCompleteDesk);
            holder.person = (TextView) view.findViewById(R.id.tvCompletePerson);
            holder.way = (TextView) view.findViewById(R.id.tvCompleteWay);
            holder.serial = (TextView) view.findViewById(R.id.tvCompleteSerial);
            holder.datetime = (TextView) view.findViewById(R.id.tvCompleteDatetime);
            holder.amount = (TextView) view.findViewById(R.id.tvCompleteAmount);
            holder.payStatus = (TextView) view.findViewById(R.id.tvCompletePayStatus);
            holder.pos = (Button) view.findViewById(R.id.btnCompletePos);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ResultsBean rb = mResults.get(i);
        boolean isInStore = rb.is_in_store();
        String deskNum = rb.getDesk_num();
        String diningWay = rb.getDining_way();
        String payStatus = rb.getPay_status();
        // String payChannel = rb.getPay_channel();
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
        holder.person.setText(String.format(mContext.getResources().getString(R.string.new_order_person), rb.getDine_num()));
        if (diningWay.equals("1")) {
            holder.way.setText("(堂食)");
        } else if (diningWay.equals("2")) {
            holder.way.setText("(外带)");
        }
        holder.serial.setText(String.format(mContext.getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
        holder.datetime.setText(String.format(mContext.getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
        holder.amount.setText(String.format(mContext.getResources().getString(R.string.order_money), rb.getTotal_amount()));
        if (payStatus.equals("2")) {
            holder.payStatus.setText("(已支付)");
            holder.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderText));
        } else if (payStatus.equals("1")) {
            holder.payStatus.setText("(未支付)");
            holder.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
        }
        holder.pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != CompleteOrderAdapter.this.mPosOrder) {
                    CompleteOrderAdapter.this.mPosOrder.posOrder(i);
                }
            }
        });
        return view;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView desk;
        TextView person;
        TextView way;
        TextView serial;
        TextView datetime;
        TextView amount;
        TextView payStatus;
        Button pos;
    }

    public void setPosOrder(IPosOrder posOrder) {
        this.mPosOrder = posOrder;
    }

    public interface IPosOrder {
        void posOrder(int position);
    }

}
