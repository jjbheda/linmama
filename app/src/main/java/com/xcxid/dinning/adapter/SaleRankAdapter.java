package com.xcxid.dinning.adapter;

/**
 * Created by jingkang onOrOff 2017/3/16
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xcxid.dinning.R;
import com.xcxid.dinning.bean.SaleRankResultsBean;

import java.util.List;

public class SaleRankAdapter extends BaseAdapter {
    private List<SaleRankResultsBean> mResults;
    private LayoutInflater mInflater;
    private Context mContext;

    public SaleRankAdapter(Context context, List<SaleRankResultsBean> results) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.lv_item_salerank, viewGroup, false);
            holder.rank = (TextView) view.findViewById(R.id.rank);
            holder.category = (TextView) view.findViewById(R.id.category);
            holder.saleNum = (TextView) view.findViewById(R.id.saleNum);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SaleRankResultsBean bean = mResults.get(i);
        if (i == 0 || i ==1 || i==2) {
            holder.rank.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
        } else if ( i >= 3 && i <= 9) {
            holder.rank.setTextColor(mContext.getResources().getColor(R.color.colorOrderTake));
        } else {
            holder.rank.setTextColor(mContext.getResources().getColor(R.color.colorLoginEdit));
        }
        holder.rank.setText(String.valueOf(i + 1));
        holder.category.setText(bean.getName());
        holder.saleNum.setText(String.valueOf(bean.getNum()));
        return view;
    }

    private static class ViewHolder {
        TextView rank;
        TextView category;
        TextView saleNum;
    }
}
