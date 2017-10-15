package com.linmama.dinning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linmama.dinning.goods.item.MenuItemResultsBean;
import com.squareup.picasso.Picasso;
import com.linmama.dinning.R;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/11
 */

public class OnSaleItemAdapter extends BaseAdapter {
    private List<MenuItemResultsBean> mResults;
    private LayoutInflater mInflater;
    private OnOffItem mOnOffItem;
    private Context mContext;

    public OnSaleItemAdapter(Context context, List<MenuItemResultsBean> results) {
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

    public void removeItem(int i) {
        MenuItemResultsBean bean = mResults.remove(i);
        if (null != bean) {
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.lv_item_onsale, viewGroup, false);
            holder.icon = (ImageView) view.findViewById(R.id.ivFoodIcon);
            holder.name = (TextView) view.findViewById(R.id.tvFoodName);
            holder.num = (TextView) view.findViewById(R.id.tvFoodNum);
            holder.off = (Button) view.findViewById(R.id.btnSoldOut);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MenuItemResultsBean bean = mResults.get(i);
        holder.name.setText(bean.getName());
        holder.num.setText(bean.getCode());
        holder.off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnOffItem) {
                    mOnOffItem.offItem(i);
                }
            }
        });
        Picasso.with(mContext)
                .load(String.format(mContext.getResources().getString(R.string.picasso_load_url), bean.getSmall_image()))
                .placeholder(R.mipmap.ic_load)
                .error(R.mipmap.ic_load)
                .into(holder.icon);
        return view;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView num;
        Button off;
    }

    public void setOnOffItem(OnOffItem onOffItem) {
        this.mOnOffItem = onOffItem;
    }

    public interface OnOffItem {
        void offItem(int position);
    }
}
