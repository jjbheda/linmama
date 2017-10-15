package com.linmama.dinning.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.bean.ResultsBean;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/6
 */
public class TakingOrderAdapter extends BaseAdapter {
    private final static int ITEM_TYPE1 = 0;
    private final static int ITEM_TYPE2 = 1;

    private List<ResultsBean> mResults;
    private LayoutInflater mInflater;
    private Context mContext;
    private IPosOrder mPosOrder;
//    private ICancelRingOrder mCancelRingOrder;
    private IOKOrder mOkOrder;
    private IComplete mCompleteOrder;

    public TakingOrderAdapter(Context context, List<ResultsBean> results) {
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
        ResultsBean rb = mResults.get(position);
        String payStatus = rb.getPay_status();
        boolean isInStore = rb.is_in_store();
//        List<OrderWarnsBean> warns = rb.getOrderWarms();
        int type = ITEM_TYPE1;
        if (payStatus.equals("1")) {
            type = ITEM_TYPE2;
        } else if (payStatus.equals("2") && isInStore) {
            type = ITEM_TYPE1;
        }
//        else if (payStatus.equals("2") && !isInStore && null != warns && warns.size() > 0) {
//            type = ITEM_TYPE2;
//        } else if (payStatus.equals("2") && !isInStore && (null == warns || warns.size() <= 0)) {
//            type = ITEM_TYPE1;
//        } else if (payStatus.equals("2") && isInStore) {
//            type = ITEM_TYPE1;
//        }
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

    public void removeItem(int i) {
        ResultsBean rb = mResults.remove(i);
        if (null != rb) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(i);
        if (view == null) {
            if (type == ITEM_TYPE1) {
                holder1 = new ViewHolder1();
                view = mInflater.inflate(R.layout.lv_item_takingorder1, viewGroup, false);
                holder1.icon = (ImageView) view.findViewById(R.id.ivTakeIcon);
                holder1.desk = (TextView) view.findViewById(R.id.tvTakeDesk);
                holder1.person = (TextView) view.findViewById(R.id.tvTakePerson);
                holder1.way = (TextView) view.findViewById(R.id.tvTakeWay);
                holder1.serial = (TextView) view.findViewById(R.id.tvTakeSerial);
                holder1.datetime = (TextView) view.findViewById(R.id.tvTakeDatetime);
                holder1.amount = (TextView) view.findViewById(R.id.tvTakeAmount);
                holder1.payStatus = (TextView) view.findViewById(R.id.tvTakePayStatus);
                holder1.pos = (Button) view.findViewById(R.id.btnTakePos);
                holder1.complete = (Button) view.findViewById(R.id.btnTakeComplete);
                view.setTag(holder1);
            } else if (type == ITEM_TYPE2) {
                holder2 = new ViewHolder2();
                view = mInflater.inflate(R.layout.lv_item_takingorder2, viewGroup, false);
                holder2.icon = (ImageView) view.findViewById(R.id.ivTakeIcon2);
                holder2.desk = (TextView) view.findViewById(R.id.tvTakeDesk2);
                holder2.person = (TextView) view.findViewById(R.id.tvTakePerson2);
                holder2.way = (TextView) view.findViewById(R.id.tvTakeWay2);
                holder2.serial = (TextView) view.findViewById(R.id.tvTakeSerial2);
                holder2.datetime = (TextView) view.findViewById(R.id.tvTakeDatetime2);
                holder2.other = (TextView) view.findViewById(R.id.tvTakeOther2);
                holder2.amount = (TextView) view.findViewById(R.id.tvTakeAmount2);
                holder2.remind = (TextView) view.findViewById(R.id.tvTakeRemind2);
                holder2.payStatus = (TextView) view.findViewById(R.id.tvTakePayStatus2);
                holder2.pos = (Button) view.findViewById(R.id.btnTakePos2);
//                holder2.cancel = (Button) view.findViewById(R.id.btnTakeCancel2);
                holder2.ok = (Button) view.findViewById(R.id.btnTakeOK2);
                holder2.complete = (Button) view.findViewById(R.id.btnTakeComplete2);
                view.setTag(holder2);
            }
        } else {
            if (type == ITEM_TYPE1) {
                holder1 = (ViewHolder1) view.getTag();
            } else if (type == ITEM_TYPE2) {
                holder2 = (ViewHolder2) view.getTag();
            }
        }
        ResultsBean rb = mResults.get(i);
        boolean isInStore = rb.is_in_store();
        String deskNum = rb.getDesk_num();
        String diningWay = rb.getDining_way();
        String payStatus = rb.getPay_status();
        String payChannel = rb.getPay_channel();
        if (type == ITEM_TYPE1) {
            if (isInStore && !TextUtils.isEmpty(deskNum)) {
                holder1.icon.setImageResource(R.mipmap.icon_online);
                holder1.desk.setText(deskNum);
                holder1.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderDeskNum));
            } else if (isInStore && TextUtils.isEmpty(deskNum)) {
                holder1.icon.setImageResource(R.mipmap.icon_fake);
                holder1.desk.setText("来客单");
                holder1.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderTake));
            } else if (!isInStore) {
                holder1.icon.setImageResource(R.mipmap.icon_appoint);
                holder1.desk.setText("预约单");
                holder1.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
            }
            holder1.person.setText(String.format(mContext.getResources().getString(R.string.new_order_person), rb.getDine_num()));
            if (diningWay.equals("1")) {
                holder1.way.setText("(堂食)");
            } else if (diningWay.equals("2")) {
                holder1.way.setText("(外带)");
            }
            holder1.serial.setText(String.format(mContext.getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
            holder1.datetime.setText(String.format(mContext.getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
            holder1.amount.setText(String.format(mContext.getResources().getString(R.string.order_money), rb.getTotal_amount()));
            if (payStatus.equals("2")) {
                holder1.payStatus.setText("(已支付)");
                holder1.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderText));
            } else if (payStatus.equals("1")){
                holder1.payStatus.setText("(未支付)");
                holder1.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
            }
            holder1.pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != TakingOrderAdapter.this.mPosOrder) {
                        TakingOrderAdapter.this.mPosOrder.posOrder(i);
                    }
                }
            });
            holder1.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != TakingOrderAdapter.this.mCompleteOrder) {
                        TakingOrderAdapter.this.mCompleteOrder.completeOrder(i);
                    }
                }
            });
        } else if (type == ITEM_TYPE2) {
            if (isInStore && TextUtils.isEmpty(deskNum)) {
                holder2.icon.setImageResource(R.mipmap.icon_fake);
                holder2.desk.setText("来客单");
                holder2.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderTake));
                holder2.ok.setVisibility(View.VISIBLE);
//                holder2.cancel.setVisibility(View.GONE);
                if (payStatus.equals("1")) {
                    holder2.other.setVisibility(View.VISIBLE);
                    if (payChannel.equals("1")) {
                        holder2.other.setText(String.format(mContext.getResources().getString(R.string.new_order_pay_way), "在线支付"));
                    } else if (payChannel.equals("2")) {
                        holder2.other.setText(String.format(mContext.getResources().getString(R.string.new_order_pay_way), "吧台支付"));
                    }
                } else if (payStatus.equals("2")){
                    holder2.other.setVisibility(View.GONE);
                }
            } else if (isInStore && !TextUtils.isEmpty(deskNum)) {
                holder2.icon.setImageResource(R.mipmap.icon_online);
                holder2.desk.setText(deskNum);
                holder2.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderDeskNum));
                holder2.other.setVisibility(View.VISIBLE);
                holder2.ok.setVisibility(View.VISIBLE);
//                holder2.cancel.setVisibility(View.GONE);
                if (payChannel.equals("1")) {
                    holder2.other.setText(String.format(mContext.getResources().getString(R.string.new_order_pay_way), "在线支付"));
                } else if (payChannel.equals("2")) {
                    holder2.other.setText(String.format(mContext.getResources().getString(R.string.new_order_pay_way), "吧台支付"));
                }
            } else if (!isInStore) {
                holder2.icon.setImageResource(R.mipmap.icon_appoint);
                holder2.desk.setText("预约单");
                holder2.desk.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
                holder2.ok.setVisibility(View.GONE);
//                if (null != rb.getOrderWarms() && rb.getOrderWarms().size() > 0) {
//                    holder2.cancel.setVisibility(View.VISIBLE);
//                } else {
//                    holder2.cancel.setVisibility(View.GONE);
//                }
                holder2.other.setVisibility(View.VISIBLE);
                String inStoreTime = rb.getIn_store_time();
                holder2.other.setText(String.format(mContext.getResources().getString(R.string.taking_order_remind), inStoreTime));
            }
            holder2.person.setText(String.format(mContext.getResources().getString(R.string.new_order_person), rb.getDine_num()));
            if (diningWay.equals("1")) {
                holder2.way.setText("(堂食)");
            } else if (diningWay.equals("2")) {
                holder2.way.setText("(外带)");
            }
            holder2.serial.setText(String.format(mContext.getResources().getString(R.string.new_order_serial), rb.getSerial_number()));
            holder2.datetime.setText(String.format(mContext.getResources().getString(R.string.new_order_datetime), rb.getOrder_datetime_bj()));
            holder2.amount.setText(String.format(mContext.getResources().getString(R.string.order_money), rb.getTotal_amount()));
            if (payStatus.equals("1")) {
                holder2.payStatus.setText("(未支付)");
                holder2.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
            } else if (payStatus.equals("2")) {
                holder2.payStatus.setText("(已支付)");
                holder2.payStatus.setTextColor(mContext.getResources().getColor(R.color.colorOrderText));
            }
            holder2.pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != TakingOrderAdapter.this.mPosOrder) {
                        TakingOrderAdapter.this.mPosOrder.posOrder(i);
                    }
                }
            });
//            holder2.cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (null != TakingOrderAdapter.this.mCancelRingOrder) {
//                        TakingOrderAdapter.this.mCancelRingOrder.cancelRingOrder(i);
//                    }
//                }
//            });
            holder2.ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != TakingOrderAdapter.this.mOkOrder) {
                        TakingOrderAdapter.this.mOkOrder.okOrder(i);
                    }
                }
            });
            holder2.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != TakingOrderAdapter.this.mCompleteOrder) {
                        TakingOrderAdapter.this.mCompleteOrder.completeOrder(i);
                    }
                }
            });
        }
        return view;
    }

    private static class ViewHolder1 {
        ImageView icon;
        TextView desk;
        TextView person;
        TextView way;
        TextView serial;
        TextView datetime;
        TextView amount;
        TextView payStatus;
        Button pos;
        Button complete;
    }

    private static class ViewHolder2 {
        ImageView icon;
        TextView desk;
        TextView person;
        TextView way;
        TextView serial;
        TextView datetime;
        TextView other;
        TextView remind;
        TextView amount;
        TextView payStatus;
        Button pos;
//        Button cancel;
        Button ok;
        Button complete;
    }

    public void setPosOrder(IPosOrder posOrder) {
        this.mPosOrder = posOrder;
    }

//    public void setCancelOrder(ICancelRingOrder cancelRingOrder) {
//        this.mCancelRingOrder = cancelRingOrder;
//    }

    public void setOkOrder(IOKOrder okOrder) {
        this.mOkOrder = okOrder;
    }

    public void setCompleteOrder(IComplete completeOrder) {
        this.mCompleteOrder = completeOrder;
    }

    public interface IPosOrder {
        void posOrder(int position);
    }

//    public interface ICancelRingOrder {
//        void cancelRingOrder(int position);
//    }

    public interface IOKOrder {
        void okOrder(int position);
    }

    public interface IComplete {
        void completeOrder(int position);
    }
}
