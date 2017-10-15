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
import com.xcxid.dinning.bean.RemindResultsBean;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class RemindOrderAdapter extends BaseAdapter {

    private List<RemindResultsBean> mResults;
    private LayoutInflater mInflater;
    private Context mContext;
    private IHandleWarn mHandleWarn;

    public RemindOrderAdapter(Context context, List<RemindResultsBean> results) {
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
            view = mInflater.inflate(R.layout.lv_item_remindorder, viewGroup, false);
            holder.warn = (TextView) view.findViewById(R.id.tvRemindTime);
            holder.icon = (ImageView) view.findViewById(R.id.ivRemindIcon);
            holder.desk = (TextView) view.findViewById(R.id.tvRemindDesk);
            holder.person = (TextView) view.findViewById(R.id.tvRemindPerson);
            holder.way = (TextView) view.findViewById(R.id.tvRemindWay);
            holder.serial = (TextView) view.findViewById(R.id.tvRemindSerial);
            holder.datetime = (TextView) view.findViewById(R.id.tvRemindDatetime);
            holder.other = (TextView) view.findViewById(R.id.tvRemindOther);
            holder.handle = (Button) view.findViewById(R.id.btnRemindHandle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        RemindResultsBean bean = mResults.get(i);
        ResultsBean rb = bean.getOrder();
        boolean isInStore = rb.is_in_store();
        String deskNum = rb.getDesk_num();
        String diningWay = rb.getDining_way();
        String warnType = bean.getWarn_type();
        String warnTime = bean.getWarn_time();
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
            holder.other.setVisibility(View.VISIBLE);
            String inStoreTime = rb.getIn_store_time();
            holder.other.setText(String.format(mContext.getResources().getString(R.string.taking_order_remind), warnTime));
            if (warnType.equals("2")) {
                holder.warn.setText(String.format(mContext.getResources().getString(R.string.remind_order_remind_time), inStoreTime));
                holder.warn.setBackgroundResource(R.drawable.login_button_enabled_bg);
            } else if (warnType.equals("1")) {
                holder.warn.setText(String.format(mContext.getResources().getString(R.string.remind_order_remind), warnTime));
                holder.warn.setBackgroundResource(R.drawable.btn_ok_bg);
            }
        }
        holder.person.setText(String.format(mContext.getResources().getString(R.string.new_order_person), rb.getDine_num()));
        if (diningWay.equals("1")) {
            holder.way.setText("(堂食)");
        } else if (diningWay.equals("2")) {
            holder.way.setText("(外带)");
        }
        holder.serial.setText(String.format(mContext.getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
        holder.datetime.setText(String.format(mContext.getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
        holder.handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != RemindOrderAdapter.this.mHandleWarn) {
                    RemindOrderAdapter.this.mHandleWarn.handleWarn(i);
                }
            }
        });
        return view;
    }

    private static class ViewHolder {
        TextView warn;
        ImageView icon;
        TextView desk;
        TextView person;
        TextView way;
        TextView serial;
        TextView datetime;
        TextView other;
        Button handle;
    }

    public void setHandleWarn(IHandleWarn handleWarn) {
        this.mHandleWarn = handleWarn;
    }

    public interface IHandleWarn {
        void handleWarn(int position);
    }

}
