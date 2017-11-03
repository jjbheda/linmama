package com.linmama.dinning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linmama.dinning.R;
import com.linmama.dinning.goods.category.MenuCategoryBean;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/11
 */

public class MenuCategoryAdapter extends BaseAdapter {
    private List<MenuCategoryBean> mResults;
    private LayoutInflater mInflater;

    public MenuCategoryAdapter(Context context, List<MenuCategoryBean> results) {
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
            view = mInflater.inflate(R.layout.lv_item_category, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.tvName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MenuCategoryBean bean = mResults.get(i);
        holder.name.setText(bean.name);
        return view;
    }

    private static class ViewHolder {
        TextView name;
    }
}
