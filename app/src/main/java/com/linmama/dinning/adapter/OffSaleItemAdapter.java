package com.linmama.dinning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.linmama.dinning.R;
import com.linmama.dinning.goods.onsale.ShopItemBean;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/11
 */

public class OffSaleItemAdapter extends BaseAdapter {
    private List<ShopItemBean> mResults;
    private LayoutInflater mInflater;
    private OnOnItem mOnOnItem;
    private Context mContext;

    public OffSaleItemAdapter(Context context, List<ShopItemBean> results) {
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
        if (mResults != null && mResults.size() > i) {
            return mResults.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void removeItem(int i) {
        ShopItemBean bean = mResults.remove(i);
        if (null != bean) {
            notifyDataSetChanged();
        }
    }

    public void clearItems() {
        mResults.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.lv_item_offsale, viewGroup, false);
            holder.icon = (ImageView) view.findViewById(R.id.ivFoodIcon);
            holder.name = (TextView) view.findViewById(R.id.tvFoodName);
            holder.num = (TextView) view.findViewById(R.id.tvFoodNum);
            holder.on = (Button) view.findViewById(R.id.btnSoldOn);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ShopItemBean bean = mResults.get(i);
        holder.name.setText(bean.getName());
        holder.on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnOnItem) {
                    mOnOnItem.onItem(bean);
                }
            }
        });
        Picasso.with(mContext)
                .load( bean.getThumbnail())
                .placeholder(R.mipmap.ic_load)
                .error(R.mipmap.ic_load)
                .into(holder.icon);
        return view;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView num;
        Button on;
    }

    public void setOnOnItem(OnOnItem onOnItem) {
        this.mOnOnItem = onOnItem;
    }

    public interface OnOnItem {
        void onItem(ShopItemBean bean);
    }
}
