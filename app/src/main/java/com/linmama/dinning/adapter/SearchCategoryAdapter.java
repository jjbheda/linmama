package com.linmama.dinning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linmama.dinning.bean.SearchItemResultsBean;
import com.squareup.picasso.Picasso;
import com.linmama.dinning.R;

import java.util.List;

/**
 * Created by jingkang onOrOff 2017/3/11
 */

public class SearchCategoryAdapter extends BaseAdapter {
    private List<SearchItemResultsBean> mResults;
    private LayoutInflater mInflater;
    private IOnItem mOnItem;
    private IOffItem mOffItem;
    private Context mContext;
    private static final int ITEM_TYPE1 = 0;
    private static final int ITEM_TYPE2 = 1;

    public SearchCategoryAdapter(Context context, List<SearchItemResultsBean> results) {
        this.mContext = context;
        this.mResults = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        SearchItemResultsBean bean = mResults.get(position);
        boolean isOnSell = bean.is_on_sell();
        if (isOnSell) {
            return ITEM_TYPE1;
        } else {
            return ITEM_TYPE2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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

    public void updateItem(int i) {
        SearchItemResultsBean bean = mResults.get(i);
        if (null != bean) {
            bean.setIs_on_sell(!bean.is_on_sell());
            notifyDataSetChanged();
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
                view = mInflater.inflate(R.layout.lv_item_onsearch, viewGroup, false);
                holder1.icon = (ImageView) view.findViewById(R.id.ivFoodIcon);
                holder1.name = (TextView) view.findViewById(R.id.tvFoodName);
                holder1.onSale = (TextView) view.findViewById(R.id.tvOnSale);
                holder1.off = (Button) view.findViewById(R.id.btnSoldOut);
                view.setTag(holder1);
            } else if (type == ITEM_TYPE2) {
                holder2 = new ViewHolder2();
                view = mInflater.inflate(R.layout.lv_item_offsearch, viewGroup, false);
                holder2.icon = (ImageView) view.findViewById(R.id.ivFoodIcon);
                holder2.name = (TextView) view.findViewById(R.id.tvFoodName);
                holder2.offSale = (TextView) view.findViewById(R.id.tvOffSale);
                holder2.on = (Button) view.findViewById(R.id.btnSoldOn);
                view.setTag(holder2);
            }
        } else {
            if (type == ITEM_TYPE1) {
                holder1 = (ViewHolder1) view.getTag();
            } else if (type == ITEM_TYPE2) {
                holder2 = (ViewHolder2) view.getTag();
            }
        }
        SearchItemResultsBean bean = mResults.get(i);
        if (type == ITEM_TYPE1 && null != holder1) {
            holder1.name.setText(bean.getName());
            holder1.onSale.setText("在售");
            holder1.off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mOffItem) {
                        mOffItem.offItem(i);
                    }
                }
            });
            Picasso.with(mContext)
                    .load(bean.getSmall_image())
                    .placeholder(R.mipmap.ic_load)
                    .error(R.mipmap.ic_load)
                    .into(holder1.icon);
        } else if (type == ITEM_TYPE2 && null != holder2) {
            holder2.name.setText(bean.getName());
            holder2.offSale.setText("已下架");
            holder2.on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mOnItem) {
                        mOnItem.onItem(i);
                    }
                }
            });
            Picasso.with(mContext)
                    .load(bean.getSmall_image())
                    .placeholder(R.mipmap.ic_load)
                    .error(R.mipmap.ic_load)
                    .into(holder2.icon);
        }
        return view;
    }

    private static class ViewHolder1 {
        ImageView icon;
        TextView name;
        TextView onSale;
        Button off;
    }

    private static class ViewHolder2 {
        ImageView icon;
        TextView name;
        TextView offSale;
        Button on;
    }

    public void setIOnItem(IOnItem onItem) {
        this.mOnItem = onItem;
    }

    public void setIOffItem(IOffItem offItem) {
        this.mOffItem = offItem;
    }

    public interface IOnItem {
        void onItem(int position);
    }

    public interface IOffItem {
        void offItem(int position);
    }
}
