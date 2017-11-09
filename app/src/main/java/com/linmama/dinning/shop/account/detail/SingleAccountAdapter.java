package com.linmama.dinning.shop.account.detail;

/**
 * Created by jingkang onOrOff 2017/3/16
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.base.CommonActivity;
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.bean.SingleAccountItemBean;

import java.util.List;

public class SingleAccountAdapter extends BaseAdapter {
    private List<SingleAccountItemBean> mResults;
    private LayoutInflater mInflater;
    private Activity mContext;

    public SingleAccountAdapter(Activity context, List<SingleAccountItemBean> results) {
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
            view = mInflater.inflate(R.layout.single_account_item_fragment, viewGroup, false);
            holder.index_tv = (TextView) view.findViewById(R.id.index_tv);
            holder.type_tv = (TextView) view.findViewById(R.id.type_tv);
            holder.num_tv = (TextView) view.findViewById(R.id.num_tv);
            holder.income_tv = (TextView) view.findViewById(R.id.income_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
       final SingleAccountItemBean bean = mResults.get(i);
//        holder.index_tv.setText(bean.type);
        holder.type_tv.setText(bean.type);
        holder.num_tv.setText(bean.serial_number);
        holder.income_tv.setText(bean.income);
        return view;
    }

    private static class ViewHolder {
        TextView index_tv;
        TextView type_tv;
        TextView num_tv;
        TextView income_tv;
    }
}
