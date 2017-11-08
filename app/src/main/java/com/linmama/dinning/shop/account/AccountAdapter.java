package com.linmama.dinning.shop.account;

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
import com.linmama.dinning.bean.AccountBeanItem;
import com.linmama.dinning.shop.bean.SaleRankBean;

import java.util.List;

public class AccountAdapter extends BaseAdapter {
    private List<AccountBeanItem> mResults;
    private LayoutInflater mInflater;
    private Context mContext;

    public AccountAdapter(Context context, List<AccountBeanItem> results) {
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
            view = mInflater.inflate(R.layout.account_item_fragment, viewGroup, false);
            holder.date = (TextView) view.findViewById(R.id.date_tv);
            holder.income = (TextView) view.findViewById(R.id.income_tv);
            holder.text = (TextView) view.findViewById(R.id.text_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        AccountBeanItem bean = mResults.get(i);
        holder.date.setText(bean.date+"日账单");
        holder.income.setText(bean.income);
        holder.text.setText(bean.text);
        return view;
    }

    private static class ViewHolder {
        TextView date;
        TextView income;
        TextView text;
    }
}
