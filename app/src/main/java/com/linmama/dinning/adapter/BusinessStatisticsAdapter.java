package com.linmama.dinning.adapter;

/**
 * Created by jingkang onOrOff 2017/3/16
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.shop.bean.BusinessParseBean;
import com.linmama.dinning.shop.bean.SaleRankBean;

import java.util.List;

public class BusinessStatisticsAdapter extends BaseAdapter {
    private List<BusinessParseBean> mResults;
    private LayoutInflater mInflater;
    private Context mContext;

    public BusinessStatisticsAdapter(Context context, List<BusinessParseBean> results) {
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
            holder.index = (TextView) view.findViewById(R.id.rank);
            holder.income = (TextView) view.findViewById(R.id.category);
            holder.saleNum = (TextView) view.findViewById(R.id.saleNum);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BusinessParseBean bean = mResults.get(i);
            holder.index.setTextColor(mContext.getResources().getColor(R.color.colorOrderAppoint));
        holder.index.setText(bean.date);
        holder.income.setText(bean.income);
        holder.saleNum.setText(bean.total_ords);
        return view;
    }

    private static class ViewHolder {
        TextView index;
        TextView income;
        TextView saleNum;
    }
}
